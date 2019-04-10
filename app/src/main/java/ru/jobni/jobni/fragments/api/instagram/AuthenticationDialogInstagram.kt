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

    private val request_url: String = context.resources.getString(R.string.base_url) +
            "oauth/authorize/?client_id=" +
            context.resources.getString(R.string.client_id) +
            "&redirect_uri=" +
            context.resources.getString(R.string.redirect_url) +
            "&response_type=code" +
            "&display=touch&scope=public_content"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.auth_dialog_instagram)
        initializeWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView() {
        val webView = findViewById<WebView>(R.id.web_view_instagram)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(request_url)
        webView.webViewClient = InstaWebViewClient
    }

    private val InstaWebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            if (url.contains("?code=")) {
                var code = url
                code = code.substring(code.lastIndexOf("=") + 1)

                listenerInstagram.onTokenReceived(code)
                dismiss()

            } else if (url.contains("?error")) {
                Log.e("code", "getting error fetching code")
                dismiss()
            }
        }
    }
}
