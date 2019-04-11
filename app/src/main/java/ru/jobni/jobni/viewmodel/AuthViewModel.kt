package ru.jobni.jobni.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.R
import ru.jobni.jobni.model.auth.mail.UserMailAuth
import ru.jobni.jobni.model.auth.phone.UserPhoneAuth
import ru.jobni.jobni.model.network.auth.*
import ru.jobni.jobni.utils.Retrofit


class AuthViewModel(application: Application) : AndroidViewModel(application) {

    val context = application

    private val authMailSessionID = "userMailSessionID"
    private val authMailUser = "userMail"
    private val authMailPass = "userPass"

    var sPrefAuthMailUser = application.getSharedPreferences("authMail", AppCompatActivity.MODE_PRIVATE)


    private val authPhoneSessionID = "userPhoneSessionID"
    private val authPhoneUser = "userPhone"
    private val authPhoneUserPassword = "userPhonePassword"

    var sPrefAuthPhoneUser = application.getSharedPreferences("authPhone", AppCompatActivity.MODE_PRIVATE)


    // Данные при регистрации, читаем здесь для sessionID
    private val authUserSessionID = "userSessionID"

    var sPrefAuthUser = application.getSharedPreferences("authUser", AppCompatActivity.MODE_PRIVATE)


    /*цвет кнопки при логине пользователя*/
    private val btnUserLogged = MutableLiveData<String>("")
    private val isUserAuthid = MutableLiveData<Boolean>()

    /*Блок Mail*/
    private val authMail = MutableLiveData<String>()
    private val authPass = MutableLiveData<String>()

    /*Блок FB*/
    private val authFBUser = MutableLiveData<String>()
    private val authFBPass = MutableLiveData<String>()

    /*Блок Google+*/
    private val authGoogleUser = MutableLiveData<String>()
    private val authGooglePass = MutableLiveData<String>()

    /*Блок OK*/
    private val authOKUser = MutableLiveData<String>()
    private val authOKPass = MutableLiveData<String>()

    /*Блок VK*/
    private val vkAuthStart = MutableLiveData<Boolean>()

    /*Блок Phone*/
    private val authPhone = MutableLiveData<String>("")
    private val authPhonePassword = MutableLiveData<String>("")


    fun setBtnUserLogged(typeLogged: String) {
        btnUserLogged.value = typeLogged
    }

    fun getBtnUserLogged(): MutableLiveData<String> = btnUserLogged


    fun setUserAuthid(authKey: Boolean) {
        isUserAuthid.value = authKey
    }

    fun isUserAuthid(): MutableLiveData<Boolean> = isUserAuthid


    /* Блок обычной авторизации */
    fun getAuthMail(): String? = authMail.value

    fun setAuthMail(query: String) {
        this.authMail.value = query
    }


    fun getAuthPass(): String? = authPass.value

    fun setAuthPass(query: String) {
        this.authPass.value = query
    }


    /* Блок facebook авторизации */
    fun getFBAuthUser(): String? = authFBUser.value

    fun setFBAuthUser(query: String) {
        this.authFBUser.value = query
    }


    fun getFBAuthPass(): String? = authFBPass.value

    fun setFBAuthPass(query: String) {
        this.authFBPass.value = query
    }


    /* Блок google+ авторизации */
    fun getGoogleAuthUser(): String? = authGoogleUser.value

    fun setGoogleAuthUser(query: String) {
        this.authGoogleUser.value = query
    }


    fun getGoogleAuthPass(): String? = authGooglePass.value

    fun setGoogleAuthPass(query: String) {
        this.authGooglePass.value = query
    }


    /* Блок OK авторизации */
    fun getOKAuthUser(): String? = authOKUser.value

    fun setOKAuthUser(query: String) {
        this.authOKUser.value = query
    }


    fun getOKAuthPass(): String? = authOKPass.value

    fun setOKAuthPass(query: String) {
        this.authOKPass.value = query
    }


