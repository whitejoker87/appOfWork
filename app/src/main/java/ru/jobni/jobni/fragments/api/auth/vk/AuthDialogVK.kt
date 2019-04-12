package ru.jobni.jobni.fragments.api.auth.vk

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.viewmodel.AuthViewModel
import java.net.MalformedURLException
import java.net.URL

class AuthDialogVK(private val _context: Context, val typeProvider: String, private val listenerVK: AuthListenerVK) : Dialog(_context) {

    // Данные при авторизации, читаем здесь для sessionID
    private val authUserSessionID = "userSessionID"

    var sPrefAuthUser = _context.getSharedPreferences("authUser", AppCompatActivity.MODE_PRIVATE)

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProviders.of(_context as FragmentActivity).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.auth_dialog_social)

        authViewModel.onGetAuthSocial(typeProvider)

        authViewModel.getUrlWebViewSocial().observe(_context as LifecycleOwner, Observer {
            initializeWebView(it)
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView(url: String) {
        val webView = findViewById<WebView>(R.id.web_view_social)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.setInitialScale(50)
        webView.loadUrl(url)
        webView.webViewClient = VKWebViewClient
    }

    private val VKWebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // Здесь можно разместить блок кода
            // с проверкой при переходе на указаный адрес, что то делать
            // Например закрывать окно WebView
            /*if (url.startsWith(request_url)) {
                dismiss()
                return true
            }*/
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            getCookie(url)

            if (url.contains("?code=")) {
                // Выделить code из ответа.
                // Старая версия, нужно учитывать как его правильно вырезать из url
                //val code = url.substring(url.lastIndexOf("=") + 1)

                // Передать листнеру для дальнейшей работы с ним если нужно
                //listenerVK.onTokenReceived(code)
                // Закрыть окно при получении кода. Значит чел. прошел авторизацию.
                dismiss()

                // Выставить авторизацию, как успешную
                authViewModel.setUserAuthid(true)
                authViewModel.setBtnUserLogged("vk")


            } else if (url.contains("?error")) {
                Log.e("code", "getting error fetching code")
                dismiss()
            }
        }

        // Нужно получить sessionid после успешной авторизации
        @Throws(MalformedURLException::class)
        fun getCookie(url: String): String? {

            val cookieManager = CookieManager.getInstance() ?: return null

            var rawCookieHeader: String? = null
            val parsedURL = URL(url)
            // Полученный ответ от CookieManager примерно такой - sessionid=do9futqubj58c08tu96qvkz4le4x5wap
            // Вырежим sessionid отдельно и получим - do9futqubj58c08tu96qvkz4le4x5wap
            rawCookieHeader = cookieManager.getCookie(parsedURL.host) //.substringBefore(";").substringAfter("=")

            if (rawCookieHeader == null)
                return null

            // Запишем полученный sessionid
            val editor = sPrefAuthUser.edit()
            editor?.putString(authUserSessionID, rawCookieHeader)
            editor?.apply()

            return rawCookieHeader
        }
    }
}
