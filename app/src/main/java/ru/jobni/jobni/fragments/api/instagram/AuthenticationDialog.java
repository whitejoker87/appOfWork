package ru.jobni.jobni.fragments.api.instagram;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import ru.jobni.jobni.R;

public class AuthenticationDialog extends Dialog {
    private final String request_url;
    private final String redirect_url;
    private AuthenticationListener listener;

    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener) {
        super(context);
        this.listener = listener;
        this.redirect_url = context.getResources().getString(R.string.redirect_url);
        this.request_url = context.getResources().getString(R.string.base_url) +
                "oauth/authorize/?client_id=" +
                context.getResources().getString(R.string.client_id) +
                "&redirect_uri=" + redirect_url +
                "&response_type=code";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialog);
        initializeWebView();
    }

    private void initializeWebView() {
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(request_url);
        webView.setWebViewClient(InstaWebViewClient);
    }

    private WebViewClient InstaWebViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (url.startsWith(redirect_url)) {
//                AuthenticationDialog.this.dismiss();
//                return true;
//            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Uri uri = Uri.parse(url);

            System.out.println("333 " + url);
            System.out.println("444 " + uri);

            if (url.contains("?code=")) {
                String access_token = url;
                access_token = access_token.substring(access_token.lastIndexOf("=") + 1);

                System.out.println("555 " + access_token);
                listener.onTokenReceived(access_token);
                //dismiss();

//                if (access_token != null) {
//                    access_token = access_token.substring(access_token.lastIndexOf("=") + 1);
//
//                    Log.e("access_token", access_token);
//                    listener.onTokenReceived(access_token);
//                    dismiss();
//
//                    System.out.println("111 " + access_token);
//                }

//                System.out.println("222 " + access_token);
            } else if (url.contains("?error")) {
                Log.e("code", "getting error fetching code");
                dismiss();
            }
        }
    };
}
