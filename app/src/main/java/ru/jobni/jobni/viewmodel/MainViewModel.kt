package ru.jobni.jobni.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.BuildConfig
import ru.jobni.jobni.R
import ru.jobni.jobni.model.RepositoryCompanyVacancy
import ru.jobni.jobni.model.RepositoryVacancy
import ru.jobni.jobni.model.SuggestionEntity
import ru.jobni.jobni.model.VacancyEntity
import ru.jobni.jobni.model.menu.left.RepositoryOwner
import ru.jobni.jobni.model.network.company.CompanyVacancy
import ru.jobni.jobni.model.network.company.ResultsCompany
import ru.jobni.jobni.model.network.vacancy.*
import ru.jobni.jobni.utils.Retrofit
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainViewModel(application: Application) : AndroidViewModel(application) {

    val SERVER_RESPONSE_DELAY: Long = 1000 // 1 sec
    private val SERVER_RESPONSE_MAX_COUNT: Int = 10
    private val firstLaunchFlag = "firstLaunch"
    private val authUserSessionID = "userSessionID"

    var sPref = application.getSharedPreferences("firstLaunchSavedData", AppCompatActivity.MODE_PRIVATE)
    var sPrefAuthUser = application.getSharedPreferences("authUser", AppCompatActivity.MODE_PRIVATE)

    private val suggestionsNamesList = MutableLiveData<ArrayList<SuggestionEntity>>(ArrayList())

    var isLoad = true

    /*список в первом уровне правог меню*/
    private val headerList = MutableLiveData<MutableList<Any>>()
    /*список второго уровня правого меню*/
    private val childList = MutableLiveData<HashMap<String, List<String>>>()
    /*флаг открытого правого меню*/
    private val isOpenDrawerRight = MutableLiveData<Boolean>()
    /*флаг открытого левого меню*/
    private val isOpenDrawerLeft = MutableLiveData<Boolean>()
    /*параметр запуска фрагмента(для навигации)*/
    private val fragmentLaunch = MutableLiveData<String>()
    /*параметр строки поиска(для наблюдения)*/
    private val searchQuery = MutableLiveData<String>()
    /*параментр видимости поля поиска*/
    private val isSearchViewVisible = MutableLiveData<Boolean>(false)
    /*параментр видимости нижнего меню*/
    private val isBottomNavigationViewVisible = MutableLiveData<Boolean>(false)
    /*параметр блокировки правого меню*/
    private val isDrawerRightLocked = MutableLiveData<Boolean>(true)
    /*парамент видимрости тулбара*/
    private val isToolbarVisible = MutableLiveData<Boolean>(false)
    /*параметр видимости спика подсказок в строке поиска*/
    private val isSearchListViewVisible = MutableLiveData<Boolean>(false)
    /*параметр запуска окна привязки соцсетей при регистрации*/
    private val socialLaunch = MutableLiveData<String>()

    // Позиция карточки для открытия в отдельном фрагменте
    var vacancyPosition = 0

    val context = application

    /*параметр uri для загружаемого фото*/
    private var outputPhotoUri: MutableLiveData<Uri> = MutableLiveData(Uri.EMPTY)
    /*параментр для запука активити(для фото)*/
    private val activityLaunch: MutableLiveData<Intent> = MutableLiveData()
    /*путь до файла с фото*/
    private var mCurrentPhotoPath: String? = ""
    /*флаг для определения откуда используется инклюд с кнопками соцсетей(из авторизации или регистрации)*/
    private val isIncludeSocialNetworkReg: MutableLiveData<Boolean> = MutableLiveData(false)

    private val modelVacancy: MutableLiveData<MainFragmentViewState> = MutableLiveData()
    private val repositoryVacancy: RepositoryVacancy = RepositoryVacancy

    private val modelOwner: MutableLiveData<OwnerViewState> = MutableLiveData()
    private val repositoryOwner: RepositoryOwner = RepositoryOwner

    private val modelCompanyVacancy: MutableLiveData<CompanyVacancyViewState> = MutableLiveData()
    private val repositoryCompanyVacancy: RepositoryCompanyVacancy = RepositoryCompanyVacancy

    init {
        repositoryVacancy.getVacancy().observeForever { vacancies ->
            modelVacancy.value = modelVacancy.value?.copy(vacancyList = vacancies!!)
                    ?: MainFragmentViewState(vacancies!!)
        }

        repositoryOwner.getCompany().observeForever { receiveCompanyList ->
            modelOwner.value = modelOwner.value?.copy(companyList = receiveCompanyList!!)
                    ?: OwnerViewState(receiveCompanyList!!)
        }

        repositoryCompanyVacancy.getCompanyVacancy().observeForever { receiveCompanyVacancyList ->
            modelCompanyVacancy.value =
                    modelCompanyVacancy.value?.copy(companyVacancyList = receiveCompanyVacancyList!!)
                            ?: CompanyVacancyViewState(receiveCompanyVacancyList!!)
        }
    }

    fun getModelVacancy(): LiveData<MainFragmentViewState> = modelVacancy

    fun getModelOwner(): LiveData<OwnerViewState> = modelOwner

    fun getModelCompanyVacancy(): LiveData<CompanyVacancyViewState> = modelCompanyVacancy


    fun setHeaderList(list: MutableList<Any>) {
        headerList.value = list
    }

    fun getHeaderList(): MutableLiveData<MutableList<Any>> = headerList


    private val isNoAuthRegVisible = MutableLiveData<Boolean>()

    fun setNoAuthRegVisible(isVisible: Boolean) {
        isNoAuthRegVisible.value = isVisible
    }

    fun isNoAuthRegVisible(): MutableLiveData<Boolean> = isNoAuthRegVisible


    private val isYesAuthRegVisible = MutableLiveData<Boolean>()

    fun setYesAuthRegVisible(isVisible: Boolean) {
        isYesAuthRegVisible.value = isVisible
    }

    fun isYesAuthRegVisible(): MutableLiveData<Boolean> = isYesAuthRegVisible


    fun setOpenDrawerLeft(isOpen: Boolean) {
        isOpenDrawerLeft.value = isOpen
    }

    fun isOpenDrawerLeft(): MutableLiveData<Boolean> = isOpenDrawerLeft


    fun setOpenDrawerRight(isOpen: Boolean) {
        isOpenDrawerRight.value = isOpen
    }

    fun isOpenDrawerRight(): MutableLiveData<Boolean> = isOpenDrawerRight


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


    fun setSocialLaunch(launchType: String) {
        socialLaunch.value = launchType
    }

    fun getSocialLaunch(): MutableLiveData<String> = socialLaunch


    fun setSuggestionsNamesList(suggestionsNames: ArrayList<SuggestionEntity>) {
        suggestionsNamesList.value = suggestionsNames
    }

    fun getSuggestionsNamesList(): MutableLiveData<ArrayList<SuggestionEntity>> = suggestionsNamesList

    fun setOutputPhotoUri(setUri: Uri) {
        outputPhotoUri.value = setUri
    }

    fun getOutputPhotoUri(): MutableLiveData<Uri> = outputPhotoUri

    fun setActivityLaunch(cameraIntent: Intent) {
        activityLaunch.value = cameraIntent
    }

    fun getActivityLaunch(): MutableLiveData<Intent> = activityLaunch

    fun setCurrentPhotoPath(mCurrentPhotoPath: String) {
        this.mCurrentPhotoPath = mCurrentPhotoPath
    }

    fun getCurrentPhotoPath(): String? = mCurrentPhotoPath


    fun setIncludeSocialNetworkReg(isReg: Boolean) {
        isIncludeSocialNetworkReg.value = isReg
    }

    fun isIncludeSocialNetworkReg(): MutableLiveData<Boolean> = isIncludeSocialNetworkReg


    private val isCardExpandResponse = MutableLiveData<Boolean>(true)

    fun setCardExpandResponse(authKey: Boolean) {
        isCardExpandResponse.value = authKey
    }

    fun isCardExpandResponse(): MutableLiveData<Boolean> = isCardExpandResponse


    private val isLoadCardVisible = MutableLiveData<Boolean>()

    fun setLoadCardVisible(isVisible: Boolean) {
        isLoadCardVisible.value = isVisible
    }

    fun isLoadCardVisible(): MutableLiveData<Boolean> = isLoadCardVisible


    private val isLoadCardFailVisible = MutableLiveData<Boolean>()

    fun setLoadCardFailVisible(isVisible: Boolean) {
        isLoadCardFailVisible.value = isVisible
    }

    fun isLoadCardFailVisible(): MutableLiveData<Boolean> = isLoadCardFailVisible


    /*открытие левого меню*/
    fun openLeftMenu() {
        setOpenDrawerLeft(true)
    }

    /*открытие правого меню*/
    fun loadLeftMenuOwnerData() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val sessionID = String.format("%s%s", "sessionid=", id)

        Retrofit.api?.ownerOrWorker(sessionID)?.enqueue(object : Callback<CompanyVacancy> {
            override fun onResponse(@NonNull call: Call<CompanyVacancy>, @NonNull response: Response<CompanyVacancy>) {
                if (response.body() != null) {

                    val resultList: ArrayList<ResultsCompany> = response.body()!!.results
                    repositoryOwner.saveCompanyList(resultList)
                }
            }

            override fun onFailure(@NonNull call: Call<CompanyVacancy>, @NonNull t: Throwable) {}
        })
    }

    /*Баланс для левого меню*/
    fun loadLeftMenuOwnerDataBalance(position: Int) {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val sessionID = String.format("%s%s", "sessionid=", id)

        val companyID: Int = repositoryOwner.companyLiveData.value!![position].id

        Retrofit.api?.ownerOrWorkerBalance(sessionID, companyID)?.enqueue(object : Callback<Int> {
            override fun onResponse(@NonNull call: Call<Int>, @NonNull response: Response<Int>) {
                if (response.code() == 401 || response.code() == 200) {

                }

                if (response.body() != null) {

                    val resultList: Int = response.body()!!
                    repositoryOwner.saveCompanyBalance(resultList)
                }
            }

            override fun onFailure(@NonNull call: Call<Int>, @NonNull t: Throwable) {
            }
        })
    }

    fun loadLeftMenuOwnerCompanyVacancy(companyID: Int) {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val sessionID = String.format("%s%s", "sessionid=", id)

        Retrofit.api?.ownerOrWorkerCompany(sessionID, companyID)?.enqueue(object : Callback<CardVacancy> {
            override fun onResponse(@NonNull call: Call<CardVacancy>, @NonNull response: Response<CardVacancy>) {
                if (response.code() == 401 || response.code() == 200) {

                }

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

                        repositoryCompanyVacancy.saveCompanyVacancy(
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
            }
        })
    }

    fun openRightMenu() {
        if (getHeaderList().value == null) loadRightMenuData()
        setOpenDrawerRight(true)
    }


    /*нажатие на кнопку в вьюпаджере первого запуска*/
    fun onClickBtnStart(typeFragment: String) {
        saveLaunchFlag()
        setFragmentLaunch(typeFragment)
    }

    /*флаг первого запуска*/
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

    /*Загрузка правого меню*/
    fun loadRightMenuData() {
        if (getHeaderList().value == null) {
            Retrofit.api?.loadDetailVacancy()?.enqueue(object : Callback<DetailVacancy> {
                override fun onResponse(@NonNull call: Call<DetailVacancy>, @NonNull response: Response<DetailVacancy>) {
                    if (response.body() != null) {
                        val (language_and_level_of_proficiency,
                                work_places,
                                employment,
                                format_of_work,
                                field_of_activity,
                                age,
                                required_number_of_people,
                                update_time,
                                presence_geography,
                                profession_and_competence,
                                desired_salary_level,
                                social_packet,
                                more,
                                auto,
                                raiting,
                                views_responses_invitations_int) = response.body()!!

                        val detailList: MutableList<Any> = mutableListOf(
                                language_and_level_of_proficiency,
                                work_places,
                                employment,
                                format_of_work,
                                field_of_activity,
                                age,
                                required_number_of_people,
                                update_time,
                                presence_geography,
                                profession_and_competence,
                                desired_salary_level,
                                social_packet,
                                more,
                                auto,
                                raiting,
                                views_responses_invitations_int
                        )
                        setHeaderList(detailList)
                    }
                }

                override fun onFailure(@NonNull call: Call<DetailVacancy>, @NonNull t: Throwable) {
                    //Toast.makeText(applicationContext, "Error in download for menu!", Toast.LENGTH_LONG).show()
                }
            })
        }

    }


    private val toolbarTitle = MutableLiveData<String>()

    fun getToolbarTitle(): String? {
        return toolbarTitle.value
    }

    fun setToolbarTitle(toolbarTitle: String) {
        this.toolbarTitle.setValue(toolbarTitle)
    }


    //todo доделать!!!
    val onNavigationClick = object : BottomNavigationView.OnNavigationItemSelectedListener {
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

    /*слушатель скролла списка поиска*/
    val onScrollViewRecycler = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val cardLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            if (isLoad) {
                if (cardLayoutManager.findLastCompletelyVisibleItemPosition() == repositoryVacancy.getSize() - 1) {
                    //Нашли конец списка
                    loadMoreCards()
                    isLoad = false
                }
            }
        }
    }

    /*слушатель строки поиска*/
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
                // обновляется благордаря колбеку в фрагменте
                // searchListAdapter.notifyDataSetChanged()
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

    /*загрузка главного фрагмента после проверкаи на первый запуск*/
    fun checkFirstLauch() {
        val duration = 2000L

        if (sPref.getBoolean(firstLaunchFlag, true))
            Handler().postDelayed({
                setFragmentLaunch("Intro")
            }, duration)
        else
            Handler().postDelayed({
                setFragmentLaunch("Welcome")
            }, duration)
    }

    /*нажатие на список подсказок в поиске*/
    fun onSuggestionsListItemClick(position: Int) {
        doSearchOnClick(suggestionsNamesList.value!![position].suggestionName)
        setSearchQuery(suggestionsNamesList.value!![position].suggestionName)
        setSearchListViewVisible(false)
    }

    /*загружаем больше в список поиска*/
    fun loadMoreCards() {
        val handler = Handler()
        handler.postDelayed({
            val nextLimit = repositoryVacancy.getSize() + 10
            val nextOffset = nextLimit - 10

            buildCardsList(nextLimit, nextOffset)
            isLoad = true
        }, SERVER_RESPONSE_DELAY)
    }

    /*клик по кнопке развернуть на карточке в поиске*/
    fun onExpandVacancyClick(position: Int) {
        vacancyPosition = position
        vacancyExpandInfo(position)

        val handler = Handler()
        handler.postDelayed({
            setFragmentLaunch("Vacancy")
        }, SERVER_RESPONSE_DELAY) // 1 сек чтобы обработать запрос от АПИ и вывести уже заполненную карточку
    }

    /*клик по кнопке развернуть на карточке в списке вакансий компании*/
    fun onExpandVacancyCompanyClick(position: Int) {
        vacancyPosition = position
        vacancyCompanyExpandInfo(position)

        val handler = Handler()
        handler.postDelayed({
            setFragmentLaunch("VacancyCompany")
        }, SERVER_RESPONSE_DELAY) // 1 сек чтобы обработать запрос от АПИ и вывести уже заполненную карточку
    }

    /*клик по глазу в карточке в поиске*/
    fun onEyeRVVacancyClick(position: Int) {
        Toast.makeText(context, "eye $position", Toast.LENGTH_SHORT).show()
    }

    /*информация для развернутой карточки*/
    private fun vacancyExpandInfo(position: Int) {
        val requestID: Int = repositoryVacancy.getVacancy().value!![position].id

        Retrofit.api?.loadVacancyCard(requestID, requestID)?.enqueue(object : Callback<CardVacancyDetail> {
            override fun onResponse(@NonNull call: Call<CardVacancyDetail>, @NonNull response: Response<CardVacancyDetail>) {
                if (response.code() == 404) {
                    setCardExpandResponse(false)
                }

                if (response.body() != null) {

                    val resultList: Detail = response.body()!!.detail

                    val newObj: VacancyEntity = repositoryVacancy.getVacancy().value!![position].copy(
                            companyDescription = resultList.company_description,
                            vacancyDescription = resultList.description,
                            requirementsDescription = resultList.requirements,
                            dutiesDescription = resultList.duties
                    )
                    repositoryVacancy.saveVacancy(newObj)
                    setCardExpandResponse(true)
                }
            }

            override fun onFailure(@NonNull call: Call<CardVacancyDetail>, @NonNull t: Throwable) {
            }
        })
    }

    /*информация для развернутой карточки компании*/
    private fun vacancyCompanyExpandInfo(position: Int) {
        val requestID: Int = repositoryCompanyVacancy.getCompanyVacancy().value!![position].id

        Retrofit.api?.loadVacancyCard(requestID, requestID)?.enqueue(object : Callback<CardVacancyDetail> {
            override fun onResponse(@NonNull call: Call<CardVacancyDetail>, @NonNull response: Response<CardVacancyDetail>) {
                if (response.code() == 404) {
                    setCardExpandResponse(false)
                }

                if (response.body() != null) {

                    val resultList: Detail = response.body()!!.detail

                    val newObj: VacancyEntity = repositoryCompanyVacancy.getCompanyVacancy().value!![position].copy(
                            companyDescription = resultList.company_description,
                            vacancyDescription = resultList.description,
                            requirementsDescription = resultList.requirements,
                            dutiesDescription = resultList.duties
                    )
                    repositoryCompanyVacancy.saveCompanyVacancy(newObj)
                    setCardExpandResponse(true)
                }
            }

            override fun onFailure(@NonNull call: Call<CardVacancyDetail>, @NonNull t: Throwable) {
            }
        })
    }

    /*посик по клику в строке поиска*/
    fun doSearchOnClick(query: String) {
        Retrofit.api?.loadVacancyByCompetence(query)?.enqueue(object : Callback<CardVacancy> {
            override fun onResponse(@NonNull call: Call<CardVacancy>, @NonNull response: Response<CardVacancy>) {
                if (response.body() != null) {

                    val resultList: List<ResultsVacancy> = response.body()!!.results

                    // Отчистить список для новых результатов
                    repositoryVacancy.clearRepository()

                    for (i in 0 until resultList.size) {
                        val tmpEmploymentList: MutableList<String> = java.util.ArrayList()
                        resultList[i].employment.forEach { employment ->
                            tmpEmploymentList.add(employment.name)
                        }

                        val tmpCompetenceList: MutableList<String> = java.util.ArrayList()
                        resultList[i].competences.forEach { competences ->
                            tmpCompetenceList.add(competences.name)
                        }

                        repositoryVacancy.saveVacancy(
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

    /*заполнение списка карточек*/
    fun buildCardsList(limitNext: Int, offsetNext: Int) {
        Retrofit.api?.loadVacancy(limitNext, offsetNext)?.enqueue(object : Callback<CardVacancy> {
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

                        repositoryVacancy.saveVacancy(
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

    /*загрузка списка подсказок к строке поиска*/
    fun doSearchCompetence(query: String) {
        Retrofit.api?.loadCompetence(query, SERVER_RESPONSE_MAX_COUNT)
                ?.enqueue(object : Callback<List<String>> {
                    override fun onResponse(@NonNull call: Call<List<String>>, @NonNull response: Response<List<String>>) {
                        if (response.body() != null) {

                            val resultList = response.body()

                            suggestionsNamesList.clear()

                            if (response.body()!!.isEmpty()) {
                                suggestionsNamesList.add(SuggestionEntity(context.getString(R.string.suggestions_empty_list)))
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

    /*открываем камеру для фото*/
    fun openCamera() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Метод resolveActivity() поможет проверить активности, способное сделать фотографию.
        // Если подходящего приложения не найдётся, то мы можем сделать кнопку для съёмки недоступной.
        if (cameraIntent.resolveActivity(context.packageManager) != null) {
            // создать файл для фотографии
            var photoFile: File? = null
            try {
                photoFile = createImageFile(context.baseContext)
            } catch (ex: IOException) {
                // ошибка, возникшая в процессе создания файла
            }

            // если файл создан, запускаем приложение камеры
            if (photoFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setOutputPhotoUri(
                            FileProvider.getUriForFile(
                                    context.applicationContext,
                                    BuildConfig.APPLICATION_ID + ".provider", //(use your app signature + ".provider" )
                                    photoFile
                            )
                    )
                } else
                    setOutputPhotoUri(Uri.fromFile(photoFile))
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputPhotoUri.value)
                //cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                setActivityLaunch(cameraIntent)
            }
        }
    }

    /*создание файла для фото*/
    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {

        // создание файла с уникальным именем
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "CAM" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        //        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        //        StrictMode.setVmPolicy(builder.build());

        val image = File.createTempFile(
                imageFileName, /* префикс */
                ".jpg", /* расширение */
                storageDir      /* директория */
        )

        //        ContentValues values = new ContentValues();
        //        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        //        values.put(MediaStore.Images.Media.MIME_TYPE, "image/ipeg");
        //        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());
        //
        //        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // сохраняем пусть для использования с интентом ACTION_VIEW
        setCurrentPhotoPath(/*"file:" + */image.absolutePath)
        return image
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

/*Для справки*/
//    private val users: MutableLiveData<List<String>> by lazy {
//        MutableLiveData<List<String>>().also {
//            //loadUsers()
//        }
//    }
//
//    fun getUsers(): LiveData<List<String>> {
//        return users
//    }

//    private fun loadUsers(): List<String> {
//        // Do an asynchronous operation to fetch users.
//    }

