package ru.jobni.jobni.fragments.api.auth.instagram

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.viewmodel.AuthViewModel

class AuthDialogInstagram(private val _context: Context, val typeProvider: String, private val listenerInstagram: AuthListenerInstagram) : Dialog(_context) {

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
        webView.webViewClient = InstagramWebViewClient
    }

    private val InstagramWebViewClient = object : WebViewClient() {

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

            if (url.contains("?code=")) {
                // Выделить code из ответа.
                // Старая версия, нужно учитывать как его правильно вырезать из url
                //val code = url.substring(url.lastIndexOf("=") + 1)

                // Передать листнеру code для дальнейшей работы с ним если нужно
                //listenerInstagram.onTokenReceived(code)
                // Закрыть окно при получении кода. Значит чел. прошел авторизацию.
                dismiss()

                // Выставить авторизацию, как успешную
                authViewModel.setUserAuthid(true)
                authViewModel.setBtnUserLogged("instagram")

            } else if (url.contains("?error")) {
                Log.e("code", "getting error fetching code")
                dismiss()
            }
        }
    }
}
