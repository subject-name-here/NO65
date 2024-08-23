package com.unicorns.invisible.no65.view

import android.view.View
import android.view.animation.AlphaAnimation
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.util.getColorById
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import kotlinx.coroutines.delay


interface FadeDrawer {
    val activity: MainActivity
    val screen: View

    fun fadeFromWhite(duration: Long = 500L) = launchCoroutineOnMain {
        fade(R.color.white, 1.0f, duration)
        delay(duration)
        screen.visibility = View.GONE
    }

    fun fadeToWhite(duration: Long = 500L) = launchCoroutineOnMain {
        fade(R.color.white, 0.0f, duration)
        delay(duration)
    }

    fun fadeToBlack(duration: Long = 2500L) = launchCoroutineOnMain {
        fade(R.color.black, 0.0f, duration)
        delay(duration)
    }

    fun fadeToGrey(duration: Long = 2500L) = launchCoroutineOnMain {
        fade(R.color.grey, 0.0f, duration)
        delay(duration)
    }

    private fun fade(colorId: Int, startAlpha: Float, duration: Long) {
        screen.visibility = View.VISIBLE
        screen.setBackgroundColor(activity.getColorById(colorId))
        val animation = AlphaAnimation(startAlpha, 1.0f - startAlpha)
        animation.duration = duration
        screen.startAnimation(animation)
    }
}