package com.unicorns.invisible.no65.view.lands

import android.animation.ObjectAnimator
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.DeliveranceAdLayoutBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class DeliveranceAdAnimator(
    private val activity: MainActivity,
    private val adLayoutBinding: DeliveranceAdLayoutBinding
) {
    suspend fun animate() = suspendCoroutine { cont ->
        launchCoroutineOnMain {
            val header = adLayoutBinding.adHeader
            val headerAnimation = RotateAnimation(
                0f,
                360f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            ).apply {
                duration = 2000L
                repeatCount = Animation.INFINITE
                repeatMode = Animation.REVERSE
            }
            header.startAnimation(headerAnimation)


            val item5 = adLayoutBinding.adItem5
            val item5Animation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                -1f,
                Animation.RELATIVE_TO_SELF,
                1f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f,
            ).apply {
                duration = 3000L
                repeatCount = Animation.INFINITE
                repeatMode = Animation.REVERSE
            }
            item5.startAnimation(item5Animation)


            val progressBar = adLayoutBinding.adProgressBar
            progressBar.max = 100
            progressBar.progress = 100
            val animation = ObjectAnimator.ofInt(progressBar, "progress", 0)
            animation.duration = 15000L
            animation.interpolator = LinearInterpolator()
            animation.doOnEnd {
                cont.resume(Unit)
            }
            animation.start()
        }
    }
}