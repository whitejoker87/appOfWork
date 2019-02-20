package ru.jobni.jobni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.model.IntroSlide
import ru.jobni.jobni.viewmodel.IntroViewModel
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var model: IntroViewModel

    private var isFirstLaunch: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val slides = ArrayList<IntroSlide>()
        model = ViewModelProviders.of(this).get(IntroViewModel::class.java)

        val firstSlide = IntroSlide(ivIntroID = R.drawable.notification_tile_bg)
        slides.add(firstSlide)

        val secondSlide = IntroSlide(ivIntroID = R.drawable.abc_ic_star_half_black_48dp)
        slides.add(secondSlide)

        model.setListSlides(slides)

        setContentView(R.layout.activity_main)

        setFragment(IntroFragment.newInstance())

//        model.setsPref(getSharedPreferences("loginSavedData", MODE_PRIVATE))
//        //model.saveLaunchFlag(true);//для отладки первого запуска
//        isFirstLaunch = model.getsPref().getBoolean(model.getFirstLaunchFlag(), true)
//        if (isFirstLaunch)
//            model.setFragmentOrActivityLaunch("training")
//        else
//            model.setFragmentOrActivityLaunch("login")

    }

    fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_intro, fragment)
        transaction.commit()
    }
}
