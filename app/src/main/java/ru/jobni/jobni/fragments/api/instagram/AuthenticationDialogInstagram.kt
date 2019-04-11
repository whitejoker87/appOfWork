package ru.jobni.jobni.fragments.api.instagram

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import ru.jobni.jobni.R

class AuthenticationDialogInstagram(context: Context, private val listenerInstagram: AuthenticationListenerInstagram) : Dialog(context) {

    private val request_url: String = context.resources.getString(R.string.instagram_base_url) +
            "oauth/authorize/" +
            "?client_id=" + context.resources.getString(R.string.instagram_client_id) +
            "&redirect_uri=" + context.resources.getString(R.string.instagram_redirect_url) +
            "&scope=basic" +
            "&response_type=code"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.auth_dialog_instagram)
        initializeWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView() {
        val webView = findViewById<WebView>(R.id.web_view_instagram)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.loadUrl(request_url)
        webView.webViewClient = InstaWebViewClient
    }

    private val InstaWebViewClient = object : WebViewClient() {

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
                // Выделить code из ответа
                //val code = url.substring(url.lastIndexOf("=") + 1)

                // Передать листнеру для дальнейшей работы с ним если нужно
                //listenerInstagram.onTokenReceived(code)
                // Закрыть окно при получении кода. Значит чел. прошел авторизацию.
                dismiss()

            } else if (url.contains("?error")) {
                Log.e("code", "getting error fetching code")
                dismiss()
            }
        }
    }
}
