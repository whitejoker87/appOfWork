package ru.jobni.jobni.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CAuthorizationMailBinding
import ru.jobni.jobni.viewmodel.AuthViewModel

class FragmentAuthMail : Fragment() {

    private val viewModel: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private lateinit var binding: CAuthorizationMailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.c_authorization_mail, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodel = viewModel

        viewModel.doAuthMailGet()

        viewModel.getFragmentLaunch().observe(this, Observer { fragmentType ->
            when (fragmentType) {
                "Auth" -> setFragment(FragmentAuth())
                else -> setFragment(FragmentAuth())
            }
            closeKeyboard()
        })

        return view
    }

    private fun setFragment(fragment: Fragment) {
        activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun closeKeyboard() {
        //скрываем клавиатуру
        val viewCloseButton = activity?.currentFocus
        viewCloseButton?.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}
