package ru.jobni.jobni.fragments.api.reg

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
import ru.jobni.jobni.viewmodel.RegViewModel


class SocialRegDialog(val contextIn: Context, val typeReg: String) : Dialog(contextIn) {

    private val regViewModel: RegViewModel by lazy {
        ViewModelProviders.of(contextIn as FragmentActivity).get(RegViewModel::class.java)
    }

//    private lateinit var binding: RegDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = RegDialogBinding.inflate(LayoutInflater.from(context))
        //setContentView(binding.root)

        this.setContentView(R.layout.reg_dialog)

        regViewModel.getSocialReg(typeReg)

        regViewModel.getUrlWebViewSocial().observe(contextIn as LifecycleOwner, Observer {
            initializeWebView(it)
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView(url: String) {
        val webView = findViewById<WebView>(R.id.web_view_social_reg)
        //val webView = binding.webViewSocialReg
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings
        webView.loadUrl(url)
        webView.webViewClient = SocRegWebViewClient
    }

    private val SocRegWebViewClient = object : WebViewClient() {

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

            if (url.contains("?code=") || url.contains("&code=")) {
                // Выделить code из ответа.
                // Старая версия, нужно учитывать как его правильно вырезать из url
                //val code = url.substring(url.lastIndexOf("=") + 1)

                // Передать листнеру для дальнейшей работы с ним если нужно
                //listenerVK.onTokenReceived(code)
                // Закрыть окно при получении кода. Значит чел. прошел авторизацию.
                dismiss()

            } else if (url.contains("?error")) {
                Log.e("code", "getting error fetching code")
                dismiss()
            }
        }
    }
}
