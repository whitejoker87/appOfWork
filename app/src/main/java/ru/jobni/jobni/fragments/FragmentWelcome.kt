package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.FragmentWelcomeBinding
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentWelcome : Fragment() {

    companion object {
        fun newInstance() = FragmentWelcome()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
        val view = binding.root;
        binding.viewmodel = viewModel
        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.setToolbarVisible(true)
        viewModel.setBottomNavigationViewVisible(true)
        viewModel.setSearchViewVisible(false)
        viewModel.setDrawerRightLocked(false)
    }
}
