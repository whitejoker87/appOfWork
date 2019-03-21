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
import ru.jobni.jobni.model.auth.UserAuth
import ru.jobni.jobni.utils.Retrofit


class AuthViewModel(application: Application) : AndroidViewModel(application) {

    val context = application

    private val authUserSessionID = "userSessionID"
    private val authUserName = "userName"
    private val authUserPass = "userPass"

    var sPrefAuthUser = application.getSharedPreferences("authUser", AppCompatActivity.MODE_PRIVATE)


    private val isBtnUserLoggedVisible = MutableLiveData<Boolean>(false)

    fun setBtnUserLoggedVisible(isVisible: Boolean) {
        isBtnUserLoggedVisible.value = isVisible
    }

    fun isBtnUserLoggedVisible(): MutableLiveData<Boolean> = isBtnUserLoggedVisible


    private val isAuthUser = MutableLiveData<Boolean>()

    fun setAuthUser(authKey: Boolean) {
        isAuthUser.value = authKey
    }

    fun isAuthUser(): MutableLiveData<Boolean> = isAuthUser


    private val authUser = MutableLiveData<String>()

    fun getAuthUser(): String? = authUser.value

    fun setAuthUser(query: String) {
        this.authUser.value = query
    }


    private val authPass = MutableLiveData<String>()

    fun getAuthPass(): String? = authPass.value

    fun setAuthPass(query: String) {
        this.authPass.value = query
    }


    fun onAuthUserLongClick(): Boolean {
        val editor = sPrefAuthUser.edit()
        editor?.remove(authUserSessionID)
        editor?.remove(authUserName)
        editor?.remove(authUserPass)
        editor?.apply()

        setBtnUserLoggedVisible(false)

        Toast.makeText(context, "AuthData deleted!", Toast.LENGTH_SHORT).show()

        return false
    }

    fun onAuthUserClick() {
        doAuthUserPost()
    }


    fun doAuthUserPost() {

        val userData = UserAuth(getAuthUser(), getAuthPass())

        Retrofit.api?.postAuthData("AuthUser", userData)?.enqueue(object : Callback<UserAuth> {
            override fun onResponse(@NonNull call: Call<UserAuth>, @NonNull response: Response<UserAuth>) {
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
                        setAuthUser(true)
                        setBtnUserLoggedVisible(true)
                    }
                }
            }

            override fun onFailure(@NonNull call: Call<UserAuth>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun doAuthUserGet() {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        Retrofit.api?.getAuthData(cid)?.enqueue(object : Callback<UserAuth> {
            override fun onResponse(@NonNull call: Call<UserAuth>, @NonNull response: Response<UserAuth>) {
                if (response.body() != null) {

                }
            }

            override fun onFailure(@NonNull call: Call<UserAuth>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

