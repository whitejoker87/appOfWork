package ru.jobni.jobni.fragments.api.reg

//import ru.jobni.jobni.fragments.api.facebook.PrefUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.telegram.passport.*
import ru.jobni.jobni.R
import ru.jobni.jobni.viewmodel.MainViewModel
import ru.jobni.jobni.viewmodel.RegViewModel
import java.util.*


class FragmentRegOneOther : Fragment() {

    private val regViewModel: RegViewModel by lazy {
        ViewModelProviders.of(activity!!).get(RegViewModel::class.java)
    }

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: ru.jobni.jobni.databinding.CRegistration01OtherBinding

//    private lateinit var callbackManager: CallbackManager

//    private var prefUtil: PrefUtil? = null

//    private var param1: String? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//        }
//    }


    private lateinit var loginButton: TelegramLoginButton
    private val payload = UUID.randomUUID().toString()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

//        callbackManager = CallbackManager.Factory.create()

        binding = DataBindingUtil.inflate(inflater, R.layout.c_registration_01_other, container, false)
        binding.lifecycleOwner = this

        val view = binding.root;

//        when(param1) {
//            "vk" ->
//        }
        binding.viewmodelreg = regViewModel
        binding.viewmodelmain = mainViewModel

        /*забрал из аута*/
        // Facebook check tokens
//        val accessToken = AccessToken.getCurrentAccessToken()
//        val isLoggedIn = accessToken != null && !accessToken.isExpired
//        val profile = Profile.getCurrentProfile()

//        binding.loginButtonFb.setReadPermissions("public_profile", "email")
//        // If using in a fragment
//        binding.loginButtonFb.fragment = this
//
//        // Callback registration
//        binding.loginButtonFb.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//            override fun onSuccess(loginResult: LoginResult) {
//                val accessTokenFB = loginResult.accessToken.token
//                val userID = loginResult.accessToken.userId
//
//                // save accessToken to SharedPreference
////                prefUtil?.saveAccessToken(accessTokenFB)
//
//                val request = GraphRequest.newMeRequest(loginResult.accessToken) { jsonObject, response ->
//                    // Getting FB User Data
//                    val facebookData = getFacebookData(jsonObject)
//                }
//
//                val parameters = Bundle()
//                parameters.putString("fields", "id,first_name,last_name,email,gender")
//                request.parameters = parameters
//                request.executeAsync()
//
//                regViewModel.sendSocialData(userID, "facebook", accessTokenFB)
//
//            }
//
//            override fun onCancel() {
//                Log.d(AccessTokenManager.TAG, "Login attempt cancelled.")
//            }
//
//            override fun onError(exception: FacebookException) {
//                exception.printStackTrace()
//                Log.d(AccessTokenManager.TAG, "Login attempt failed.")
//                deleteAccessToken()
//            }
//        })



        /*for Telegram*/
        loginButton = binding.loginButtonTel
		loginButton.setOnClickListener {
            val params = TelegramPassport.AuthRequest()
            params.botID = 443863171
            params.nonce = payload
            params.publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                    "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAzmgKr0fPP4rB/TsNEweC\n" +
                    "hoG3ntUxuBTmHsFBW6CpABGdaTmKZSjAI/cTofhBgtRQIOdX0YRGHHHhwyLf49Wv\n" +
                    "9l+XexbJOa0lTsJSNMj8Y/9sZbqUl5ur8ZOTM0sxbXC0XKexu1tM9YavH+Lbrobk\n" +
                    "jt0+cmo/zEYZWNtLVihnR2IDv+7tSgiDoFWi/koAUdfJ1VMw+hReUaLg3vE9CmPK\n" +
                    "tQiTy+NvmrYaBPb75I0Jz3Lrz1+mZSjLKO25iT84RIsxarBDd8iYh2avWkCmvtiR\n" +
                    "Lcif8wLxi2QWC1rZoCA3Ip+Hg9J9vxHlzl6xT01WjUStMhfwrUW6QBpur7FJ+aKM\n" +
                    "oaMoHieFNCG4qIkWVEHHSsUpLum4SYuEnyNH3tkjbrdldZanCvanGq+TZyX0buRt\n" +
                    "4zk7FGcu8iulUkAP/o/WZM0HKinFN/vuzNVA8iqcO/BBhewhzpqmmTMnWmAO8WPP\n" +
                    "DJMABRtXJnVuPh1CI5pValzomLJM4/YvnJGppzI1QiHHNA9JtxVmj2xf8jaXa1LJ\n" +
                    "WUNJK+RvUWkRUxpWiKQQO9FAyTPLRtDQGN9eUeDR1U0jqRk/gNT8smHGN6I4H+NR\n" +
                    "3X3/1lMfcm1dvk654ql8mxjCA54IpTPr/icUMc7cSzyIiQ7Tp9PZTl1gHh281ZWf\n" +
                    "P7d2+fuJMlkjtM7oAwf+tI8CAwEAAQ==\n" +
                    "-----END PUBLIC KEY-----"
            params.scope = PassportScope(
                PassportScopeElementOneOfSeveral(PassportScope.PASSPORT, PassportScope.IDENTITY_CARD).withSelfie(),
                PassportScopeElementOne(PassportScope.PERSONAL_DETAILS).withNativeNames(),
                PassportScope.DRIVER_LICENSE,
                PassportScope.ADDRESS,
                PassportScope.ADDRESS_DOCUMENT,
                PassportScope.PHONE_NUMBER
            )
            TelegramPassport.request(activity, params, 105)
        }

//		cornerRoundnessSlider=findViewById(R.id.corner_roundness_slider);
//		cornerRoundnessSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
//				loginButton.setCornerRoundness(progress/(float)seekBar.getMax());
//			}
//
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar){
//
//			}
//
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar){
//
//			}
//        })



        return view
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String) =
//            FragmentRegOneOther().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                }
//            }
//
//        private const val ARG_PARAM1 = "param1"
//    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    fun deleteAccessToken() {
//        object : AccessTokenTracker() {
//            override fun onCurrentAccessTokenChanged(
//                    oldAccessToken: AccessToken,
//                    currentAccessToken: AccessToken?) {
//
//                if (currentAccessToken == null) {
//                    //User logged out
////                    prefUtil?.clearToken()
//                    LoginManager.getInstance().logOut()
//                }
//            }
//        }
//    }

//    private fun getFacebookData(`object`: JSONObject): Bundle? {
//        val bundle = Bundle()
//
//        try {
//            val id = `object`.getString("id")
//            val profile_pic: URL
//            try {
//                profile_pic = URL("https://graph.facebook.com/$id/picture?type=large")
//                //Log.i("profile_pic", profile_pic + "")
//                bundle.putString("profile_pic", profile_pic.toString())
//            } catch (e: MalformedURLException) {
//                e.printStackTrace()
//                return null
//            }
//
//            bundle.putString("idFacebook", id)
//            if (`object`.has("first_name"))
//                bundle.putString("first_name", `object`.getString("first_name"))
//            if (`object`.has("last_name"))
//                bundle.putString("last_name", `object`.getString("last_name"))
//            if (`object`.has("email"))
//                bundle.putString("email", `object`.getString("email"))
//            if (`object`.has("gender"))
//                bundle.putString("gender", `object`.getString("gender"))
//
//
////            prefUtil?.saveFacebookUserInfo(`object`.getString("first_name"),
////                    `object`.getString("last_name"), `object`.getString("email"),
////                    `object`.getString("gender"), profile_pic.toString())
//
//        } catch (e: Exception) {
//            Log.d(AccessTokenManager.TAG, "BUNDLE Exception : $e")
//        }
//
//        return bundle
//    }
}