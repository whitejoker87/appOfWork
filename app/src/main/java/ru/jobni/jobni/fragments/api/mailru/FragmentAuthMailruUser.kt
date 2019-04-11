package ru.jobni.jobni.fragments.api.mailru

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

    private var authenticationDialogMailru: AuthenticationDialogMailru? = null

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

        binding.btnUserMailruAuth.setOnClickListener{
            authenticationDialogMailru = AuthenticationDialogMailru(context!!, object : AuthenticationListenerMailru {
                override fun onTokenReceived(accessToken: String, vid: String) {
                    viewModelAuth.onAuthMailruClick(accessToken, vid)
                }
            })
            authenticationDialogMailru!!.setCancelable(true)
            authenticationDialogMailru!!.show()
        }

        // По кнопке логина просто переходим на сайт провайдера в свой личный кабинет (если авторизова)
        // чтобы например выйти из авторизации для последующих тестов.
        binding.btnUserMailruLogin.setOnClickListener {
            val authenticationDialogMailruLogin = AuthenticationDialogMailruLogin(context!!)
            authenticationDialogMailruLogin.setCancelable(true)
            authenticationDialogMailruLogin.show()
        }

        // Вызов окна авторизации
        binding.btnUserMailruAuth.setOnClickListener{
            authenticationDialogMailru = AuthenticationDialogMailru(context!!, object : AuthenticationListenerMailru {
                override fun onTokenReceived(accessToken: String, vid: String) {
                    viewModelAuth.onAuthMailruClick(accessToken, vid)
                }
            })
            authenticationDialogMailru!!.setCancelable(true)
            authenticationDialogMailru!!.show()
        }
        return view
    }
}
