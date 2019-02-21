package ru.jobni.jobni

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.jobni.jobni.fragments.FragmentSplashScreen

// TODO: Изучить Android Navigation Component
// https://startandroid.ru/ru/courses/dagger-2/27-course/architecture-components/557-urok-24-android-navigation-component-vvedenie.html

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentSplashScreen())
                .commit()
        }
    }
}