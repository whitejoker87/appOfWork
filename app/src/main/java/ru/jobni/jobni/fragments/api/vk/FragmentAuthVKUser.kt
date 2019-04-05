package ru.jobni.jobni.fragments.api.vk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.vk.api.sdk.VK
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CAuthorizationUserVkBinding
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel
import ru.jobni.jobni.viewmodel.RegViewModel

class FragmentAuthVKUser : Fragment() {

//    companion object {
//        fun startFrom(context: Context) {
//            val intent = Intent(context, FragmentAuthVKUser::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
////            context.startActivity(intent)
//        }
//    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private val viewModelReg: RegViewModel by lazy {
        ViewModelProviders.of(activity!!).get(RegViewModel::class.java)
    }

    private lateinit var binding: CAuthorizationUserVkBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

//        if (VK.isLoggedIn()) {
//            FragmentAuthVKUserData.startFrom(context!!)
//            return view
//        }

        binding = DataBindingUtil.inflate(inflater, R.layout.c_authorization_user_vk, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodelauth = viewModelAuth

        binding.viewmodelmain = viewModel

        binding.viewmodelreg = viewModelReg

        binding.btnUserVkLogin.setOnClickListener {
            VK.login(activity!!, arrayListOf())
        }

        binding.btnUserVkAuth.setOnClickListener {
            viewModelReg.btnVKClickAuth()
        }

        return view
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val callback = object : VKAuthCallback {
//            override fun onLogin(token: VKAccessToken) {
//                // Пользователь успешно авторизовался
//                val acsessToken = token.accessToken
//                val acsessTokenSecretKey = token.secret
//                val userLogin = token.userId
//
//                FragmentAuthVKUserData.startFrom(activity as Context)
//            }
//
//            override fun onLoginFailed(errorCode: Int) {
//                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
//            }
//        }
//        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }
}
