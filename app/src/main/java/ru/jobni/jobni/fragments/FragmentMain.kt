package ru.jobni.jobni.fragments

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.FragmentMainBinding
import ru.jobni.jobni.utils.SearchLVAdapter
import ru.jobni.jobni.utils.VacancyRVAdapter
import ru.jobni.jobni.viewmodel.MainViewModel


class FragmentMain : Fragment() {

    private lateinit var vacancyRecyclerView: RecyclerView
    private lateinit var vacancyAdapter: VacancyRVAdapter

    private lateinit var searchView: SearchView
    private lateinit var searchListAdapter: SearchLVAdapter
    private lateinit var searchListView: ListView

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentMainBinding

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

        binding.lifecycleOwner = this

        val view = binding.root

        viewModel.setSearchViewVisible(true)

        binding.viewmodel = viewModel

        vacancyRecyclerView = binding.rvCards

        vacancyAdapter = VacancyRVAdapter(activity as Context)

        buildCardsRecyclerView()

        viewModel.getModelVacancy().observe(this, Observer { vacancy ->
            vacancy?.let { vacancyAdapter.vacancies = vacancy.vacancyList }
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
                viewModel.buildCardsList(0, 0)
            }
        }

        viewModel.getSearchQuery().observe(this, Observer {
            vacancyRecyclerView.smoothScrollToPosition(0) // Вернуть пользователя к началу списка
            searchView.setQuery(it, true)
        })

        viewModel.getSuggestionsNamesList().observe(this, Observer {
            searchListAdapter.notifyDataSetChanged()
        })

        return view
    }

    private fun buildSearchView(view: View) {

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
    }

    private fun buildCardsRecyclerView() {

        vacancyAdapter.setHasStableIds(true)
        vacancyRecyclerView.adapter = vacancyAdapter
    }
}