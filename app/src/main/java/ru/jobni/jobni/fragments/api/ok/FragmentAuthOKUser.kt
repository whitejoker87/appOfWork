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
import ru.ok.android.sdk.Odnoklassniki
import ru.ok.android.sdk.util.OkAuthType
import ru.ok.android.sdk.util.OkScope

class FragmentAuthOKUser : Fragment() {

    // -------------- YOUR APP DATA GOES HERE ------------
    private val APP_ID = "1277635072"
    private val APP_KEY = "CBALCICNEBABABABA"
    private val REDIRECT_URL = "okauth://ok1277635072"
    // -------------- YOUR APP DATA ENDS -----------------

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

        val odnoklassniki = Odnoklassniki.createInstance(context, APP_ID, APP_KEY)

        binding.btnOkLogin.setOnClickListener {
            odnoklassniki.requestAuthorization(activity, REDIRECT_URL, OkAuthType.ANY, OkScope.VALUABLE_ACCESS)
        }

        binding.btnOkLogout.setOnClickListener {
            odnoklassniki.clearTokens()
        }

        return view
    }
}
