package ru.jobni.jobni.fragments.api.auth.win

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CAuthorizationUserWinBinding
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel


class FragmentAuthWinUser : Fragment() {

    private val socialProvider = "microsoft"

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private lateinit var binding: CAuthorizationUserWinBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.c_authorization_user_win, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodelauth = viewModelAuth

        binding.viewmodelmain = viewModel

        // По кнопке логина просто переходим на сайт провайдера в свой личный кабинет (если авторизова)
        // чтобы например выйти из авторизации для последующих тестов.
        binding.btnUserWinLogin.setOnClickListener {
            val authenticationDialogWinLogin = AuthDialogWinLogin(context!!)
            authenticationDialogWinLogin.setCancelable(true)
            authenticationDialogWinLogin.show()
        }

        // Вызов окна авторизации
        binding.btnUserWinAuth.setOnClickListener {
            val authenticationDialogMailru = AuthDialogWin(context!!, socialProvider, object : AuthListenerWin {
                override fun onTokenReceived(code: String) {
                    //Делаем с кодом что нибудь
                }
            })
            authenticationDialogMailru.setCancelable(true)
            authenticationDialogMailru.show()
        }
        return view
    }
}
