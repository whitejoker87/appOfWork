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
import ru.jobni.jobni.model.auth.mail.UserMailAuth
import ru.jobni.jobni.model.auth.phone.UserPhoneAuth
import ru.jobni.jobni.model.network.auth.AuthMail
import ru.jobni.jobni.model.network.auth.AuthPhone
import ru.jobni.jobni.model.network.auth.AuthSocial
import ru.jobni.jobni.utils.Retrofit


class AuthViewModel(application: Application) : AndroidViewModel(application) {

    val context = application

    // Данные при авторизации, читаем здесь для sessionID
    private val userAuthSessionID = "userSessionID"

    var sPrefUserAuth = application.getSharedPreferences("userAuth", AppCompatActivity.MODE_PRIVATE)

    /*общие значения при логине пользователя*/
    // Цвет кнопки
    private val btnUserLogged = MutableLiveData<String>("")
    // Статус пользователя - ЗАЛОГИНИЛСЯ
    private val isUserAuthid = MutableLiveData<Boolean>()
    // Получения ссылки для социальных аккаунтов
    private val urlWebViewSocial = MutableLiveData<String>()

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


    fun setUrlWebViewSocial(url: String) {
        urlWebViewSocial.value = url
    }

    fun getUrlWebViewSocial(): MutableLiveData<String> = urlWebViewSocial


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


    private val socialAccDeleted = MutableLiveData<Boolean>()

    fun isSocialAccDeleted(): MutableLiveData<Boolean> = socialAccDeleted

    fun setSocialAccDeleted(isStart: Boolean) {
        socialAccDeleted.value = isStart
    }

    fun onAuthUserChangeClick(): Boolean {

        onGetSocialAccList()

        if (socialAccDeleted.value == true) {
            val editor = sPrefUserAuth.edit()
            editor?.remove(userAuthSessionID)
            editor?.apply()
        }

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

                        val editor = sPrefUserAuth.edit()
                        editor?.putString(userAuthSessionID, sessionID)
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

                        val editor = sPrefUserAuth.edit()
                        editor?.putString(userAuthSessionID, sessionID)
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

    fun onGetSocialAccList() {

        val id = sPrefUserAuth.getString(userAuthSessionID, null)
        val sessionID = String.format("%s%s", "sessionid=", id)

        Retrofit.api?.getSocialAccList(sessionID)?.enqueue(object : Callback<AuthSocial> {
            override fun onResponse(call: Call<AuthSocial>, response: Response<AuthSocial>) {
                if (response.body() != null) {

                    if (response.body()!!.success) {
                        // Массив при ответе должен содержать одну запись, так как
                        // чел. может быть одновременно авторизован одной учетной записью.
                        // Поэтому забираем нулевое значение - 0
                        if (response.body()!!.results.isNotEmpty()) {
                            val uid = response.body()!!.results[0].id
                            onDeleteSocialAcc(uid)
                        }

                    } else if (!(response.body()!!.success)) {
                        Toast.makeText(context, "${response.body()!!.detail}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<AuthSocial>, t: Throwable) {
                Toast.makeText(context, "Error onGetAuthSocial!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onDeleteSocialAcc(socialAccID: Int) {

        val id = sPrefUserAuth.getString(userAuthSessionID, null)
        val sessionID = String.format("%s%s", "sessionid=", id)

        Retrofit.api?.deleteSocialAcc(sessionID, socialAccID)?.enqueue(object : Callback<AuthSocial> {
            override fun onResponse(call: Call<AuthSocial>, response: Response<AuthSocial>) {
                if (response.body() != null) {

                    if (response.body()!!.success) {
                        Toast.makeText(context, "Аккаунт отвязан!", Toast.LENGTH_SHORT).show()
                        setSocialAccDeleted(true)
                    } else if (!(response.body()!!.success)) {
                        Toast.makeText(context, "${response.body()!!.detail}", Toast.LENGTH_LONG).show()
                        setSocialAccDeleted(true)
                    }
                }
            }

            override fun onFailure(call: Call<AuthSocial>, t: Throwable) {
                Toast.makeText(context, "Error onDeleteSocialAcc!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

