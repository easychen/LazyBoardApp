package com.pushdeer.reactdash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

// import android.webkit.ServiceWorkerController

class MainActivity : AppCompatActivity() {

    companion object {
        const val SCAN_REQUEST = 1
    }

    private var targetUrl: String = "https://ftqq.com/"

    private lateinit var llReload: View
    private lateinit var etTargetUrl: EditText
    private lateinit var pbLoad: ProgressBar

    // private var mWebView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val swController = ServiceWorkerController.getInstance()
//
//        swController.serviceWorkerWebSettings.allowContentAccess = true
        setContentView(R.layout.activity_main)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        llReload = findViewById(R.id.ll_reload)
        etTargetUrl = findViewById(R.id.et_target_url)
        pbLoad = findViewById(R.id.pb_load)
        findViewById<View>(R.id.imv_scanner).setOnClickListener{
            val intent = Intent(this, ScanActivity::class.java)
            startActivityForResult(intent, SCAN_REQUEST)
        }

        etTargetUrl.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE && v.id == etTargetUrl.id) {
                    reload(etTargetUrl.text.toString())
                    val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(etTargetUrl.windowToken, 0)
                    return true
                }
                return false
            }
        })

        webView.apply {
            webViewClient = WebViewClient()
            WebView.setWebContentsDebuggingEnabled(true)
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setDatabaseEnabled(true);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                webView.getSettings().setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
            }

            settings.javaScriptEnabled = true

            webChromeClient = object :WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    when (progress) {
                      100 -> pbLoad.visibility = View.GONE
                      else -> {
                          if (pbLoad.visibility == View.GONE) pbLoad.visibility = View.VISIBLE
                          pbLoad.progress = newProgress
                      }
                    }
                }
            }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SCAN_REQUEST) {
            val url = data?.getStringExtra("code") ?:""
            reload(url)
        }
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