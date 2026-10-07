package com.example.twofingermusic

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LockGestureActivity : AppCompatActivity() {
    private lateinit var audioManager: AudioManager
    private var startY1 = 0f
    private var startY2 = 0f
    private var isTrackingTwoFingers = false
    private val swipeThreshold = 150f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock_gesture)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        findViewById<View>(R.id.touch_surface).setOnTouchListener { _, event ->
            handleMultiTouch(event)
        }
    }

    private fun handleMultiTouch(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    startY1 = event.getY(0)
                    startY2 = event.getY(1)
                    isTrackingTwoFingers = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isTrackingTwoFingers && event.pointerCount == 2) {
                    val diffY1 = event.getY(0) - startY1
                    val diffY2 = event.getY(1) - startY2
                    if (diffY1 > swipeThreshold && diffY2 > swipeThreshold) {
                        togglePlayPause()
                        isTrackingTwoFingers = false
                        finish()
                        return true
                    }
                }
            }
            MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isTrackingTwoFingers = false
            }
        }
        return true
    }

    private fun togglePlayPause() {
        val downEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
        val upEvent = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
        audioManager.dispatchMediaKeyEvent(downEvent)
        audioManager.dispatchMediaKeyEvent(upEvent)
        Toast.makeText(this, "Playback Toggled", Toast.LENGTH_SHORT).show()
    }
}
