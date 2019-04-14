package ru.jobni.jobni.fragments.api.auth

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
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

    private val requestUrl =
            String.format("%s%s%s", "https://dev.jobni.ru/api/accounts/", typeProvider, "/login/?process=login")

    // Данные для авторизации
    private val userAuthSessionID = "userSessionID"
    var sPrefUserAuth = _context.getSharedPreferences("userAuth", AppCompatActivity.MODE_PRIVATE)

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
        webView.loadUrl(url)
        webView.webViewClient = VKWebViewClient
    }

    private val VKWebViewClient = object : WebViewClient() {

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            // Нужно получить sessionid после успешной авторизации
            getCookie(url)

            if (url.contains(_context.getString(R.string.jobni_callback_url_for_social_network))) {
                // Закрыть окно если строка содержит адрес callback
                dismiss()

                // Выставить авторизацию, как успешную
                when {
                    typeProvider.contains("instagram") -> {
                        authViewModel.setUserAuthid(true)
                        authViewModel.setBtnUserLogged("instagram")
                    }
                    typeProvider.contains("vk") -> {
                        authViewModel.setUserAuthid(true)
                        authViewModel.setBtnUserLogged("vk")
                    }
                    typeProvider.contains("microsoft") -> {
                        authViewModel.setUserAuthid(true)
                        authViewModel.setBtnUserLogged("microsoft")
                    }
                    typeProvider.contains("odnoklassniki") -> {
                        authViewModel.setUserAuthid(true)
                        authViewModel.setBtnUserLogged("odnoklassniki")
                    }
                    typeProvider.contains("mailru") -> {
                        authViewModel.setUserAuthid(true)
                        authViewModel.setBtnUserLogged("mailru")
                    }
                    typeProvider.contains("google") -> {
                        authViewModel.setUserAuthid(true)
                        authViewModel.setBtnUserLogged("google")
                    }
                    typeProvider.contains("facebook") -> {
                        authViewModel.setUserAuthid(true)
                        authViewModel.setBtnUserLogged("facebook")
                    }
                    typeProvider.contains("discord") -> {
                        authViewModel.setUserAuthid(true)
                        authViewModel.setBtnUserLogged("discord")
                    }
                }
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
        sessionID = rawCookieHeader.substringAfter(";").substringAfter("=")

        // Запишем полученный sessionid
        val editor = sPrefUserAuth.edit()
        editor?.putString(userAuthSessionID, sessionID)
        editor?.apply()

        return rawCookieHeader
    }
}
