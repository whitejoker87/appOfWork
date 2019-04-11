package ru.jobni.jobni.fragments.api.discord

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import ru.jobni.jobni.R

class AuthenticationDialogDiscordLogin(context: Context) : Dialog(context) {

    private val request_url: String = "https://discordapp.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.auth_dialog_discord_login)
        initializeWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView() {
        val webView = findViewById<WebView>(R.id.web_view_discord_login)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.loadUrl(request_url)
        webView.webViewClient = DiscordWebViewClient
    }

    private val DiscordWebViewClient = object : WebViewClient() {}
}