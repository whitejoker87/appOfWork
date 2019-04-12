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

    private val TG_PASSPORT_RESULT = 7
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
            req.botID = 649847916
            req.publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAruw2yP/BCcsJliRoW5eB\n" +
                    "VBVle9dtjJw+OYED160Wybum9SXtBBLXriwt4rROd9csv0t0OHCaTmRqBcQ0J8fx\n" +
                    "hN6/cpR1GWgOZRUAiQxoMnlt0R93LCX/j1dnVa/gVbCjdSxpbrfY2g2L4frzjJvd\n" +
                    "l84Kd9ORYjDEAyFnEA7dD556OptgLQQ2e2iVNq8NZLYTzLp5YpOdO1doK+ttrltg\n" +
                    "gTCy5SrKeLoCPPbOgGsdxJxyz5KKcZnSLj16yE5HvJQn0CNpRdENvRUXe6tBP78O\n" +
                    "39oJ8BTHp9oIjd6XWXAsp2CvK45Ol8wFXGF710w9lwCGNbmNxNYhtIkdqfsEcwR5\n" +
                    "JwIDAQAB\n" +
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
