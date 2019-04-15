package ru.jobni.jobni.fragments.api.auth.telegram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.telegram.passport.PassportScope
import org.telegram.passport.TelegramPassport
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CAuthorizationUserTelegramBinding
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel
import java.util.*


class FragmentAuthTelegramUser : Fragment() {

    private val TG_PASSPORT_RESULT = 107
    private val payload = UUID.randomUUID().toString()

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private lateinit var binding: CAuthorizationUserTelegramBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.c_authorization_user_telegram, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodelauth = viewModelAuth

        binding.viewmodelmain = viewModel

        binding.btnUserTelegramAuth.setOnClickListener {

            val req = TelegramPassport.AuthRequest()
            req.botID = 754325666
            req.publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqOzs+28ZpVTAzYfvY+rP\n" +
                    "LP1JYUXMROxqK4rUGZlPReAQQvUc61pM8DmFK9iNQvBJj6TXcV41wl5OVTscbtrR\n" +
                    "mX5dIhVgCsYAFmgL6HVnON/sC1TSr5j4JiWux9yReeSCuI83f0ie3QPQ3pY8WUNh\n" +
                    "TgmU/752CPzm4ZemGdFrXk7mJG8sCoWsX9S0gdjce4e8r6/FgRWTcw2AD0tRVaDI\n" +
                    "9EsUb3l0O71M1E/ZKtvf8PNmfBoAZImy6A3PTQ9Rdgku1Z4m4GMpn2vsxva5wjx9\n" +
                    "V4IXjaznA5sW+13CFzgnGBSOf1LSgpxrbzCr5nyoYy4plS+5gtsCXa8dJe2T4naL\n" +
                    "kQIDAQAB\n" +
                    "-----END PUBLIC KEY-----"
            req.nonce = payload
            req.scope = PassportScope(
                    PassportScope.PHONE_NUMBER
            )
            TelegramPassport.request(activity, req, TG_PASSPORT_RESULT)
        }

        return view
    }
}
