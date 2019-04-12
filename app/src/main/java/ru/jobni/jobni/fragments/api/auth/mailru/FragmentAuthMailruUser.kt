package ru.jobni.jobni.fragments.api.auth.mailru

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CAuthorizationUserMailruBinding
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel


class FragmentAuthMailruUser : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private lateinit var binding: CAuthorizationUserMailruBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.c_authorization_user_mailru, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodelauth = viewModelAuth

        binding.viewmodelmain = viewModel

        // По кнопке логина просто переходим на сайт провайдера в свой личный кабинет (если авторизован)
        // чтобы например выйти из авторизации для последующих тестов.
        binding.btnUserMailruLogin.setOnClickListener {
            val authDialogMailruLogin = AuthDialogMailruLogin(context!!)
            authDialogMailruLogin.setCancelable(true)
            authDialogMailruLogin.show()
        }

        // Вызов окна авторизации
        binding.btnUserMailruAuth.setOnClickListener{
            val authDialogMailru = AuthDialogMailru(context!!, object : AuthListenerMailru {
                override fun onTokenReceived(code: String) {
                    //Делаем с кодом что нибудь
                }
            })
            authDialogMailru.setCancelable(true)
            authDialogMailru.show()
        }
        return view
    }
}
