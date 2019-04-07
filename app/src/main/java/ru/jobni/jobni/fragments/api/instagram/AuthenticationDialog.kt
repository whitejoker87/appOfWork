package ru.jobni.jobni.fragments.api.instagram

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import ru.jobni.jobni.R

class AuthenticationDialog(context: Context, private val listener: AuthenticationListener) : Dialog(context) {
    private val request_url: String
    private val redirect_url: String = context.resources.getString(R.string.redirect_url)

    init {
        this.request_url = context.resources.getString(R.string.base_url) +
                "oauth/authorize/?client_id=" +
                context.resources.getString(R.string.client_id) +
                "&redirect_uri=" + redirect_url +
                "&response_type=code"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.auth_dialog)
        initializeWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView() {
        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(request_url)
        webView.webViewClient = InstaWebViewClient
    }

    private val InstaWebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(redirect_url)) {
                dismiss()
                return true
            }
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            if (url.contains("?code=")) {
                var code = url
                code = code.substring(code.lastIndexOf("=") + 1)

                listener.onTokenReceived(code)
                dismiss()

            } else if (url.contains("?error")) {
                Log.e("code", "getting error fetching code")
                dismiss()
            }
        }
    }
}
