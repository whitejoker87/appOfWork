package ru.jobni.jobni.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.R
import ru.jobni.jobni.model.auth.mail.UserMailAuth
import ru.jobni.jobni.model.auth.phone.UserPhoneAuth
import ru.jobni.jobni.model.network.auth.AuthInstagram
import ru.jobni.jobni.model.network.auth.AuthMail
import ru.jobni.jobni.model.network.auth.AuthPhone
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

    /*кликабельность кнопки при навигации(изменяется внутри меню)*/
    private val isBtnMailNotClickable = MutableLiveData<Boolean>()

    private val isMailAuthid = MutableLiveData<Boolean>()
    private val authMail = MutableLiveData<String>()
    private val authPass = MutableLiveData<String>()


    /*кликабельность кнопки при навигации FB(изменяется внутри меню)*/
    private val isBtnFBNotClickable = MutableLiveData<Boolean>()

    private val isFBAuthid = MutableLiveData<Boolean>()
    private val authFBUser = MutableLiveData<String>()
    private val authFBPass = MutableLiveData<String>()


    /*кликабельность кнопки при навигации Google+(изменяется внутри меню)*/
    private val isBtnGoogleNotClickable = MutableLiveData<Boolean>()

    private val isGoogleAuthid = MutableLiveData<Boolean>()
    private val authGoogleUser = MutableLiveData<String>()
    private val authGooglePass = MutableLiveData<String>()


    /*кликабельность кнопки при навигации OK(изменяется внутри меню)*/
    private val isBtnOKNotClickable = MutableLiveData<Boolean>()

    private val isOKAuthid = MutableLiveData<Boolean>()
    private val authOKUser = MutableLiveData<String>()
    private val authOKPass = MutableLiveData<String>()


    /*кликабельность кнопки при навигации VK(изменяется внутри меню)*/
    private val isBtnVKNotClickable = MutableLiveData<Boolean>()

    private val isVKAuthid = MutableLiveData<Boolean>()
    private val authVKUser = MutableLiveData<String>()
    private val authVKPass = MutableLiveData<String>()


    /*кликабельность кнопки при навигации Phone(изменяется внутри меню)*/
    private val isBtnPhoneNotClickable = MutableLiveData<Boolean>()

    private val isPhoneAuthid = MutableLiveData<Boolean>()
    private val authPhone = MutableLiveData<String>("")
    private val authPhonePassword = MutableLiveData<String>("")


    /*кликабельность кнопки при навигации Instagram(изменяется внутри меню)*/
    private val isBtnInstagramNotClickable = MutableLiveData<Boolean>()

    private val isInstagramAuthid = MutableLiveData<Boolean>()


    /* Блок обычной авторизации */
    fun setBtnUserLogged(typeLogged: String) {
        btnUserLogged.value = typeLogged
    }

    fun getBtnUserLogged(): MutableLiveData<String> = btnUserLogged


    fun setBtnMailNotClickable(isVisible: Boolean) {
        isBtnMailNotClickable.value = isVisible
    }

    fun isBtnMailNotClickable(): MutableLiveData<Boolean> = isBtnMailNotClickable


    fun setMailAuthid(authKey: Boolean) {
        isMailAuthid.value = authKey
    }

    fun isMailAuthid(): MutableLiveData<Boolean> = isMailAuthid


    fun getAuthMail(): String? = authMail.value

    fun setAuthMail(query: String) {
        this.authMail.value = query
    }


    fun getAuthPass(): String? = authPass.value

    fun setAuthPass(query: String) {
        this.authPass.value = query
    }


    /* Блок facebook авторизации */
    fun setBtnFBNotClickable(isVisible: Boolean) {
        isBtnFBNotClickable.value = isVisible
    }

    fun isBtnFBNotClickable(): MutableLiveData<Boolean> = isBtnFBNotClickable


    fun setFBAuthid(authKey: Boolean) {
        isFBAuthid.value = authKey
    }

    fun isFBAuthid(): MutableLiveData<Boolean> = isFBAuthid


    fun getFBAuthUser(): String? = authFBUser.value

    fun setFBAuthUser(query: String) {
        this.authFBUser.value = query
    }


    fun getFBAuthPass(): String? = authFBPass.value

    fun setFBAuthPass(query: String) {
        this.authFBPass.value = query
    }


    /* Блок google+ авторизации */
    fun setBtnGoogleNotClickable(isVisible: Boolean) {
        isBtnGoogleNotClickable.value = isVisible
    }

    fun isBtnGoogleNotClickable(): MutableLiveData<Boolean> = isBtnGoogleNotClickable


    fun setGoogleAuthid(authKey: Boolean) {
        isGoogleAuthid.value = authKey
    }

    fun isGoogleAuthid(): MutableLiveData<Boolean> = isGoogleAuthid


    fun getGoogleAuthUser(): String? = authGoogleUser.value

    fun setGoogleAuthUser(query: String) {
        this.authGoogleUser.value = query
    }


    fun getGoogleAuthPass(): String? = authGooglePass.value

    fun setGoogleAuthPass(query: String) {
        this.authGooglePass.value = query
    }


    /* Блок OK авторизации */
    fun setBtnOKNotClickable(isVisible: Boolean) {
        isBtnOKNotClickable.value = isVisible
    }

    fun isBtnOKNotClickable(): MutableLiveData<Boolean> = isBtnOKNotClickable


    fun setOKAuthid(authKey: Boolean) {
        isOKAuthid.value = authKey
    }

    fun isOKAuthid(): MutableLiveData<Boolean> = isOKAuthid


    fun getOKAuthUser(): String? = authOKUser.value

    fun setOKAuthUser(query: String) {
        this.authOKUser.value = query
    }


    fun getOKAuthPass(): String? = authOKPass.value

    fun setOKAuthPass(query: String) {
        this.authOKPass.value = query
    }


    /* Блок VK авторизации */
    fun setBtnVKNotClickable(isVisible: Boolean) {
        isBtnVKNotClickable.value = isVisible
    }

    fun isBtnVKNotClickable(): MutableLiveData<Boolean> = isBtnVKNotClickable


    fun setVKAuthid(authKey: Boolean) {
        isVKAuthid.value = authKey
    }

    fun isVKAuthid(): MutableLiveData<Boolean> = isVKAuthid


    fun getVKAuthUser(): String? = authVKUser.value

    fun setVKAuthUser(query: String) {
        this.authVKUser.value = query
    }


    fun getVKAuthPass(): String? = authVKPass.value

    fun setVKAuthPass(query: String) {
        this.authVKPass.value = query
    }


    /* Блок Phone авторизации */
    fun setBtnPhoneNotClickable(isVisible: Boolean) {
        isBtnPhoneNotClickable.value = isVisible
    }

    fun isBtnPhoneNotClickable(): MutableLiveData<Boolean> = isBtnPhoneNotClickable


    fun setPhoneAuthid(authKey: Boolean) {
        isPhoneAuthid.value = authKey
    }

    fun isPhoneAuthid(): MutableLiveData<Boolean> = isPhoneAuthid


    fun getAuthPhone(): String? = authPhone.value

    fun setAuthPhone(query: String) {
        this.authPhone.value = query
    }


    fun getAuthPhonePassword(): String? = authPhonePassword.value

    fun setAuthPhonePassword(query: String) {
        this.authPhonePassword.value = query
    }


    /* Блок Instagram авторизации */
    fun setBtnInstagramNotClickable(isVisible: Boolean) {
        isBtnInstagramNotClickable.value = isVisible
    }

    fun isBtnInstagramNotClickable(): MutableLiveData<Boolean> = isBtnInstagramNotClickable


    fun setInstagramAuthid(authKey: Boolean) {
        isInstagramAuthid.value = authKey
    }

    fun isInstagramAuthid(): MutableLiveData<Boolean> = isInstagramAuthid


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
        setMailAuthid(false)
        setBtnPhoneNotClickable(false)

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

                        setMailAuthid(true)
                        setBtnUserLogged("mail")
                        setBtnPhoneNotClickable(true)

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
        setPhoneAuthid(false)

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

                        setPhoneAuthid(true)
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
        setVKAuthid(false)

        return false
    }

    fun onAuthVKClick() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        Retrofit.api?.postVKAuth(cid)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() != null) {

                    setVKAuthid(true)
                    setBtnUserLogged("vk")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onAuthInstagramChangeClick(): Boolean {
        setBtnUserLogged("")
        setInstagramAuthid(false)

        return false
    }

    fun onAuthInstagramClick() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        Retrofit.api?.postInstagramAuth(cid)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() != null) {

                    setInstagramAuthid(true)
                    setBtnUserLogged("instagram")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Error onAuthInstagramClick!", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun convertInstagramCode(code: String) {

        Retrofit.api?.getAccessToken(
                context.resources.getString(R.string.client_id),
                context.resources.getString(R.string.client_secret),
                "authorization_code",
                context.resources.getString(R.string.redirect_url), code)?.enqueue(object : Callback<AuthInstagram> {
            override fun onResponse(call: Call<AuthInstagram>, response: Response<AuthInstagram>) {
                if (response.body() != null) {

                    val access_token = response.body()?.access_token
                    val user = response.body()?.user

                    setInstagramAuthid(true)
                    setBtnUserLogged("instagram")
                }
            }

            override fun onFailure(call: Call<AuthInstagram>, t: Throwable) {
                Toast.makeText(context, "Error convertInstagramCode!", Toast.LENGTH_SHORT).show()
            }
        })
    }

}

