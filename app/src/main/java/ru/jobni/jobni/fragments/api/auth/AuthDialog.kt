package ru.jobni.jobni.fragments.api.auth

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.viewmodel.AuthViewModel
import java.net.MalformedURLException
import java.net.URL

class AuthDialog(
        private val _context: Context,
        private val typeProvider: String
) : Dialog(_context) {

    private val requestUrl = String.format("%s%s%s", "https://dev.jobni.ru/api/accounts/", typeProvider, "/login/?process=login")
    // Ключ для обработки url
    // Если url уже содержит строку callback не прерывать процесс.
    // Выполнять процесс только когда sessionid получен
    private var checkSessionID: Boolean? = false

    // Данные для авторизации SharedPreferences.
    // Название полей соответствуют AuthViewModel
    // Так как нам нужно именно эти данные прочитать позже
    private val userAuthSessionID = "userSessionID"
    private val userAuthBtnProvider = "userBtnProvider"

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProviders.of(_context as FragmentActivity).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.auth_dialog_social)

        initializeWebView(requestUrl)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView(url: String) {
        val webView = findViewById<WebView>(R.id.web_view_social)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.userAgentString = "Chrome/44.0.0.0 Mobile"
        webView.loadUrl(url)
        webView.webViewClient = VKWebViewClient
    }

    private val VKWebViewClient = object : WebViewClient() {

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            // Нужно получить sessionid после успешной авторизации
            getCookie(url)

            if (url.contains(_context.getString(R.string.jobni_callback_url_for_social_network))) {
                if (checkSessionID == true) {
                    //запишем данные для кнопки если SessionID получен
                    val editor = authViewModel.sPrefUserAuth.edit()
                    editor?.putString(userAuthBtnProvider, typeProvider)
                    editor?.apply()
                    //Выставить авторизацию, как успешную
                    authViewModel.setUserAuthid(true)
                    authViewModel.setBtnUserLogged(typeProvider)
                    // Закрыть окно если строка содержит адрес callback
                    dismiss()
                }
            } else if (url.contains("?errors=")) {
                Log.e("?errors=", "getting error fetching auth")
            }
        }
    }

    @Throws(MalformedURLException::class)
    fun getCookie(url: String): String? {

        val cookieManager = CookieManager.getInstance() ?: return null

        var rawCookieHeader: String? = null
        var sessionID: String? = null
        val parsedURL = URL(url)
        rawCookieHeader = cookieManager.getCookie(parsedURL.host)

        if (rawCookieHeader == null)
            return null

        // Полученный ответ от CookieManager.
        // Вырежим sessionid и получим примерно - do9futqubj58c08tu96qvkz4le4x5wap
        if (rawCookieHeader.contains("sessionid=")) {
            sessionID = rawCookieHeader.substringAfter(";").substringAfter("=")

            // Запишем полученный sessionid
            val editor = authViewModel.sPrefUserAuth.edit()
            editor?.putString(userAuthSessionID, sessionID)
            editor?.apply()

            // sessionid получен, поэтому ключ в режим TRUE
            checkSessionID = true
        }

        return rawCookieHeader
    }
}
