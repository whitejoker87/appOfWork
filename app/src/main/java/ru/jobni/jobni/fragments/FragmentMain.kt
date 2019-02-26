package ru.jobni.jobni.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ExpandableListAdapter
import androidx.annotation.NonNull
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.R
import ru.jobni.jobni.model.*
import ru.jobni.jobni.model.network.vacancy.ResultsVacancy
import ru.jobni.jobni.model.network.vacancy.VacancyRequest
import ru.jobni.jobni.utils.RecyclerAdapter
import ru.jobni.jobni.utils.Retrofit
import java.util.*

class FragmentMain : Fragment() {

    private lateinit var searchReal: AutoCompleteTextView
    private lateinit var searchFake: SearchView

    private lateinit var button: Button
    private lateinit var progressBar: ProgressBar

    private val SERVER_RESPONSE_DELAY: Long = 1000 // 1 sec
    private val SERVER_RESPONSE_MAX_COUNT: Int = 10

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnList: Button

    private lateinit var expandableListAdapter: ExpandableListAdapter
    private lateinit var expandableListView: ExpandableListView
    private val  headerList = ArrayList<String>()
    private val childList = HashMap<String, List<String>>()

    private lateinit var mVacancyList: ArrayList<VacancyEntity>

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        //val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        //(activity as AppCompatActivity).setSupportActionBar(toolbar)
        //toolbar.title = ""

        progressBar = view.findViewById(R.id.search_progress_bar) as ProgressBar
        drawerLayout = view.findViewById(R.id.drawer_layout)

        btnList = view.findViewById(R.id.list)
        btnList.setOnClickListener { openRightMenu() }

        expandableListView = view.findViewById(R.id.exp_list_view)

        val navigationView: NavigationView = view.findViewById(R.id.navigation_view);

        searchFake = view.findViewById(R.id.search_view_fake) as SearchView
        button = view.findViewById(R.id.search_work) as Button
        val fakeLayout = view.findViewById(R.id.constraint_layout_fake) as ConstraintLayout

        searchReal = view.findViewById(R.id.search_view_real) as AutoCompleteTextView

        initSearchFake(view, fakeLayout)
        initSearch()

        mRecyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        mVacancyList = ArrayList()

        buildRecyclerView()

        return view
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
        searchReal.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(arg0: AdapterView<*>, arg1: View, arg2: Int, arg3: Long) {
                Toast.makeText(context, "onSuggestionSelect", Toast.LENGTH_SHORT).show()
            }
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

    fun showProgressBar(searchView: AutoCompleteTextView) {
        val id = searchView.context.resources.getIdentifier("android:id/search_plate", null, null)
        progressBar.animate().setDuration(200).alpha(1f).start()
    }

    fun hideProgressBar(searchView: AutoCompleteTextView) {
        val id = searchView.context.resources.getIdentifier("android:id/search_plate", null, null)
        progressBar.animate().setDuration(200).alpha(0f).start()
    }

    private fun initSearchFake(view: View, fakeLayout: ConstraintLayout) {
        button.setOnClickListener {
            TransitionManager.beginDelayedTransition(constraint_layout_fake)
            fakeLayout.visibility = View.GONE
            doBtnOnClick()
        }

        searchFake.setOnClickListener {
            TransitionManager.beginDelayedTransition(constraint_layout_fake)
            fakeLayout.visibility = View.GONE
        }

        searchFake.setOnSearchClickListener {
            TransitionManager.beginDelayedTransition(constraint_layout_fake)
            fakeLayout.visibility = View.GONE
        }

        searchFake.setOnQueryTextFocusChangeListener { _: View, _: Boolean ->
            TransitionManager.beginDelayedTransition(constraint_layout_fake)
            fakeLayout.visibility = View.GONE
        }
    }

    private fun doSearchOnTextChange(query: String) {
        Retrofit.api?.loadCompetence(query, SERVER_RESPONSE_MAX_COUNT)
            ?.enqueue(object : Callback<List<String>> {
                override fun onResponse(@NonNull call: Call<List<String>>, @NonNull response: Response<List<String>>) {
                    if (response.body() != null) {

                        val resultList = response.body()

                        val suggestions: List<String> = ArrayList()
                        (suggestions as ArrayList<String>).clear()

                        if (response.body()!!.isEmpty()) {
                            suggestions.add(getString(R.string.search_view_suggestions_empty))
                        } else {
                            for (i in 0 until response.body()!!.size) {
                                suggestions.add(resultList!![i])
                            }
                            val adapter = ArrayAdapter<String>(context!!, R.layout.search_item, R.id.item, suggestions)
                            searchReal.setAdapter<ArrayAdapter<String>>(adapter)
                            adapter.notifyDataSetChanged()
                        }
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

        headerList.forEach {  str ->
            childList.put(str, top250)
        }
    }

    fun openRightMenu() {
        Retrofit.api?.loadDetailVacancy()?.enqueue(object : Callback<DetailVacancy> {
            override fun onResponse(@NonNull call: Call<DetailVacancy>, @NonNull response: Response<DetailVacancy>) {
                if (response.body() != null) {
                    val(competence,languages,work_places,employment,format_of_work,field_of_activity,age_company,required_number_of_people, zarplata, social_packet,auto,raiting) = response.body()!!
//                      Вариант выдвчи инфы о полях класса
//                    DetailVacancy::class.memberProperties.forEach { member ->
//                        val name = member.name
//                        val value = member.get(instance) as String
//
//                        findTextViewByName(name).text = value
//                    }
                    val detailList: MutableList<Any> = mutableListOf(competence,languages,work_places,employment,format_of_work,field_of_activity,age_company,required_number_of_people, zarplata, social_packet,auto,raiting)
                    detailList.forEach { str:Any ->
                            if (str is String)headerList.add(str)
                            else when(str) {
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
        drawerLayout.openDrawer(GravityCompat.END)
    }

    private fun doBtnOnClick() {
        Retrofit.api?.loadVacancy()
            ?.enqueue(object : Callback<VacancyRequest> {
                override fun onResponse(@NonNull call: Call<VacancyRequest>, @NonNull response: Response<VacancyRequest>) {
                    if (response.body() != null) {

                        val list: List<ResultsVacancy> = response.body()!!.results

                        val position = 0
                        for (i in 0 until list.size) {
                            val tmpEmploymentList: MutableList<String> = java.util.ArrayList()
                            list[i].employment.forEach { employment ->
                                tmpEmploymentList.add(employment.name)
                            }

                            val tmpCompetenceList: MutableList<String> = java.util.ArrayList()
                            list[i].competences.forEach { competences ->
                                tmpCompetenceList.add(competences.name)
                            }

                            mVacancyList.add(
                                position,
                                VacancyEntity(
                                    list[i].name,
                                    list[i].company.name,
                                    list[i].salary_level_newbie.toString(),
                                    list[i].salary_level_experienced.toString(),
                                    list[i].format_of_work.name,
                                    tmpEmploymentList,
                                    tmpCompetenceList
                                )
                            )
                        }
                        mAdapter.notifyItemInserted(position) // Обновить список после добавления элемента
                    }
                }

                override fun onFailure(@NonNull call: Call<VacancyRequest>, @NonNull t: Throwable) {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                }
            })
    }
}