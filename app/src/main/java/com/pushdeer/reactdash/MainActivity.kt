package com.pushdeer.reactdash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
// import android.webkit.ServiceWorkerController




class MainActivity : AppCompatActivity() {
    // private var mWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val swController = ServiceWorkerController.getInstance()
//
//        swController.serviceWorkerWebSettings.allowContentAccess = true
        setContentView(R.layout.activity_main)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        val client = WebViewClient()
        with(webView) {
            webView.setWebViewClient(client)
            WebView.setWebContentsDebuggingEnabled(true);
            webView.settings.javaScriptEnabled = true;
            webView.loadUrl("https://ftqq.com/")
        }

    }

    // if you press Back button this code will work
    override fun onBackPressed() {

    }
}