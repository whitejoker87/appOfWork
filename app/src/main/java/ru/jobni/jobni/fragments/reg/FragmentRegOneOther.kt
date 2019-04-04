package ru.jobni.jobni.fragments.reg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.viewmodel.MainViewModel
import ru.jobni.jobni.viewmodel.RegViewModel

class FragmentRegOneOther : Fragment() {

    private val regViewModel: RegViewModel by lazy {
        ViewModelProviders.of(activity!!).get(RegViewModel::class.java)
    }

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: ru.jobni.jobni.databinding.CRegistration01OtherBinding

    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.c_registration_01_other, container, false)
        val view = binding.root;

//        when(param1) {
//            "vk" ->
//        }
        binding.viewmodelreg = regViewModel
        binding.viewmodelmain = mainViewModel

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            FragmentRegOneOther().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }

        private const val ARG_PARAM1 = "param1"
    }
}
