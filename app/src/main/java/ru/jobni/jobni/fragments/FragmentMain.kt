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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.R
import ru.jobni.jobni.model.SuggestionEntity
import ru.jobni.jobni.model.VacancyEntity
import ru.jobni.jobni.model.network.vacancy.CardVacancy
import ru.jobni.jobni.model.network.vacancy.ResultsVacancy
import ru.jobni.jobni.utils.CardRVAdapter
import ru.jobni.jobni.utils.Retrofit
import ru.jobni.jobni.utils.SearchLVAdapter
import java.util.*

class FragmentMain : Fragment() {

    private val SERVER_RESPONSE_DELAY: Long = 1000 // 1 sec
    private val SERVER_RESPONSE_MAX_COUNT: Int = 10

    private lateinit var mVacancyList: ArrayList<VacancyEntity>

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: CardRVAdapter

    private lateinit var searchView: SearchView
    private lateinit var searchListAdapter: SearchLVAdapter
    private lateinit var searchListView: ListView
    private var suggestionsNamesList = ArrayList<SuggestionEntity>()

    var isLoading = false

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
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        mRecyclerView = view.findViewById(R.id.rv_cards) as RecyclerView

        buildCardsRecyclerView()
        initScrollListener()

        searchView = activity?.findViewById(R.id.search_main) as SearchView
        //В глобальном тулбаре поле поиска отключено - включаем!
        searchView.visibility = View.VISIBLE
        buildSearchView(view)

        searchListView = view.findViewById(R.id.lv_suggestions) as ListView

        buildSearchListView()

        if (arguments != null) {
            if (arguments!!.getString(ARG_SET) == "SetFocus") {
                buildSearchTip()
            }

            if (arguments!!.getString(ARG_SET) == "SetCards") {
                buildCardsList()
            }
        }

