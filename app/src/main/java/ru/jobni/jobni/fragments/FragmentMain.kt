package ru.jobni.jobni.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.ActivityMainBinding
import ru.jobni.jobni.databinding.FragmentMainBinding
import ru.jobni.jobni.model.RepositoryVacancyEntity
import ru.jobni.jobni.model.SuggestionEntity
import ru.jobni.jobni.model.VacancyEntity
import ru.jobni.jobni.model.network.vacancy.CardVacancy
import ru.jobni.jobni.model.network.vacancy.ResultsVacancy
import ru.jobni.jobni.utils.CardRVAdapter
import ru.jobni.jobni.utils.Retrofit
import ru.jobni.jobni.utils.SearchLVAdapter
import ru.jobni.jobni.viewmodel.MainFragmentViewState
import ru.jobni.jobni.viewmodel.MainViewModel
import ru.jobni.jobni.viewmodel.clear
import java.util.*


class FragmentMain : Fragment() {

//    private val SERVER_RESPONSE_DELAY: Long = 1000 // 1 sec
//    private val SERVER_RESPONSE_MAX_COUNT: Int = 10

    private lateinit var cardRecyclerView: RecyclerView
    private var cardAdapter: CardRVAdapter = CardRVAdapter()
    private val cardLayoutManager: LinearLayoutManager = LinearLayoutManager(context)

    private lateinit var searchView: SearchView
    private lateinit var searchListAdapter: SearchLVAdapter
    private lateinit var searchListView: ListView
//    private var suggestionsNamesList = ArrayList<SuggestionEntity>()
//
//    var isLoading = true

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentMainBinding

    private val repository: RepositoryVacancyEntity = RepositoryVacancyEntity

    companion object {
        private val ARG_SET: String = "argSet"

        fun newInstance(str: String): FragmentMain {
            val fragment = FragmentMain()
            val args = Bundle()
            args.putString(ARG_SET, str)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


//        MartianDataBinding binding = DataBindingUtil.inflate(
//                inflater, R.layout.martian_data, container, false);
//        View view = binding.getRoot();
//        //here data must be an instance of the class MarsDataProvider
//        binding.setMarsdata(data);
//        return view;

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        val view = binding.root

        viewModel.setSearchViewVisible(true)

        binding.viewmodel = viewModel

        cardRecyclerView = binding.rvCards

        buildCardsRecyclerView()

        viewModel.getModelVacancy().observe(this, Observer { vacancy ->
            vacancy?.let { cardAdapter.vacancies = vacancy.vacancyList }
        })

        searchView = binding.mainToolbar.searchviewToolbar

        buildSearchView(view)

        searchListView = binding.lvSuggestions

        buildSearchListView()

        if (arguments != null) {
            if (arguments!!.getString(ARG_SET) == "SetFocus") {
                buildSearchTip()
            }

            if (arguments!!.getString(ARG_SET) == "SetCards") {
                viewModel.buildCardsList(0,0)
            }
        }

        viewModel.getSearchQuery().observe(this, Observer {
            cardRecyclerView.smoothScrollToPosition(0) // Вернуть пользователя к началу списка
        })

        viewModel.getSuggestionsNamesList().observe(this, Observer {
            searchListAdapter.notifyDataSetChanged()
        })

        return view
    }

    private fun buildSearchView(view: View) {
        searchView.setOnQueryTextListener(onQuerySearchView)

        //Find EditText view
        val et = view.findViewById(R.id.search_src_text) as EditText
        et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

        // Get the search close button image view
        val closeButton = view.findViewById(R.id.search_close_btn) as ImageView

        // Set on click listener
        closeButton.setOnClickListener {
            //Clear the text from EditText view
            et.setText("")

            //скрываем клавиатуру
            val viewCloseButton = activity?.currentFocus
            viewCloseButton?.let { v ->
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            searchView.clearFocus()
        }
    }

    private fun buildSearchTip() {
        searchView.requestFocus()
        //открываем клавиатуру
        val view = activity?.currentFocus
        view?.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(v, 0)
        }
    }

    private fun buildSearchListView() {

        searchListAdapter = SearchLVAdapter(context!!, viewModel.getSuggestionsNamesList().value!!)
        searchListView.adapter = searchListAdapter

//        searchListView.setOnItemClickListener { parent, viewClick, position, id ->
//            doSearchOnClick(suggestionsNamesList[position].suggestionName)
//            searchView.setQuery(suggestionsNamesList[position].suggestionName, true)
//            searchListView.visibility = View.GONE
//        }
    }

    private val onQuerySearchView = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            viewModel.doSearchOnClick(query)
            viewModel.isLoad = false
            searchListView.visibility = View.GONE

            return false
        }

        override fun onQueryTextChange(query: String): Boolean {
            var timer = Timer()

            if (query.isEmpty()) {
                viewModel.getSuggestionsNamesList().clear()
                searchListAdapter.notifyDataSetChanged()
                searchListView.visibility = View.GONE
            } else {
                searchListView.visibility = View.VISIBLE
                timer.cancel()
                timer = Timer()
                timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                viewModel.doSearchCompetence(query)
                            }
                        },
                        viewModel.SERVER_RESPONSE_DELAY
                )
            }
            return false
        }
    }

    private fun buildCardsRecyclerView() {

        cardRecyclerView.setHasFixedSize(true)
        cardRecyclerView.layoutManager = cardLayoutManager
        cardAdapter.setHasStableIds(true)
        cardRecyclerView.adapter = cardAdapter

        cardAdapter.setOnItemClickListener(object : CardRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(context, "onItemClick!", Toast.LENGTH_SHORT).show()
            }

            override fun onEyeClick(v: View, position: Int) {
                Toast.makeText(context, "onEyeClick!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}