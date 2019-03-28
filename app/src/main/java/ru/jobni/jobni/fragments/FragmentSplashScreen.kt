package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentSplashScreen : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.e_splash_screen, container, false)

        // Bundle - свежий старт активности. А нам нужно загружаться только в 1 раз - поэтому проверка
        // TODO: Изучить Bundle

        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        if (savedInstanceState == null) {
            viewModel.checkFirstLauch()
        }
        return view
    }
}