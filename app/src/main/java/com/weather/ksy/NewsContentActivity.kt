package com.weather.ksy

import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_news_content.*

class NewsContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_news_content)
        settingView()
    }

    private fun settingView(){
        news_content_webview.loadUrl(intent.getStringExtra("contentUrl"))
        news_content_webview.settings.javaScriptEnabled =true
        news_content_webview.webChromeClient = WebChromeClient()
        news_content_webview.webViewClient = WebViewClientClass()
        news_content_title_tv.text = intent.getStringExtra("contentTitle")
    }

    private inner class WebViewClientClass:WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Toast.makeText(this@NewsContentActivity,"Page loading",Toast.LENGTH_SHORT).show()
            view?.loadUrl(url)
            return true
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if((keyCode == KeyEvent.KEYCODE_BACK) && news_content_webview.canGoBack()){
            this.news_content_webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
