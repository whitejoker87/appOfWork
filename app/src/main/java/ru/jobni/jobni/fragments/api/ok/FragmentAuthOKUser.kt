package ru.jobni.jobni.fragments.api.ok

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CAuthorizationUserOkBinding
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentAuthOKUser : Fragment() {

    private var authenticationDialogOK: AuthenticationDialogOK? = null

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private lateinit var binding: CAuthorizationUserOkBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.c_authorization_user_ok, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodelauth = viewModelAuth

        binding.viewmodelmain = viewModel

        binding.btnOkLogin.setOnClickListener {
            authenticationDialogOK = AuthenticationDialogOK(context!!, object : AuthenticationListenerOK {
                override fun onTokenReceived(accessToken: String, sig: String) {
                    viewModelAuth.convertOKCode(accessToken, sig)
                }
            })
            authenticationDialogOK!!.setCancelable(true)
            authenticationDialogOK!!.show()
        }

        return view
    }
}
