package ru.jobni.jobni.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.R
import ru.jobni.jobni.model.VacancyEntity
import ru.jobni.jobni.model.network.vacancy.*
import ru.jobni.jobni.utils.RecyclerAdapter
import ru.jobni.jobni.utils.Retrofit
import java.util.*

class FragmentMain : Fragment() {

    private lateinit var searchReal: AutoCompleteTextView

    private lateinit var progressBar: ProgressBar

    private val SERVER_RESPONSE_DELAY: Long = 1000 // 1 sec
    private val SERVER_RESPONSE_MAX_COUNT: Int = 10

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var btnList: Button

    private lateinit var expandableListAdapter: ExpandableListAdapter
    private lateinit var expandableListView: ExpandableListView
    private val headerList = ArrayList<String>()
    private val childList = HashMap<String, List<String>>()

    private lateinit var mVacancyList: ArrayList<VacancyEntity>

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        progressBar = view.findViewById(R.id.search_progress_bar) as ProgressBar
        drawerLayout = activity!!.findViewById(R.id.drawer_layout)
        bottomNavigationView = activity!!.findViewById(R.id.menu_bottom)

        btnList = view.findViewById(R.id.list)
        btnList.setOnClickListener { openRightMenu() }

        expandableListView = activity!!.findViewById(R.id.exp_list_view)

        searchReal = view.findViewById(R.id.search_view_real) as AutoCompleteTextView

        initSearch()

        mRecyclerView = view.findViewById(R.id.rv_cards) as RecyclerView
        mVacancyList = ArrayList()

        buildRecyclerView()

        return view
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.visibility = View.VISIBLE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    }

    private fun buildRecyclerView() {
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(context)
        mAdapter = RecyclerAdapter(mVacancyList)

        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter

        mAdapter.setOnItemClickListener(object : RecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(context, "onItemClick!", Toast.LENGTH_SHORT).show()
            }

            override fun onEyeClick(v: View, position: Int) {
                Toast.makeText(context, "onEyeClick!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initSearch() {
        searchReal.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            val searchItem: String = searchReal.text.toString()
            doSearchOnClick(searchItem)
        }

        searchReal.addTextChangedListener(object : TextWatcher {
            private var timer = Timer()

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            doSearchOnTextChange(s.toString())
                        }
                    },
                    SERVER_RESPONSE_DELAY
                )
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun doSearchOnTextChange(query: String) {
        Retrofit.api?.loadCompetence(query, SERVER_RESPONSE_MAX_COUNT)
            ?.enqueue(object : Callback<List<String>> {
                override fun onResponse(@NonNull call: Call<List<String>>, @NonNull response: Response<List<String>>) {
                    if (response.body() != null) {

                        val resultList = response.body()

                        val suggestions: List<String> = ArrayList()
                        (suggestions as ArrayList<String>).clear()

                        if (resultList!!.isEmpty()) {
                            suggestions.add(getString(R.string.search_view_suggestions_empty))
                        }

                        for (i in 0 until response.body()!!.size) {
                            suggestions.add(resultList[i])
                        }
                        val adapter = ArrayAdapter<String>(context!!, R.layout.c_search_item, R.id.item, suggestions)
                        searchReal.setAdapter<ArrayAdapter<String>>(adapter)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(@NonNull call: Call<List<String>>, @NonNull t: Throwable) {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun prepareListData() {
        val top250 = ArrayList<String>()
        top250.add("The Shawshank Redemption")
        top250.add("The Godfather")
        top250.add("The Godfather: Part II")
        top250.add("Pulp Fiction")
        top250.add("The Good, the Bad and the Ugly")
        top250.add("The Dark Knight")
        top250.add("12 Angry Men")

        headerList.forEach { str ->
            // java ver. childList.put(str, top250)
            childList[str] = top250
        }
    }

    private fun openRightMenu() {
        if (headerList.isEmpty()){
            Retrofit.api?.loadDetailVacancy()?.enqueue(object : Callback<DetailVacancy> {
                override fun onResponse(@NonNull call: Call<DetailVacancy>, @NonNull response: Response<DetailVacancy>) {
                    if (response.body() != null) {
                        val (competence, languages, work_places, employment, format_of_work, field_of_activity, age_company, required_number_of_people, zarplata, social_packet, auto, raiting) = response.body()!!

                        val detailList: MutableList<Any> = mutableListOf(
                            competence,
                            languages,
                            work_places,
                            employment,
                            format_of_work,
                            field_of_activity,
                            age_company,
                            required_number_of_people,
                            zarplata,
                            social_packet,
                            auto,
                            raiting
                        )
                        detailList.forEach { str: Any ->
                            if (str is String) headerList.add(str)
                            else when (str) {
                                is Zarplata -> headerList.add("Зарплата")
                                is Social_packet -> headerList.add("Социальный пакет")
                                is Auto -> headerList.add("Авто")
                                is Raiting -> headerList.add("Рейтинг")
                            }
                        }

                        prepareListData()

                        expandableListAdapter = ExpandableListAdapter(activity as Context, headerList, childList)
                        expandableListView.setAdapter(expandableListAdapter)
                    }
                }

                override fun onFailure(@NonNull call: Call<DetailVacancy>, @NonNull t: Throwable) {
                    Toast.makeText(context, "Error in download for menu!", Toast.LENGTH_LONG).show()
                }
            })
        }

        drawerLayout.openDrawer(GravityCompat.END)
        //ниже закрываем клавиатуру если открыта
        val view = activity!!.currentFocus
        view?.let { v ->
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
        }
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
                    mAdapter.notifyDataSetChanged() // Обновить список после добавления элемента

                }
            }

            override fun onFailure(@NonNull call: Call<CardVacancy>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun doSearchOnClick(query: String) {
        Retrofit.api?.loadVacancyByCompetence(query)?.enqueue(object : Callback<CardVacancy> {
            override fun onResponse(@NonNull call: Call<CardVacancy>, @NonNull response: Response<CardVacancy>) {
                if (response.body() != null) {

                    val resultList: List<ResultsVacancy> = response.body()!!.results

                    //Отчистить список для новых результатов
                    mVacancyList.clear()
                    mAdapter.notifyDataSetChanged()

                    if (resultList.isEmpty()) {
                        Toast.makeText(context, R.string.search_response_empty, Toast.LENGTH_SHORT).show()
                    }

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
                    mAdapter.notifyDataSetChanged() // Обновить список после добавления элемента
                }
            }

            override fun onFailure(@NonNull call: Call<CardVacancy>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}