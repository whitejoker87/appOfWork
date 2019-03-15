package ru.jobni.jobni.viewmodel

import android.app.Application
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.R
import ru.jobni.jobni.model.RepositoryVacancyEntity
import ru.jobni.jobni.model.SuggestionEntity
import ru.jobni.jobni.model.VacancyEntity
import ru.jobni.jobni.model.network.vacancy.*
import ru.jobni.jobni.utils.Retrofit
import java.util.*
import kotlin.collections.ArrayList


class MainViewModel(application: Application) : AndroidViewModel(application) {

    val SERVER_RESPONSE_DELAY: Long = 1000 // 1 sec
    private val SERVER_RESPONSE_MAX_COUNT: Int = 10
    private val firstLaunchFlag = "firstLaunch"

    var sPref = application.getSharedPreferences("firstLaunchSavedData", AppCompatActivity.MODE_PRIVATE)

    private val suggestionsNamesList = MutableLiveData<ArrayList<SuggestionEntity>>(ArrayList())

    var isLoad = true

    private lateinit var bodyResponse: DetailVacancy
    private val headerList = MutableLiveData<MutableList<String>>()
    private val childList = MutableLiveData<HashMap<String, List<String>>>()
    private val isOpenDrawer = MutableLiveData<Boolean>()
    private val fragmentLaunch = MutableLiveData<String>()
    private val searchQuery = MutableLiveData<String>()

    private val isSearchViewVisible = MutableLiveData<Boolean>(false)
    private val isBottomNavigationViewVisible = MutableLiveData<Boolean>(false)
    private val isDrawerRightLocked = MutableLiveData<Boolean>(true)
    private val isToolbarVisible = MutableLiveData<Boolean>(false)
    private val isSearchListViewVisible = MutableLiveData<Boolean>(false)

    val context = application

    private val modelVacancy : MutableLiveData<MainFragmentViewState> = MutableLiveData()
    private val repository: RepositoryVacancyEntity = RepositoryVacancyEntity

    init {
        repository.getVacancy().observeForever { vacancies ->
            modelVacancy.value = modelVacancy.value?.copy(vacancyList = vacancies!!) ?: MainFragmentViewState(vacancies!!)
        }
    }

    fun getModelVacancy(): LiveData<MainFragmentViewState> = modelVacancy

    fun getHeaderList(): MutableLiveData<MutableList<String>> = headerList

    fun getChildList(): MutableLiveData<HashMap<String, List<String>>> = childList

    fun setOpenDrawer(isOpen: Boolean) {
        isOpenDrawer.value = isOpen
    }

    fun isOpenDrawer(): MutableLiveData<Boolean> = isOpenDrawer

    fun setFragmentLaunch(fragmentType: String) {
        fragmentLaunch.value = fragmentType
    }

    fun getFragmentLaunch(): MutableLiveData<String> = fragmentLaunch

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun getSearchQuery(): MutableLiveData<String> = searchQuery

    fun setSearchViewVisible(isVisible: Boolean) {
        isSearchViewVisible.value = isVisible
    }

    fun isSearchViewVisible(): MutableLiveData<Boolean> = isSearchViewVisible

    fun setBottomNavigationViewVisible(isVisible: Boolean) {
        isBottomNavigationViewVisible.value = isVisible
    }

    fun isBottomNavigationViewVisible(): MutableLiveData<Boolean> = isBottomNavigationViewVisible

    fun setDrawerRightLocked(isVisible: Boolean) {
        isDrawerRightLocked.value = isVisible
    }

    fun isDrawerRightLocked(): MutableLiveData<Boolean> = isDrawerRightLocked

    fun setToolbarVisible(isVisible: Boolean) {
        isToolbarVisible.value = isVisible
    }

    fun isToolbarVisible(): MutableLiveData<Boolean> = isToolbarVisible

    fun setSearchListViewVisible(isVisible: Boolean) {
        isSearchListViewVisible.value = isVisible
    }

    fun isSearchListViewVisible(): MutableLiveData<Boolean> = isSearchListViewVisible

    fun setSuggestionsNamesList(suggestionsNames: ArrayList<SuggestionEntity>) {
        suggestionsNamesList.value = suggestionsNames
    }

    fun getSuggestionsNamesList(): MutableLiveData<ArrayList<SuggestionEntity>> = suggestionsNamesList



    private val users: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>().also {
            //loadUsers()
        }
    }

    fun getUsers(): LiveData<List<String>> {
        return users
    }

