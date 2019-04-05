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
import ru.jobni.jobni.model.auth.UserAuth
import ru.jobni.jobni.utils.Retrofit


class AuthViewModel(application: Application) : AndroidViewModel(application) {

    val context = application

    private val authUserSessionID = "userSessionID"
    private val authUserName = "userName"
    private val authUserPass = "userPass"

    var sPrefAuthUser = application.getSharedPreferences("authUser", AppCompatActivity.MODE_PRIVATE)

    /*цвет кнопки при логине пользователя*/
    private val btnUserLogged = MutableLiveData<String>("")

    /*кликабельность кнопки при навигации(изменяется внутри меню)*/
    private val isBtnUserNotClickable = MutableLiveData<Boolean>()

    private val isUserAuthid = MutableLiveData<Boolean>()
    private val authUser = MutableLiveData<String>()
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


    /* Блок обычной авторизации */
    fun setBtnUserLogged(typeLogged: String) {
        btnUserLogged.value = typeLogged
    }

    fun getBtnUserLogged(): MutableLiveData<String> = btnUserLogged


    fun setBtnUserNotClickable(isVisible: Boolean) {
        isBtnUserNotClickable.value = isVisible
    }

    fun isBtnUserNotClickable(): MutableLiveData<Boolean> = isBtnUserNotClickable


    fun setUserAuthid(authKey: Boolean) {
        isUserAuthid.value = authKey
    }

    fun isUserAuthid(): MutableLiveData<Boolean> = isUserAuthid


    fun getAuthUser(): String? = authUser.value

    fun setAuthUser(query: String) {
        this.authUser.value = query
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


    fun onAuthUserChangeClick(): Boolean {
        val editor = sPrefAuthUser.edit()
        editor?.remove(authUserSessionID)
        editor?.remove(authUserName)
        editor?.remove(authUserPass)
        editor?.apply()

        setBtnUserLogged("")
        setUserAuthid(false)

        return false
    }

    fun onAuthUserClick() {
        doAuthUserPost()
    }


    fun doAuthUserPost() {

        val userData = UserAuth(getAuthUser(), getAuthPass())

        Retrofit.api?.postAuthData("AuthUser", userData)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(@NonNull call: Call<ResponseBody>, @NonNull response: Response<ResponseBody>) {
                if (response.body() != null) {

                    val resultListHeaders = response.headers().get("Set-Cookie")

                    /* Пример ответа от АПИ
                    set-cookie: sessionid=26jmvokos705ehtv7l2fe86fmuwem5n3; expires=Wed, 03 Apr 2019 09:33:23 GMT; Max-Age=1209600; Path=/
                    Нам нужно выделить из этой строки sessionid
                    На выходе получаем 26jmvokos705ehtv7l2fe86fmuwem5n3 */

                    val sessionID = resultListHeaders?.substringBefore(";")?.substringAfter("=")

                    val editor = sPrefAuthUser.edit()
                    editor?.putString(authUserSessionID, sessionID)
                    editor?.putString(authUserName, getAuthUser())
                    editor?.putString(authUserPass, getAuthPass())
                    editor?.apply()

                    if (sessionID != null) {
                        setUserAuthid(true)
                        setBtnUserLogged("mail")
                    }
                }
            }

            override fun onFailure(@NonNull call: Call<ResponseBody>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

