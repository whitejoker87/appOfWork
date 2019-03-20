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
import ru.jobni.jobni.model.auth.UserAuthMail
import ru.jobni.jobni.utils.Retrofit


class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authMailSessionID = "mailSessionID"
    private val authMailUser = "mailUser"
    private val authMailPass = "mailPass"

    var sPrefAuthMail = application.getSharedPreferences("authMail", AppCompatActivity.MODE_PRIVATE)

    val context = application

    private val isAuthMail = MutableLiveData<Boolean>()

    fun setAuthMail(authKey: Boolean) {
        isAuthMail.value = authKey
    }

    fun isAuthMail(): MutableLiveData<Boolean> = isAuthMail


    private val authEmail = MutableLiveData<String>()

    fun getAuthEmail(): String? = authEmail.value

    fun setAuthEmail(query: String) {
        this.authEmail.value = query
    }


    private val authPass = MutableLiveData<String>()

    fun getAuthPass(): String? = authPass.value

    fun setAuthPass(query: String) {
        this.authPass.value = query
    }


    fun onAuthMailClick() {
        doAuthMailPost()
    }

    fun doAuthMailPost() {

        val userData = UserAuthMail(getAuthEmail(), getAuthPass())

        Retrofit.api?.postAuthData("AuthMail", userData)?.enqueue(object : Callback<UserAuthMail> {
            override fun onResponse(@NonNull call: Call<UserAuthMail>, @NonNull response: Response<UserAuthMail>) {
                if (response.body() != null) {

                    val resultListHeaders = response.headers().get("Set-Cookie")

                    /* Пример ответа от АПИ
                    set-cookie: sessionid=26jmvokos705ehtv7l2fe86fmuwem5n3; expires=Wed, 03 Apr 2019 09:33:23 GMT; Max-Age=1209600; Path=/
                    Нам нужно выделить из этой строки sessionid
                    На выходе получаем 26jmvokos705ehtv7l2fe86fmuwem5n3 */

                    val sessionID = resultListHeaders?.substringBefore(";")?.substringAfter("=")

                    val editor = sPrefAuthMail.edit()
                    editor?.putString(authMailSessionID, sessionID)
                    editor?.putString(authMailUser, getAuthEmail())
                    editor?.putString(authMailPass, getAuthPass())
                    editor?.apply()

                    if (sessionID != null) {
                        setAuthMail(true)
                    }
                }
            }

            override fun onFailure(@NonNull call: Call<UserAuthMail>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun doAuthMailGet() {

        val id = sPrefAuthMail.getString(authMailSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        Retrofit.api?.getAuthData(cid)?.enqueue(object : Callback<UserAuthMail> {
            override fun onResponse(@NonNull call: Call<UserAuthMail>, @NonNull response: Response<UserAuthMail>) {
                if (response.body() != null) {

                }
            }

            override fun onFailure(@NonNull call: Call<UserAuthMail>, @NonNull t: Throwable) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