//    private fun loadUsers(): List<String> {
//        // Do an asynchronous operation to fetch users.
//    }





    fun openRightMenu() {
        if (headerList.value == null) {
            Retrofit.api?.loadDetailVacancy()?.enqueue(object : Callback<DetailVacancy> {
                override fun onResponse(@NonNull call: Call<DetailVacancy>, @NonNull response: Response<DetailVacancy>) {
                    if (response.body() != null) {
                        bodyResponse = response.body()!!
                        loadHeaderList()
                    }
                }

                override fun onFailure(@NonNull call: Call<DetailVacancy>, @NonNull t: Throwable) {
                    //Toast.makeText(applicationContext, "Error in download for menu!", Toast.LENGTH_LONG).show()
                }
            })
        }

        setOpenDrawer(true)
//        drawer.openDrawer(GravityCompat.END)
//        //ниже закрываем клавиатуру если открыта
//        val view = this.currentFocus
//        view?.let { v ->
//            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
//        }
    }

    fun loadChildList(headers: MutableList<String>) {
        val top250 = ArrayList<String>()
        val childs = HashMap<String, List<String>>()
        top250.add("The Shawshank Redemption")
        top250.add("The Godfather")
        top250.add("The Godfather: Part II")
        top250.add("Pulp Fiction")
        top250.add("The Good, the Bad and the Ugly")
        top250.add("The Dark Knight")
        top250.add("12 Angry Men")

        headers.forEach { str ->
            // java ver. childList.put(str, top250)
            childs[str] = top250
        }
        childList.value = childs
    }

    fun onClickBtnStart(typeFragment:String) {
        if (typeFragment.equals("Intro"))saveLaunchFlag()
        setFragmentLaunch(typeFragment)
    }

    fun saveLaunchFlag() {
        val editor = sPref.edit()
        editor?.putBoolean(firstLaunchFlag, false)
        editor?.apply()
    }

    fun saveLaunchFlag(reset: Boolean) {//для отладки первого запуска
        val editor = sPref.edit()
        editor.putBoolean(firstLaunchFlag, reset)
        editor.apply()
    }

    private fun loadHeaderList() {
        val headers = ArrayList<String>()
        val (competence,
            languages,
            work_places,
            employment,
            format_of_work,
            field_of_activity,
            age_company,
            required_number_of_people,
            zarplata, social_packet,
            auto,
            raiting) = bodyResponse

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
            if (str is String) headers.add(str)
            else when (str) {
                is Zarplata -> headers.add("Зарплата")
                is Social_packet -> headers.add("Социальный пакет")
                is Auto -> headers.add("Авто")
                is Raiting -> headers.add("Рейтинг")
            }
        }
        headerList.value = headers
    }

    private val toolbarTitle = MutableLiveData<String>()

    fun getToolbarTitle(): String? {
        return toolbarTitle.value
    }

    fun setToolbarTitle(toolbarTitle: String) {
        this.toolbarTitle.setValue(toolbarTitle)
    }



    //todo доделать!!!
   val onNavigationClick = object : BottomNavigationView.OnNavigationItemSelectedListener{
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.getItemId()) {
                ru.jobni.jobni.R.id.bottom_menu_search -> {
                    //setFragment(FragmentMain())
                    return true
                }
                ru.jobni.jobni.R.id.bottom_menu_notification -> {
                    return true
                }
                ru.jobni.jobni.R.id.bottom_menu_chat -> {
                    setFragmentLaunch("Welcome")
                    return true
                }
                ru.jobni.jobni.R.id.bottom_menu_profile -> {
                    //popup.show()
                    return true
                }
            }
            return false
        }
    }

    val onScrollViewRecycler = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val cardLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (isLoad) {
                    if (cardLayoutManager.findLastCompletelyVisibleItemPosition() == repository.getSize() - 1) {
                        //Нашли конец списка
                        loadMoreCards()
                        isLoad = false
                    }
                }
            }
        }

    val onQuerySearchView = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            doSearchOnClick(query)
            isLoad = false
            setSearchListViewVisible(false)

            return false
        }

        override fun onQueryTextChange(query: String): Boolean {
            var timer = Timer()

            if (query.isEmpty()) {
                getSuggestionsNamesList().clear()
//                searchListAdapter.notifyDataSetChanged() // обновляется благордаря колбеку в фрагменте
                setSearchListViewVisible(false)
            } else {
                setSearchListViewVisible(true)
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

//    val onItemClickListener = object : AdapterView.OnItemClickListener {
//        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            doSearchOnClick(suggestionsNamesList.value!![position].suggestionName)
//            setSearchQuery(suggestionsNamesList.value!![position].suggestionName)
//            setSearchListViewVisible(false)
//        }
//
//    }

    fun switchToMainActivity() {
        val duration = 2000L

        /*
        // Вариант с блокировкой экрана (использование одного потока)
        Thread.sleep(duration)
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, FragmentMain())
                ?.commit()
        */
        if (sPref.getBoolean(firstLaunchFlag, true))
            Handler().postDelayed({
                setFragmentLaunch("Intro")
            }, duration)
        else
            Handler().postDelayed({
                setFragmentLaunch("Main")
            }, duration)


    }

    fun onSuggestionsListItemClick(position: Int) {
        doSearchOnClick(suggestionsNamesList.value!![position].suggestionName)
        setSearchQuery(suggestionsNamesList.value!![position].suggestionName)
        setSearchListViewVisible(false)
    }

    fun loadMoreCards() {
        val handler = Handler()
        handler.postDelayed({
            val nextLimit = repository.getSize() + 10
            val nextOffset = nextLimit - 10

            buildCardsList(nextLimit, nextOffset)
            isLoad = true
        }, SERVER_RESPONSE_DELAY)
    }

    fun onCardRVVacancyClick(position: Int) {
        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
    }

    fun doSearchOnClick(query: String) {
        Retrofit.api?.loadVacancyByCompetence(query)?.enqueue(object : Callback<CardVacancy> {
            override fun onResponse(@NonNull call: Call<CardVacancy>, @NonNull response: Response<CardVacancy>) {
                if (response.body() != null) {

                    val resultList: List<ResultsVacancy> = response.body()!!.results

                    // Отчистить список для новых результатов
                    repository.clearRepository()

                    for (i in 0 until resultList.size) {
                        val tmpEmploymentList: MutableList<String> = java.util.ArrayList()
                        resultList[i].employment.forEach { employment ->
                            tmpEmploymentList.add(employment.name)
                        }

                        val tmpCompetenceList: MutableList<String> = java.util.ArrayList()
                        resultList[i].competences.forEach { competences ->
                            tmpCompetenceList.add(competences.name)
                        }

                        repository.saveVacancy(
                            VacancyEntity(
                                resultList[i].id,
                                resultList[i].name,
                                resultList[i].company.name,
                                resultList[i].salary_level_newbie.toString(),
                                resultList[i].salary_level_experienced.toString(),
                                resultList[i].format_of_work.name,
                                tmpEmploymentList,
                                tmpCompetenceList,
                                "",
                                "",
                                "",
                                ""
                            )
                        )
                    }
                }
            }

            override fun onFailure(@NonNull call: Call<CardVacancy>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun buildCardsList(limitNext: Int, offsetNext: Int){
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

                        repository.saveVacancy(
                            VacancyEntity(
                                resultList[i].id,
                                resultList[i].name,
                                resultList[i].company.name,
                                resultList[i].salary_level_newbie.toString(),
                                resultList[i].salary_level_experienced.toString(),
                                resultList[i].format_of_work.name,
                                tmpEmploymentList,
                                tmpCompetenceList,
                                "",
                                "",
                                "",
                                ""
                            )
                        )
                    }
                }
            }

            override fun onFailure(@NonNull call: Call<CardVacancy>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun doSearchCompetence(query: String) {
        Retrofit.api?.loadCompetence(query, SERVER_RESPONSE_MAX_COUNT)
            ?.enqueue(object : Callback<List<String>> {
                override fun onResponse(@NonNull call: Call<List<String>>, @NonNull response: Response<List<String>>) {
                    if (response.body() != null) {

                        val resultList = response.body()

                        suggestionsNamesList.clear()

                        if (response.body()!!.isEmpty()) {
                            suggestionsNamesList.add(SuggestionEntity("Нет совпадений"))
                        }

                        for (i in 0 until response.body()!!.size) {
                            val suggestionName = SuggestionEntity(resultList!![i])
                            suggestionsNamesList.add(suggestionName)
                        }
                    }
                }

                override fun onFailure(@NonNull call: Call<List<String>>, @NonNull t: Throwable) {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                }
            })
    }

}
//функция для оповещения наблюдателей после добавления элеента в спискоо(обычно нужно список перезаписать)
fun <T> MutableLiveData<ArrayList<T>>.add(item: T) {
    val updatedItems = this.value as ArrayList
    updatedItems.add(item)
    this.value = updatedItems
}
//аналогичная для очистки
fun <T> MutableLiveData<ArrayList<T>>.clear() {
    val updatedItems = this.value as ArrayList
    updatedItems.clear()
    this.value = updatedItems
}

