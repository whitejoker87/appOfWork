package ru.jobni.jobni.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.viewmodel.RegViewModel

class FragmentRegThree : Fragment() {

    private val viewModel: RegViewModel by lazy {
        ViewModelProviders.of(activity!!).get(RegViewModel::class.java)
    }

    private lateinit var binding: ru.jobni.jobni.databinding.CRegistration03Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.c_registration_03, container, false)
        val view = binding.root;
        binding.viewmodel = viewModel
        return view
    }
}