        return view
    }

    private fun buildSearchView(view: View) {
        searchView.queryHint = getString(R.string.search_view_hint)
        searchView.setOnQueryTextListener(onQuerySearchView)

        //Find EditText view
        val et = activity?.findViewById(R.id.search_src_text) as EditText
        et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

        // Get the search close button image view
        val closeButton = activity?.findViewById(R.id.search_close_btn) as ImageView

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
        suggestionsNamesList = ArrayList()

        searchListAdapter = SearchLVAdapter(context!!, suggestionsNamesList)
        searchListView.adapter = searchListAdapter

        searchListView.setOnItemClickListener { parent, viewClick, position, id ->
            doSearchOnClick(suggestionsNamesList[position].suggestionName)
            searchView.setQuery(suggestionsNamesList[position].suggestionName, true)
            searchListView.visibility = View.GONE
        }
    }

    private val onQuerySearchView = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            doSearchOnClick(query)
            isLoading = true
            searchListView.visibility = View.GONE

            return false
        }

        override fun onQueryTextChange(query: String): Boolean {
            var timer = Timer()

            if (query.isEmpty()) {
                suggestionsNamesList.clear()
                searchListAdapter.notifyDataSetChanged()
                searchListView.visibility = View.GONE
            } else {
                searchListView.visibility = View.VISIBLE
                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            doSearchCompetence(query)
                        }
                    },
                    SERVER_RESPONSE_DELAY
                )
            }
            return false
        }
    }

    private fun buildCardsRecyclerView() {
        mVacancyList = ArrayList()

        mRecyclerView.setHasFixedSize(true)
        mAdapter = CardRVAdapter(mVacancyList)
        mRecyclerView.adapter = mAdapter

        mAdapter.setOnItemClickListener(object : CardRVAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(context, "onItemClick!", Toast.LENGTH_SHORT).show()
            }

            override fun onEyeClick(v: View, position: Int) {
                Toast.makeText(context, "onEyeClick!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initScrollListener() {
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mVacancyList.size - 1) {
                        //Нашли конец списка
                        loadMoreCards()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMoreCards() {
        val handler = Handler()
        handler.postDelayed({
            val nextLimit = mVacancyList.size + 10
            val nextOffset = nextLimit - 10

            buildCardsListNext(nextLimit, nextOffset)
            isLoading = false
        }, SERVER_RESPONSE_DELAY)
    }

    private fun doSearchCompetence(query: String) {
        Retrofit.api?.loadCompetence(query, SERVER_RESPONSE_MAX_COUNT)
            ?.enqueue(object : Callback<List<String>> {
                override fun onResponse(@NonNull call: Call<List<String>>, @NonNull response: Response<List<String>>) {
                    if (response.body() != null) {

                        val resultList = response.body()

                        suggestionsNamesList.clear()

                        if (response.body()!!.isEmpty()) {
                            suggestionsNamesList.add(SuggestionEntity(getString(R.string.search_view_suggestions_empty)))
                            searchListAdapter.notifyDataSetChanged()
                        }

                        for (i in 0 until response.body()!!.size) {
                            val suggestionName = SuggestionEntity(resultList!![i])
                            suggestionsNamesList.add(suggestionName)
                            searchListAdapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(@NonNull call: Call<List<String>>, @NonNull t: Throwable) {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun buildCardsList() {
        Retrofit.api?.loadVacancy()?.enqueue(object : Callback<CardVacancy> {
            override fun onResponse(@NonNull call: Call<CardVacancy>, @NonNull response: Response<CardVacancy>) {
                if (response.body() != null) {

                    val resultList: List<ResultsVacancy> = response.body()!!.results

                    for (i in 0 until resultList.size) {
                        val tmpEmploymentList: MutableList<String> = java.util.ArrayList()
                        resultList[i].employment.forEach { employment ->
                            tmpEmploymentList.add(employment.name)
                        }

                        val tmpCompetenceList: MutableList<String> = java.util.ArrayList()
                        resultList[i].competences.forEach { competences ->
                            tmpCompetenceList.add(competences.name)
                        }

                        mVacancyList.add(
                            VacancyEntity(
                                resultList[i].name,
                                resultList[i].company.name,
                                resultList[i].salary_level_newbie.toString(),
                                resultList[i].salary_level_experienced.toString(),
                                resultList[i].format_of_work.name,
                                tmpEmploymentList,
                                tmpCompetenceList
                            )
                        )
                    }
                    mAdapter.notifyDataSetChanged() // Обновить список после добавления элементов
                }
            }

            override fun onFailure(@NonNull call: Call<CardVacancy>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun buildCardsListNext(limitNext: Int, offsetNext: Int): ArrayList<VacancyEntity> {
        Retrofit.api?.loadVacancyNext(limitNext, offsetNext)?.enqueue(object : Callback<CardVacancy> {
            override fun onResponse(@NonNull call: Call<CardVacancy>, @NonNull response: Response<CardVacancy>) {
                if (response.body() != null) {

                    val resultList: List<ResultsVacancy> = response.body()!!.results

                    for (i in 0 until resultList.size) {
                        val tmpEmploymentList: MutableList<String> = java.util.ArrayList()
                        resultList[i].employment.forEach { employment ->
                            tmpEmploymentList.add(employment.name)
                        }

                        val tmpCompetenceList: MutableList<String> = java.util.ArrayList()
                        resultList[i].competences.forEach { competences ->
                            tmpCompetenceList.add(competences.name)
                        }

                        mVacancyList.add(
                            VacancyEntity(
                                resultList[i].name,
                                resultList[i].company.name,
                                resultList[i].salary_level_newbie.toString(),
                                resultList[i].salary_level_experienced.toString(),
                                resultList[i].format_of_work.name,
                                tmpEmploymentList,
                                tmpCompetenceList
                            )
                        )
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(@NonNull call: Call<CardVacancy>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
        return mVacancyList
    }

    private fun doSearchOnClick(query: String) {
        Retrofit.api?.loadVacancyByCompetence(query)?.enqueue(object : Callback<CardVacancy> {
            override fun onResponse(@NonNull call: Call<CardVacancy>, @NonNull response: Response<CardVacancy>) {
                if (response.body() != null) {

                    val resultList: List<ResultsVacancy> = response.body()!!.results

                    //Отчистить список для новых результатов
                    mVacancyList.clear()
                    mAdapter.notifyDataSetChanged()

                    for (i in 0 until resultList.size) {
                        val tmpEmploymentList: MutableList<String> = java.util.ArrayList()
                        resultList[i].employment.forEach { employment ->
                            tmpEmploymentList.add(employment.name)
                        }

                        val tmpCompetenceList: MutableList<String> = java.util.ArrayList()
                        resultList[i].competences.forEach { competences ->
                            tmpCompetenceList.add(competences.name)
                        }

                        mVacancyList.add(
                            VacancyEntity(
                                resultList[i].name,
                                resultList[i].company.name,
                                resultList[i].salary_level_newbie.toString(),
                                resultList[i].salary_level_experienced.toString(),
                                resultList[i].format_of_work.name,
                                tmpEmploymentList,
                                tmpCompetenceList
                            )
                        )
                    }
                    mAdapter.notifyDataSetChanged() // Обновить список после добавления элементов
                }
            }

            override fun onFailure(@NonNull call: Call<CardVacancy>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}