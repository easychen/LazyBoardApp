package com.pushdeer.reactdash

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

// import android.webkit.ServiceWorkerController

class MainActivity : AppCompatActivity() {

    private var targetUrl: String = "https://ftqq.com/"

    private lateinit var llReload: View
    private lateinit var etTargetUrl: EditText

    // private var mWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val swController = ServiceWorkerController.getInstance()
//
//        swController.serviceWorkerWebSettings.allowContentAccess = true
        setContentView(R.layout.activity_main)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        llReload = findViewById(R.id.ll_reload)
        etTargetUrl = findViewById(R.id.et_target_url)

        etTargetUrl.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE && v.id == etTargetUrl.id) {
                    reload(etTargetUrl.text.toString())
                    return true
                }
                return false
            }
        })

        val client = WebViewClient()
        with(webView) {
            webView.setWebViewClient(client)
            WebView.setWebContentsDebuggingEnabled(true);
            webView.settings.javaScriptEnabled = true;
            reload(targetUrl)
        }

    }

    // if you press Back button this code will work
    override fun onBackPressed() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            llReload.visibility = if (llReload.isVisible) View.INVISIBLE else View.VISIBLE
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun reload(url: String) {
        try {
            Uri.parse(url)
        } catch (e: Exception) {
            Toast.makeText(this,"Please enter a valid URL", Toast.LENGTH_SHORT).show()
        }
        etTargetUrl.setText(url)
        webView.loadUrl(url)
    }
}