package ru.jobni.jobni

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import ru.jobni.jobni.fragments.FragmentIntroSlide
import ru.jobni.jobni.fragments.FragmentSplashScreen
import ru.jobni.jobni.fragments.FragmentWelcome

class MainActivity : AppCompatActivity(), FragmentIntroSlide.OnClickBtnStartListener {

    private val firstLaunchFlag = "firstLaunch"
    private lateinit var sPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sPref = getSharedPreferences("firstLaunchSavedData", MODE_PRIVATE)
        saveLaunchFlag(true)//отладка первого запуска
        if (savedInstanceState == null) {
            setFragment(FragmentSplashScreen())
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

        when {
            drawer.isDrawerOpen(GravityCompat.END) -> drawer.closeDrawer(GravityCompat.END)
            supportFragmentManager.backStackEntryCount > 0 -> supportFragmentManager.popBackStack()
            else -> super.onBackPressed()
        }
    }

    override fun onClickBtnStart() {
        saveLaunchFlag()
        setFragment(FragmentWelcome())
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
}