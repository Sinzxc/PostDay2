package com.example.postmachine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import java.net.URL

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        findViewById<Button>(R.id.button3).setOnClickListener{
            finish()
        }
        findViewById<Button>(R.id.button2).setOnClickListener{
            findViewById<WebView>(R.id.web1).loadUrl("https://ru.wikipedia.org/wiki/Машина_Поста")
        }
    }
}