package com.example

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  // Android Native JS Interface for Professional Native Bridge
  class AndroidAppBridge(private val context: Context) {
    @JavascriptInterface
    fun vibrateBall() {
      vibrate(30)
    }

    @JavascriptInterface
    fun vibrateBoundary() {
      vibrate(90)
    }

    @JavascriptInterface
    fun vibrateWicket() {
      vibrate(180)
    }

    private fun vibrate(millis: Long) {
      try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
          val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
          vibratorManager?.defaultVibrator?.vibrate(
            VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)
          )
        } else {
          @Suppress("DEPRECATION")
          val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
          v?.vibrate(millis)
        }
      } catch (_: Exception) {}
    }

    @JavascriptInterface
    fun showNativeToast(message: String) {
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun shareMatchLink(text: String, url: String) {
      val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "$text\n$url")
        type = "text/plain"
      }
      val shareIntent = Intent.createChooser(sendIntent, "Share Match Link")
      shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      context.startActivity(shareIntent)
    }
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
              WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                  ViewGroup.LayoutParams.MATCH_PARENT,
                  ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.parseColor("#070a12"))
                setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)
                settings.apply {
                  javaScriptEnabled = true
                  domStorageEnabled = true
                  databaseEnabled = true
                  allowFileAccess = true
                  allowContentAccess = true
                  mediaPlaybackRequiresUserGesture = false
                  mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                  cacheMode = WebSettings.LOAD_DEFAULT
                  useWideViewPort = true
                  loadWithOverviewMode = true
                }
                addJavascriptInterface(AndroidAppBridge(context), "AndroidBridge")
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                loadUrl("file:///android_asset/index.html")
              }
            }
          )
        }
      }
    }
  }
}

