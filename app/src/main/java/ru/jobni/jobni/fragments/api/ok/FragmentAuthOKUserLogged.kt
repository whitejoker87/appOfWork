package ru.jobni.jobni.fragments.api.ok

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CAuthorizationUserOkLoggedBinding
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel
import ru.ok.android.sdk.Odnoklassniki

class FragmentAuthOKUserLogged : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private lateinit var binding: CAuthorizationUserOkLoggedBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.c_authorization_user_ok_logged, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodelauth = viewModelAuth

        binding.viewmodelmain = viewModel

        val ok = Odnoklassniki.createInstance(
                context,
                context?.resources?.getString(R.string.ok_client_id),
                context?.resources?.getString(R.string.ok_public_key)
        )

        binding.btnChangeUserAuth.setOnClickListener {
            // Сбросить токен от сдк однокласников
            ok.clearTokens()
            // Вызвать метод сброса авторизации
            viewModelAuth.onAuthOKChangeClick()
        }

        return view
    }
}
