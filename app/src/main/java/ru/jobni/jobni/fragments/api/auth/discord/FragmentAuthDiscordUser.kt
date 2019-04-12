package ru.jobni.jobni.fragments.api.auth.discord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CAuthorizationUserDiscordBinding
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel


class FragmentAuthDiscordUser : Fragment() {

    private val socialProvider = "discord"

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private lateinit var binding: CAuthorizationUserDiscordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.c_authorization_user_discord, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodelauth = viewModelAuth

        binding.viewmodelmain = viewModel

        // По кнопке логина просто переходим на сайт провайдера в свой личный кабинет (если авторизован)
        // чтобы например выйти из авторизации для последующих тестов.
        binding.btnUserDiscordLogin.setOnClickListener {
            val authDialogDiscordLogin = AuthDialogDiscordLogin(context!!)
            authDialogDiscordLogin.setCancelable(true)
            authDialogDiscordLogin.show()
        }

        // Вызов окна авторизации
        binding.btnUserDiscordAuth.setOnClickListener {
            val authDialogDiscord = AuthDialogDiscord(context!!, socialProvider, object : AuthListenerDiscord {
                override fun onTokenReceived(code: String) {
                    //Делаем с кодом что нибудь
                }
            })
            authDialogDiscord.setCancelable(true)
            authDialogDiscord.show()
        }

        return view
    }
}
