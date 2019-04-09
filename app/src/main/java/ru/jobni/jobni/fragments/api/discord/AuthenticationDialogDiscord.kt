package ru.jobni.jobni.fragments.api.discord

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import ru.jobni.jobni.R
import ru.jobni.jobni.fragments.api.instagram.AuthenticationListenerDiscord

class AuthenticationDialogDiscord(context: Context, private val listener: AuthenticationListenerDiscord) : Dialog(context) {

    private val request_url: String = context.resources.getString(R.string.discord_base_url) +
            "oauth2/authorize" +
            "?response_type=code" +
            "&client_id=" + context.resources.getString(R.string.discord_client_id) +
            "&scope=identify" +
            //"&state=24569016ghq5428habn" +
            "&redirect_uri=" + context.resources.getString(R.string.discord_redirect_url)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.auth_dialog_discord)
        initializeWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView() {
        val webView = findViewById<WebView>(R.id.web_view_discord)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(request_url)
        webView.webViewClient = DiscordWebViewClient

//        val address = Uri.parse(request_url)
//        val openLinkIntent = Intent(Intent.ACTION_VIEW, address)
//
//        if (openLinkIntent.resolveActivity(context.packageManager) != null) {
//            startActivity(context, openLinkIntent, null)
//        } else {
//            Log.d("Intent", "Не получается обработать намерение!")
//        }
//
//        val intent = getIntent(address.toString())
//        val action = intent.action
//        val data = intent.data
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

                println("111 " + code)
                listener.onTokenReceived(code)
//                dismiss()

            } else if (url.contains("?error")) {
                Log.e("code", "getting error fetching code")
                dismiss()
            }
        }
    }
}
