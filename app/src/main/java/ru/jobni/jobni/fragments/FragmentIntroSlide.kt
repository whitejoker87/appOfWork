package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CIntro05Binding
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentIntroSlide : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: CIntro05Binding

    private var param1: Int = 0

    companion object {

        private const val ARG_PARAM1 = "param1"

        @JvmStatic
        fun newInstance(num: Int) =
                FragmentIntroSlide().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, num)
                    }
                }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(param1, container, false)
        return if (param1 == R.layout.c_intro_05) {
            binding = DataBindingUtil.inflate(inflater, param1, container, false)
            binding.root
        } else view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (param1 == R.layout.c_intro_05) {
            viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
            binding.viewmodel = viewModel
        }
    }
}
