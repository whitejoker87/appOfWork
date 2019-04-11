package ru.jobni.jobni.fragments.api.google

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CAuthorizationUserGoogleBinding
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel


class FragmentAuthGoogleUser : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private lateinit var binding: CAuthorizationUserGoogleBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.c_authorization_user_google, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodelauth = viewModelAuth

        binding.viewmodelmain = viewModel

        // По кнопке логина просто переходим на сайт провайдера в свой личный кабинет (если авторизова)
        // чтобы например выйти из авторизации для последующих тестов.
        binding.btnUserGoogleLogin.setOnClickListener {
            val authenticationDialogOKLogin = AuthenticationDialogGoogleLogin(context!!)
            authenticationDialogOKLogin.setCancelable(true)
            authenticationDialogOKLogin.show()
        }

        // Вызов окна авторизации
        binding.btnUserGoogleAuth.setOnClickListener {
            val authenticationDialogGoogle = AuthenticationDialogGoogle(context!!, object : AuthenticationListenerGoogle {
                override fun onTokenReceived(code: String) {
                    //Делаем с кодом что нибудь
                }
            })
            authenticationDialogGoogle.setCancelable(true)
            authenticationDialogGoogle.show()
        }

        return view
    }
}
