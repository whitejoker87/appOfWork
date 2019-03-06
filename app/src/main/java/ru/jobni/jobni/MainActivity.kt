package ru.jobni.jobni

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.jobni.jobni.fragments.*


// TODO: Изучить Android Navigation Component
// https://startandroid.ru/ru/courses/dagger-2/27-course/architecture-components/557-urok-24-android-navigation-component-vvedenie.html

class MainActivity : AppCompatActivity(), FragmentIntroSlide.OnClickBtnStartListener {


    private val firstLaunchFlag = "firstLaunch"
    private lateinit var sPref: SharedPreferences
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var popup: PopupMenu
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawer = findViewById(R.id.drawer_layout)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        bottomNavigationView = findViewById(R.id.menu_bottom)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

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
        saveLaunchFlag(false)//отладка первого запуска true
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
}