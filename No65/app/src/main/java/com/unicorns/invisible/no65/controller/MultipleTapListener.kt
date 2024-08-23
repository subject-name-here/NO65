package com.unicorns.invisible.no65.controller

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

abstract class MultipleTapListener(ctx: Context?) : View.OnTouchListener {
    private var numberOfTaps = 0
    private var lastTouchUpTime: Long = 0
    private var lastTouchDownTime: Long = 0

    private val tapListener = GestureDetector(ctx, TapListener())

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        return tapListener.onTouchEvent(event)
    }

    private inner class TapListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            val newTouchDownTime = System.currentTimeMillis()
            if (numberOfTaps > 0 && newTouchDownTime - lastTouchUpTime < TAP_TIMEOUT) {
                numberOfTaps += 1
            } else {
                numberOfTaps = 1
            }
            lastTouchDownTime = newTouchDownTime
            onMultipleTap(e, numberOfTaps)

            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            lastTouchUpTime = System.currentTimeMillis()
            if (lastTouchUpTime - lastTouchDownTime > TAP_TIMEOUT) {
                numberOfTaps = 0
            }

            return true
        }
    }

    abstract fun onMultipleTap(event: MotionEvent, numberOfTaps: Int)

    companion object {
        const val TAP_TIMEOUT = 333L
        // MUR-DER MUR-MAID MUR-DER
    }
}