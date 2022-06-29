package com.starmakerinteractive.thevoic.fruit

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.webkit.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.starmakerinteractive.thevoic.databinding.FruitWebBinding
import com.starmakerinteractive.thevoic.di.UrlPrefKey
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FruitWeb : AppCompatActivity() {
    @UrlPrefKey
    @Inject
    lateinit var key: String
    private var messageAb: ValueCallback<Array<Uri?>>? = null
    private lateinit var binding: FruitWebBinding
    private val viewModel: FruitWebViewModel by viewModels()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FruitWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webView = binding.fruitWebView
        var url = requireNotNull(intent.getStringExtra(key))

        if (url.startsWith(INCORRECT_PREFIX)) {
            url = CORRECT_PREFIX + url.removePrefix(INCORRECT_PREFIX)
        } else if (!url.startsWith(CORRECT_PREFIX)) {
            url = CORRECT_PREFIX + url
        }

        webView.loadUrl(url)

        webView.webViewClient = FruitClient()
        webView.settings.javaScriptEnabled = true
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = false

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            //For Android API >= 21 (5.0 OS)
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                messageAb = filePathCallback
                selectImageIfNeed()
                return true
            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun onCreateWindow(
                view: WebView?, isDialog: Boolean,
                isUserGesture: Boolean, resultMsg: Message
            ): Boolean {
                val newWebView = WebView(applicationContext)
                newWebView.settings.javaScriptEnabled = true
                newWebView.webChromeClient = this
                newWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                newWebView.settings.domStorageEnabled = true
                newWebView.settings.setSupportMultipleWindows(true)
                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                return true
            }
        }
    }

    private fun selectImageIfNeed() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = IMAGE_MIME_TYPE
        startActivityForResult(
            Intent.createChooser(intent, IMAGE_CHOOSER_TITLE),
            RESULT_CODE
        )
    }

    private inner class FruitClient : WebViewClient() {
        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            if (errorCode == -2) {
                Toast.makeText(this@FruitWeb, "Error", Toast.LENGTH_LONG).show()
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            url?.let { viewModel.saveUrl(it) }
        }
    }

    companion object {
        private const val IMAGE_CHOOSER_TITLE = "Image Chooser"
        private const val IMAGE_MIME_TYPE = "image/*"

        private const val RESULT_CODE = 1

        private const val CORRECT_PREFIX = "https://"
        private const val INCORRECT_PREFIX = "http://"
    }
}