package ru.jobni.jobni

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.jobni.jobni.fragments.FragmentIntroSlide
import ru.jobni.jobni.fragments.FragmentMain
import ru.jobni.jobni.fragments.FragmentSplashScreen

// TODO: Изучить Android Navigation Component
// https://startandroid.ru/ru/courses/dagger-2/27-course/architecture-components/557-urok-24-android-navigation-component-vvedenie.html

class MainActivity : AppCompatActivity(),FragmentIntroSlide.OnClickBtnStartListener {

    private val firstLaunchFlag = "firstLaunch"
    private lateinit var sPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sPref = getSharedPreferences("firstLaunchSavedData", MODE_PRIVATE)
        saveLaunchFlag(true)
        if (savedInstanceState == null) {
            setFragment(FragmentSplashScreen())
        }
    }

    override fun onClickBtnStart() {
        saveLaunchFlag()
        setFragment(FragmentMain())
    }

    fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
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

}