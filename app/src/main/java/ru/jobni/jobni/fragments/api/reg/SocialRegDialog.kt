package ru.jobni.jobni.fragments.api.reg

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_bottom_sheet_photo.*
import ru.jobni.jobni.viewmodel.RegViewModel
import java.net.MalformedURLException
import java.net.URL
import android.webkit.CookieSyncManager
import android.widget.ProgressBar
import ru.jobni.jobni.R


class SocialRegDialog(val contextIn: Context, val typeReg: String) : Dialog(contextIn) {

    var sPrefAuthUser = contextIn.getSharedPreferences("authUser", AppCompatActivity.MODE_PRIVATE)
    private val authUserSessionID = "userSessionID"

    private val regViewModel: RegViewModel by lazy {
        ViewModelProviders.of(contextIn as FragmentActivity).get(RegViewModel::class.java)
    }

    private lateinit var progressBar: ProgressBar
//    private lateinit var binding: RegDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = RegDialogBinding.inflate(LayoutInflater.from(context))
        //setContentView(binding.root)

        this.setContentView(R.layout.reg_dialog)

        progressBar = findViewById(R.id.reg_progress_bar)

        regViewModel.getSocialReg(typeReg)

        regViewModel.getUrlWebViewSocial().observe(contextIn as LifecycleOwner, Observer {
            initializeWebView(it)
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView(url: String) {
        val webView = findViewById<WebView>(ru.jobni.jobni.R.id.web_view_social_reg)
        //val webView = binding.webViewSocialReg
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
//        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.userAgentString = "Chrome/44.0.0.0 Mobile"
//        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY)
//        webView.setScrollbarFadingEnabled(false)
//        webView.setInitialScale(50)
        setCookie(webView, url)//направляем на установку куки и дальше
//        webView.loadUrl(url)
//        webView.webViewClient = SocRegWebViewClient
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

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            showProgressBar()
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

//            view.loadUrl("javascript:(function(){var m=document.createElement('META');" +
//                    " m.name='viewport'; m.content='width=device-width, user-scalable=yes';" +
//                    " document.body.appendChild(m);})()")
//            if (url.contains("?errors=")) {
//                Log.e("code", "ошибка при регистрации")
//                //dismiss()
//            }

            if (url.contains(contextIn.getString(ru.jobni.jobni.R.string.jobni_callback_url_for_social_network))) {
                if (url.contains(contextIn.getString(ru.jobni.jobni.R.string.social_network_reg_sucess))) {
                    Log.e("code", "social auth success111")
                    Toast.makeText(context, "Регистрация соцсети пройдена!", Toast.LENGTH_LONG).show()
                    if (!regViewModel.getResultReg2Success().value!!) /*regViewModel.setResultReg2Success(true)*/regViewModel.postPassword()//работает только для первой реги
                    // Закрыть окно при получении кода. Значит чел. прошел авторизацию.
                    view.clearCache(true)
                    view.clearHistory()
                    clearCookies(contextIn)
                    //dismiss()
                } else if (url.contains(contextIn.getString(ru.jobni.jobni.R.string.social_network_reg_already_exists))){
                    Log.e("code", "account already exists")
                    Toast.makeText(context, "Аккаунт привязан к другому полщовтаелю!", Toast.LENGTH_LONG).show()
                    view.clearCache(true)
                    view.clearHistory()
                    clearCookies(contextIn)
                    regViewModel.setResultReg1Success(false)
                    //dismiss()
                }
            }
            hideProgressBar()
        }
    }

    @Throws(MalformedURLException::class)
    fun setCookie(view: WebView, url: String) {

        val id = sPrefAuthUser.getString(authUserSessionID, null)
        val cid = String.format("%s%s", "sessionid=", id)

        val cookieManager = CookieManager.getInstance()



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies (view, true)
        } else cookieManager.setAcceptCookie (true)

        //асинхронно устанавиваем куки и продолжаем загрузку
        cookieManager.setCookie(url,cid) {
            if (it){
                view.loadUrl(url)
                view.webViewClient = SocRegWebViewClient
            }
        }
    }

    fun clearCookies(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        } else {
            val cookieSyncMngr = CookieSyncManager.createInstance(context)
            cookieSyncMngr.startSync()
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            cookieSyncMngr.stopSync()
            cookieSyncMngr.sync()
        }
    }

    private fun showProgressBar() {
        progressBar.animate().setDuration(200).alpha(1f).start()
    }

    private fun hideProgressBar() {
        progressBar.animate().setDuration(200).alpha(0f).start()
    }

}
