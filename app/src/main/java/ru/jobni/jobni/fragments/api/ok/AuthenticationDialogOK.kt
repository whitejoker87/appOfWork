package ru.jobni.jobni.fragments.api.ok

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import ru.jobni.jobni.R

class AuthenticationDialogOK(context: Context, private val listener: AuthenticationListenerOK) : Dialog(context) {

    private val request_url: String = context.resources.getString(R.string.ok_base_url) +
            "oauth/authorize?" +
            "client_id=" + context.resources.getString(R.string.ok_client_id) +
            "&scope=VALUABLE_ACCESS" +
            "&response_type=token" +
            "&redirect_uri=" + context.resources.getString(R.string.ok_redirect_url) +
            "&layout=m"

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
        webView.webViewClient = MailruWebViewClient
    }

    private val MailruWebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            if (url.contains("#access_token=")) {
                val accessToken = url.substring(url.indexOf("access_token=") + 13, url.lastIndexOf("&session_secret_key"))
                val sessionSecretKey = url.substring(url.indexOf("key=") + 4, url.lastIndexOf("&expires_in"))

                listener.onTokenReceived(accessToken, sessionSecretKey)
//                dismiss()

            } else if (url.contains("?error")) {
                Log.e("access_token", "getting error fetching access_token")
                dismiss()
            }
        }
    }
}
