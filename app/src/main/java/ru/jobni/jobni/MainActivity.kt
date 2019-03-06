package ru.jobni.jobni

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.fragments.*
import ru.jobni.jobni.model.network.vacancy.*
import ru.jobni.jobni.utils.ExpandableListAdapter
import ru.jobni.jobni.utils.Retrofit
import java.util.*


// TODO: Изучить Android Navigation Component
// https://startandroid.ru/ru/courses/dagger-2/27-course/architecture-components/557-urok-24-android-navigation-component-vvedenie.html

class MainActivity : AppCompatActivity(), FragmentIntroSlide.OnClickBtnStartListener {


    private val firstLaunchFlag = "firstLaunch"
    private lateinit var sPref: SharedPreferences
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var popup: PopupMenu
    private lateinit var drawer: DrawerLayout

    private lateinit var expandableListAdapter: ExpandableListAdapter
    private lateinit var expandableListView: ExpandableListView
    private val headerList = ArrayList<String>()
    private val childList = HashMap<String, List<String>>()
    private lateinit var btnList: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawer = findViewById(R.id.drawer_layout)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        bottomNavigationView = findViewById(R.id.menu_bottom)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        expandableListView = findViewById(R.id.exp_list_view)

        btnList = findViewById(R.id.list)
        btnList.setOnClickListener { openRightMenu() }

        popup = PopupMenu(this@MainActivity, findViewById(R.id.bottom_menu_profile))
        val inflater = popup.getMenuInflater()
        inflater.inflate(R.menu.bottom_profile_not_logged_in, popup.getMenu())
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.reg_bottom_not_logged -> setFragment(FragmentRegAuth.newInstance("reg"))
                    R.id.auth_bottom_not_logged -> setFragment(FragmentRegAuth.newInstance("auth"))
                }
                return true
            }
        })

        sPref = getSharedPreferences("firstLaunchSavedData", MODE_PRIVATE)
        saveLaunchFlag(true)//отладка первого запуска true
        if (savedInstanceState == null) {
            setFragment(FragmentSplashScreen())
        }
    }

    override fun onBackPressed() {
        when {
            drawer.isDrawerOpen(GravityCompat.END) -> drawer.closeDrawer(GravityCompat.END)
            supportFragmentManager.backStackEntryCount > 0 -> supportFragmentManager.popBackStack()
            else -> super.onBackPressed()
        }
    }

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.bottom_menu_search -> {
                    setFragment(FragmentMain())
                    return true
                }
                R.id.bottom_menu_notification -> {
                    return true
                }
                R.id.bottom_menu_chat -> {
                    return true
                }
                R.id.bottom_menu_profile -> {
                    popup.show()
                    return true
                }
            }
            return false
        }
    }


    override fun onClickBtnStart() {
        saveLaunchFlag()
        setFragment(FragmentWelcome.newInstance())
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun saveLaunchFlag() {
        val editor = sPref.edit()
        editor?.putBoolean(firstLaunchFlag, false)
        editor?.apply()
    }

    private fun saveLaunchFlag(reset: Boolean) {//для отладки первого запуска
        val editor = sPref.edit()
        editor.putBoolean(firstLaunchFlag, reset)
        editor.apply()
    }

    private fun openRightMenu() {
        if (headerList.isEmpty()) {
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

                        expandableListAdapter = ExpandableListAdapter(applicationContext, headerList, childList)
                        expandableListView.setAdapter(expandableListAdapter)
                    }
                }

                override fun onFailure(@NonNull call: Call<DetailVacancy>, @NonNull t: Throwable) {
                    Toast.makeText(applicationContext, "Error in download for menu!", Toast.LENGTH_LONG).show()
                }
            })
        }

        drawer.openDrawer(GravityCompat.END)
        //ниже закрываем клавиатуру если открыта
        val view = this.currentFocus
        view?.let { v ->
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
        }
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
}