    /* Блок VK авторизации */
    fun isVkAuthStart(): MutableLiveData<Boolean> = vkAuthStart

    fun setVkAuthStart(isStart: Boolean) {
        vkAuthStart.value = isStart
    }


    /* Блок Phone авторизации */
    fun getAuthPhone(): String? = authPhone.value

    fun setAuthPhone(query: String) {
        this.authPhone.value = query
    }


    fun getAuthPhonePassword(): String? = authPhonePassword.value

    fun setAuthPhonePassword(query: String) {
        this.authPhonePassword.value = query
    }


    // Временный метод для разных авторизаций
    fun onAuthUserChangeClick(): Boolean {
        return false
    }

    fun onAuthMailUserChangeClick(): Boolean {
        val editor = sPrefAuthMailUser.edit()
        editor?.remove(authMailSessionID)
        editor?.remove(authMailUser)
        editor?.remove(authMailPass)
        editor?.apply()

        setBtnUserLogged("")
        setUserAuthid(false)

        return false
    }

    fun onAuthMailUserClick() {

        val userData = UserMailAuth(getAuthMail(), getAuthPass())

        Retrofit.api?.postAuthData("AuthMailUser", userData)?.enqueue(object : Callback<AuthMail> {
            override fun onResponse(@NonNull call: Call<AuthMail>, @NonNull response: Response<AuthMail>) {
                if (response.body() != null) {

                    if (response.body()!!.success) {

                        val resultListHeaders = response.headers().get("Set-Cookie")

                        /* Пример ответа от АПИ
                        set-cookie: sessionid=26jmvokos705ehtv7l2fe86fmuwem5n3; expires=Wed, 03 Apr 2019 09:33:23 GMT; Max-Age=1209600; Path=/
                        Нам нужно выделить из этой строки sessionid
                        На выходе получаем 26jmvokos705ehtv7l2fe86fmuwem5n3 */

                        val sessionID = resultListHeaders?.substringBefore(";")?.substringAfter("=")

                        val editor = sPrefAuthMailUser.edit()
                        editor?.putString(authMailSessionID, sessionID)
                        editor?.putString(authMailUser, getAuthMail())
                        editor?.putString(authMailPass, getAuthPass())
                        editor?.apply()

                        setUserAuthid(true)
                        setBtnUserLogged("mail")

                    } else if (!(response.body()!!.success)) {
                        Toast.makeText(context, "${response.body()!!.errors}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(@NonNull call: Call<AuthMail>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error onAuthMailUserClick!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onAuthPhoneUserChangeClick(): Boolean {
        val editor = sPrefAuthPhoneUser.edit()
        editor?.remove(authPhoneSessionID)
        editor?.remove(authPhoneUser)
        editor?.remove(authPhoneUserPassword)
        editor?.apply()

        setBtnUserLogged("")
        setUserAuthid(false)

        return false
    }

    fun onAuthPhoneUserClick() {

        val userData = UserPhoneAuth(getAuthPhone(), getAuthPhonePassword())

        Retrofit.api?.postAuthDataPhone("AuthPhoneUser", userData)?.enqueue(object : Callback<AuthPhone> {
            override fun onResponse(@NonNull call: Call<AuthPhone>, @NonNull response: Response<AuthPhone>) {
                if (response.body() != null) {

                    if (response.body()!!.success) {

                        val resultListHeaders = response.headers().get("Set-Cookie")

                        /* Пример ответа от АПИ
                        set-cookie: sessionid=26jmvokos705ehtv7l2fe86fmuwem5n3; expires=Wed, 03 Apr 2019 09:33:23 GMT; Max-Age=1209600; Path=/
                        Нам нужно выделить из этой строки sessionid
                        На выходе получаем 26jmvokos705ehtv7l2fe86fmuwem5n3 */

                        val sessionID = resultListHeaders?.substringBefore(";")?.substringAfter("=")

                        val editor = sPrefAuthPhoneUser.edit()
                        editor?.putString(authPhoneSessionID, sessionID)
                        editor?.putString(authPhoneUser, getAuthPhone())
                        editor?.putString(authPhoneUserPassword, getAuthPhonePassword())
                        editor?.apply()

                        setUserAuthid(true)
                        setBtnUserLogged("phone")

                    } else if (!(response.body()!!.success)) {
                        Toast.makeText(context, "${response.body()!!.errors}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(@NonNull call: Call<AuthPhone>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error onAuthPhoneUserClick!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onAuthVKChangeClick(): Boolean {
        setBtnUserLogged("")
        setUserAuthid(false)

        return false
    }

    fun onAuthVKClick(userLogin: String, provider: String, accessToken: String) {

        val contactFace = AuthVKJobni(
                userLogin,
                provider,
                accessToken
//              "ba352282246678fe6c7f60b6c7b23464640a34b3c0dffb9b9f9245e911ad2874a0831dba3d29849727d62"
        )

        Retrofit.api?.postVKAuth(contactFace)?.enqueue(object : Callback<AuthVK> {
            override fun onResponse(call: Call<AuthVK>, response: Response<AuthVK>) {
                if (response.body() != null) {

                    if (response.body()!!.success) {
                        setUserAuthid(true)
                        setBtnUserLogged("vk")

                    } else if (!(response.body()!!.success)) {
                        Toast.makeText(context, "${response.body()!!.errors}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<AuthVK>, t: Throwable) {
                Toast.makeText(context, "Error onAuthVKClick!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onAuthInstagramChangeClick(): Boolean {
        setBtnUserLogged("")
        setUserAuthid(false)

        return false
    }

    fun onAuthInstagramClick(accessToken: String, uid: String) {

        val provider = "instagram"
        val contactFace = AuthInstagramJobni(
                uid,
                provider,
                accessToken
        )

        Retrofit.api?.postInstagramAuth(contactFace)?.enqueue(object : Callback<AuthInstagram> {
            override fun onResponse(call: Call<AuthInstagram>, response: Response<AuthInstagram>) {
                if (response.body() != null) {

                    if (response.body()!!.success) {
                        setUserAuthid(true)
                        setBtnUserLogged("instagram")

                    } else if (!(response.body()!!.success)) {
                        Toast.makeText(context, "${response.body()!!.errors}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<AuthInstagram>, t: Throwable) {
                Toast.makeText(context, "Error onAuthInstagramClick!", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun convertInstagramCode(code: String) {

        Retrofit.api?.getInstagramAccessToken(
                context.resources.getString(R.string.instagram_client_id),
                context.resources.getString(R.string.instagram_client_secret),
                "authorization_code",
                context.resources.getString(R.string.instagram_redirect_url), code)?.enqueue(object : Callback<AuthInstagram> {
            override fun onResponse(call: Call<AuthInstagram>, response: Response<AuthInstagram>) {
                if (response.body() != null) {

                    val accessToken = response.body()?.access_token.toString()
                    val userID = response.body()?.user.toString()

                    onAuthInstagramClick(accessToken, userID)
                }
            }

            override fun onFailure(call: Call<AuthInstagram>, t: Throwable) {
                Toast.makeText(context, "Error convertInstagramCode!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onAuthDiscordChangeClick(): Boolean {
        setBtnUserLogged("")
        setUserAuthid(false)

        return false
    }

    fun onAuthDiscordClick(accessToken: String, uid: String) {

        val provider = "discord"
        val contactFace = AuthDiscordJobni(
                uid,
                provider,
                accessToken
        )

        Retrofit.api?.postDiscordAuth(contactFace)?.enqueue(object : Callback<AuthDiscord> {
            override fun onResponse(call: Call<AuthDiscord>, response: Response<AuthDiscord>) {
                if (response.body() != null) {

                    if (response.body()!!.success) {
                        setUserAuthid(true)
                        setBtnUserLogged("discord")

                    } else if (!(response.body()!!.success)) {
                        Toast.makeText(context, "${response.body()!!.errors}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<AuthDiscord>, t: Throwable) {
                Toast.makeText(context, "Error onAuthDiscordClick!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun convertDiscordCode(code: String) {

        Retrofit.api?.getDiscordAccessToken(
                context.resources.getString(R.string.discord_client_id),
                context.resources.getString(R.string.discord_client_secret),
                "authorization_code",
                code,
                context.resources.getString(R.string.discord_redirect_url),
                "identify")?.enqueue(object : Callback<AuthDiscord> {
            override fun onResponse(call: Call<AuthDiscord>, response: Response<AuthDiscord>) {
                if (response.body() != null) {

                    val dscAccessToken = response.body()?.access_token.toString()

                    getDiscordUID(dscAccessToken)
                }
            }

            override fun onFailure(call: Call<AuthDiscord>, t: Throwable) {
                Toast.makeText(context, "Error convertDiscordCode!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getDiscordUID(AccessToken: String) {

        Retrofit.api?.getDiscordUID("Bearer $AccessToken")?.enqueue(object : Callback<AuthDiscord> {
            override fun onResponse(call: Call<AuthDiscord>, response: Response<AuthDiscord>) {
                if (response.body() != null) {

                    val dscUserID = response.body()?.id.toString()

                    onAuthDiscordClick(AccessToken, dscUserID)
                }
            }

            override fun onFailure(call: Call<AuthDiscord>, t: Throwable) {
                Toast.makeText(context, "Error convertDiscordCode!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onAuthMailruChangeClick(): Boolean {
        setBtnUserLogged("")
        setUserAuthid(false)

        return false
    }

    fun onAuthMailruClick(accessToken: String, vid: String) {

        val provider = "mailru"
        val contactFace = AuthMailruJobni(
                vid,
                provider,
                accessToken
        )

        Retrofit.api?.postMailruAuth(contactFace)?.enqueue(object : Callback<AuthMailru> {
            override fun onResponse(call: Call<AuthMailru>, response: Response<AuthMailru>) {
                if (response.body() != null) {

                    if (response.body()!!.success) {
                        setUserAuthid(true)
                        setBtnUserLogged("mailru")

                    } else if (!(response.body()!!.success)) {
                        Toast.makeText(context, "${response.body()!!.errors}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<AuthMailru>, t: Throwable) {
                Toast.makeText(context, "Error onAuthMailruClick!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onAuthOKChangeClick(): Boolean {
        setBtnUserLogged("")
        setUserAuthid(false)

        return false
    }

    fun onAuthOKClick(accessToken: String, uid: String) {

        val provider = "odnoklassniki"
        val contactFace = AuthOKJobni(
                uid,
                provider,
                accessToken
        )

        Retrofit.api?.postOKAuth(contactFace)?.enqueue(object : Callback<AuthOK> {
            override fun onResponse(call: Call<AuthOK>, response: Response<AuthOK>) {
                if (response.body() != null) {

                    if (response.body()!!.success) {
                        setUserAuthid(true)
                        setBtnUserLogged("ok")

                    } else if (!(response.body()!!.success)) {
                        Toast.makeText(context, "${response.body()!!.errors}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<AuthOK>, t: Throwable) {
                Toast.makeText(context, "Error onAuthOKClick!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun convertOKCode(accessToken: String, sig: String) {

        Retrofit.api?.getUserDataOK(
                context.resources.getString(R.string.ok_public_key),
                "users.getCurrentUser",
                sig,
                accessToken)?.enqueue(object : Callback<AuthOK> {
            override fun onResponse(call: Call<AuthOK>, response: Response<AuthOK>) {
                if (response.body() != null) {

                    val okUserID = response.body()?.uid.toString()

                    onAuthOKClick(accessToken, okUserID)
                }
            }

            override fun onFailure(call: Call<AuthOK>, t: Throwable) {
                Toast.makeText(context, "Error convertOKCode!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

