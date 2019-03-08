package ru.jobni.jobni.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.fragments.FragmentWelcome
import ru.jobni.jobni.model.network.vacancy.*
import ru.jobni.jobni.utils.Retrofit
import java.util.ArrayList
import java.util.HashMap

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val firstLaunchFlag = "firstLaunch"
    var sPref = application.getSharedPreferences("firstLaunchSavedData", AppCompatActivity.MODE_PRIVATE)
    private lateinit var bodyResponse: DetailVacancy
    private val headerList = MutableLiveData<MutableList<String>>()
    private val childList = MutableLiveData<HashMap<String, List<String>>>()
    private val isOpenDrawer = MutableLiveData<Boolean>()
    private val fragmentLaunch = MutableLiveData<String>()
    val context = application

    fun getHeaderList(): MutableLiveData<MutableList<String>> {
        return headerList
    }

    fun getChildList(): MutableLiveData<HashMap<String, List<String>>> {
        return childList
    }

    fun setOpenDrawer(isOpen: Boolean) {
        isOpenDrawer.value = isOpen
    }

    fun isOpenDrawer(): MutableLiveData<Boolean> {
        return isOpenDrawer
    }

    fun setFragmentLaunch(fragmentType: String) {
        fragmentLaunch.value = fragmentType
    }

    fun getFragmentLaunch(): MutableLiveData<String> {
        return fragmentLaunch
    }

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

    fun onClickBtnStart() {
        saveLaunchFlag()
        setFragmentLaunch("Welcome")
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

}