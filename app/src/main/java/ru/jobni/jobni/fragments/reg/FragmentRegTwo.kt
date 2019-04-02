package ru.jobni.jobni.fragments.reg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.viewmodel.MainViewModel
import ru.jobni.jobni.viewmodel.RegViewModel

class FragmentRegTwo : Fragment() {

    private val regViewModel: RegViewModel by lazy {
        ViewModelProviders.of(activity!!).get(RegViewModel::class.java)
    }

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: ru.jobni.jobni.databinding.CRegistration02Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.c_registration_02, container, false)
        binding.lifecycleOwner = this
        val view = binding.root;
        binding.regViewModel = regViewModel
        binding.mainViewModel = mainViewModel

//        regViewModel.getResultReg1Success().observe(this, Observer {
//            Toast.makeText(context, "Регистрация успешно проехала? ЭТАП 2 ${it}", Toast.LENGTH_LONG).show()
//        })

        return view
    }
}
