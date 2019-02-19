package ru.jobni.jobni.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.jobni.jobni.R

class FragmentSplashScreen : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.splash_screen, container, false)

        // Bundle - свежий старт активности. А нам нужно загружаться только в 1 раз - поэтому проверка
        // TODO: Изучить Bundle

        if (savedInstanceState == null) {
            switchToMainActivity()
        }

        return view
    }

    private fun switchToMainActivity() {
        val duration = 2000L

        /*
        // Вариант с блокировкой экрана (использование одного потока)
        Thread.sleep(duration)
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, FragmentMain())
                ?.commit()
        */

        Handler().postDelayed({
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, FragmentMain())
                ?.commit()
        }, duration)
    }
}