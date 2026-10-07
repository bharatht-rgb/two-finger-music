package com.example.twofingermusic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val button = Button(this).apply {
            text = "Enable Lock Screen Gesture"
            setOnClickListener {
                startService(Intent(this@MainActivity, ScreenListenerService::class.java))
                Toast.makeText(this@MainActivity, "Gesture Service Running", Toast.LENGTH_SHORT).show()
            }
        }
        setContentView(button)
    }
}
