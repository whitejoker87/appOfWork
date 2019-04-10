package ru.jobni.jobni.fragments.api.mailru

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import ru.jobni.jobni.R
import ru.jobni.jobni.fragments.api.instagram.AuthenticationListenerMailru

class AuthenticationDialogMailru(context: Context, private val listener: AuthenticationListenerMailru) : Dialog(context) {

    private val request_url: String = context.resources.getString(R.string.discord_base_url) +
            "oauth2/authorize" +
            "?response_type=code" +
            "&client_id=" + context.resources.getString(R.string.discord_client_id) +
            "&scope=identify" +
            "&redirect_uri=" + context.resources.getString(R.string.discord_redirect_url)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.auth_dialog_mailru)
        initializeWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView() {
        val webView = findViewById<WebView>(R.id.web_view_mailru)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.loadUrl(request_url)
        webView.webViewClient = DiscordWebViewClient
    }

    private val DiscordWebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            if (url.contains("?code=")) {
                var code = url
                code = code.substring(code.lastIndexOf("=") + 1)

                listener.onTokenReceived(code)
//                dismiss()

            } else if (url.contains("?error")) {
                Log.e("code", "getting error fetching code")
                dismiss()
            }
        }
    }
}
