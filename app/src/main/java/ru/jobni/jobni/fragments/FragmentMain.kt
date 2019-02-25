package ru.jobni.jobni.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Button
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.transition.TransitionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_main.*
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.R
import ru.jobni.jobni.utils.Retrofit
import ru.jobni.jobni.model.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class FragmentMain : Fragment() {

    private lateinit var searchReal: AutoCompleteTextView
    private lateinit var searchFake: SearchView

    private lateinit var button: Button
    private lateinit var progressBar: ProgressBar

    private val SERVER_RESPONSE_DELAY: Long = 1000 // 1 sec
    private val SERVER_RESPONSE_MAX_COUNT: Int = 10

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var btnList: Button

    private lateinit var expandableListAdapter: ExpandableListAdapter
    private lateinit var expandableListView: ExpandableListView
    private val  headerList = ArrayList<String>()
    private val childList = HashMap<String, List<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        progressBar = view.findViewById(R.id.search_progress_bar) as ProgressBar
        drawerLayout = activity!!.findViewById(R.id.drawer_layout)
        bottomNavigationView = activity!!.findViewById(R.id.menu_bottom)

        btnList = view.findViewById(R.id.list)
        btnList.setOnClickListener { openRightMenu() }

        expandableListView = activity!!.findViewById(R.id.exp_list_view)

        searchFake = view.findViewById(R.id.search_view_fake) as SearchView
        button = view.findViewById(R.id.search_work) as Button
        val fakeLayout = view.findViewById(R.id.constraint_layout_fake) as ConstraintLayout

        searchReal = view.findViewById(R.id.search_view_real) as AutoCompleteTextView

        initSearchFake(view, fakeLayout)
        initSearch()

        return view
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.visibility = View.VISIBLE
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
        }

        searchFake.setOnClickListener {
            TransitionManager.beginDelayedTransition(constraint_layout_fake)
            fakeLayout.visibility = View.GONE
        }

        searchFake.setOnSearchClickListener {
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
                    val(competence, languages, work_places, employment, format_of_work, field_of_activity,
                        age_company, required_number_of_people, zarplata, social_packet, auto,raiting) = response.body()!!
                    val detailList: MutableList<Any> = mutableListOf(competence,
                        languages,work_places,employment,format_of_work,field_of_activity,
                        age_company,required_number_of_people, zarplata, social_packet,auto,raiting)
                    detailList.forEach { str:Any ->
                            if (str is String)headerList.add(str)
                            else when(str) {
                                is Zarplata -> headerList.add("Зарплата")
                                is Social_packet -> headerList.add("Социальный пакет")
                                is Auto -> headerList.add("Авто")
                                is Raiting -> headerList.add("Рейтинг")
                            }
                    }

                    prepareListData()//Заглушка для второго уровня правого меню

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
}