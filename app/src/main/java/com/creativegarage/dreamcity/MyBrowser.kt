package com.creativegarage.dreamcity

import android.webkit.WebView
import android.webkit.WebViewClient

class MyBrowser() : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        //  progressBar.visibility = View.VISIBLE
        view.loadUrl(url)
        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url)
        //progressBar.visibility = View.GONE
    }
}