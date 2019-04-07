package ru.jobni.jobni.fragments.api.instagram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CAuthorizationUserInstagramBinding
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel


class FragmentAuthInstagramUser : Fragment() {

    private var authenticationDialog: AuthenticationDialog? = null

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private lateinit var binding: CAuthorizationUserInstagramBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.c_authorization_user_instagram, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodelauth = viewModelAuth

        binding.viewmodelmain = viewModel

        binding.btnInstagramEnter.setOnClickListener{
            authenticationDialog = AuthenticationDialog(context!!, object : AuthenticationListener {
                    override fun onTokenReceived(auth_token: String) {
//                        appPreferences?.putString(AppPreferences.TOKEN, auth_token)
//                        token = auth_token
//                        getUserInfoByAccessToken(token!!)
                        viewModelAuth.convertInstagramCode(auth_token)
                    }
                })
            authenticationDialog!!.setCancelable(true)
            authenticationDialog!!.show()
        }

        return view
    }
}
