package com.unicorns.invisible.no65.view.battlefield

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.*
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class BattleFieldAnimator(private val activity: MainActivity) {
    fun animateEvasion(cellsLayout: ConstraintLayout) {
        val evasionAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT,
            0.0f,
            Animation.RELATIVE_TO_PARENT,
            0.15f,
            Animation.RELATIVE_TO_PARENT,
            0.0f,
            Animation.RELATIVE_TO_PARENT,
            0.0f,
        ).apply {
            duration = 450L
            repeatCount = 1
            repeatMode = Animation.REVERSE
        }

        launchCoroutineOnMain {
            cellsLayout.startAnimation(evasionAnimation)
        }
    }

    private var enemyAnimation: Animation = EMPTY_ANIMATION
    fun animateEnemy(animation: CharacterAnimation, enemyCellsLayout: ConstraintLayout) {
        enemyAnimation = getAnimationFromCharacterAnimation(animation)
        enemyAnimation.repeatCount = Animation.INFINITE
        enemyAnimation.repeatMode = Animation.REVERSE

        launchCoroutineOnMain {
            enemyCellsLayout.startAnimation(enemyAnimation)
        }
    }

    fun stopEnemyAnimation(enemyCellsLayout: ConstraintLayout) = launchCoroutineOnMain {
        enemyCellsLayout.clearAnimation()
    }
    fun pauseEnemyAnimation(enemyCellsLayout: ConstraintLayout) = stopEnemyAnimation(enemyCellsLayout)

    fun resumeEnemyAnimation(enemyCellsLayout: ConstraintLayout) = launchCoroutineOnMain {
        enemyAnimation.reset()
        enemyCellsLayout.startAnimation(enemyAnimation)
    }

    private var protagonistAnimation: Animation = EMPTY_ANIMATION
    fun animateProtagonist(animation: CharacterAnimation, protagonistCellsLayout: ConstraintLayout) {
        protagonistAnimation = getAnimationFromCharacterAnimation(animation)
        protagonistAnimation.repeatCount = Animation.INFINITE
        protagonistAnimation.repeatMode = Animation.REVERSE

        launchCoroutineOnMain {
            protagonistCellsLayout.startAnimation(protagonistAnimation)
        }
    }
    fun stopProtagonistAnimation(protagonistCellsLayout: ConstraintLayout) = launchCoroutineOnMain {
        protagonistCellsLayout.clearAnimation()
    }

    private fun getAnimation(animId: Int): Animation {
        return AnimationUtils.loadAnimation(activity, animId)
    }
    private fun getAnimationFromCharacterAnimation(characterAnimation: CharacterAnimation): Animation {
        return when (characterAnimation) {
            CharacterAnimation.LITTLE_SWING -> {
                RotateAnimation(
                    -4f,
                    4f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                ).also { it.duration = 450L }
            }
            CharacterAnimation.DRUNK_SWING -> {
                RotateAnimation(
                    -45f,
                    30f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                ).also { it.duration = 1350L }
            }
            CharacterAnimation.ROTATE_FULL -> {
                RotateAnimation(
                    0f,
                    360f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                ).also { it.duration = 900L }
            }
            CharacterAnimation.FLOAT -> {
                getAnimation(R.anim.floating)
            }
            CharacterAnimation.FLOAT_D99 -> {
                getAnimation(R.anim.floating_d99)
            }
            CharacterAnimation.FLOAT_HORIZONTAL -> {
                getAnimation(R.anim.floating_horizontal)
            }
            CharacterAnimation.NONE -> EMPTY_ANIMATION
        }
    }

    private fun getAnimationWithCallback(animId: Int, onAnimationEndCustom: (Animation?) -> Unit): Animation {
        val animation = AnimationUtils.loadAnimation(activity, animId)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                onAnimationEndCustom(p0)
            }
        })
        return animation
    }


    fun animateProgressBar(progressBar: ProgressBar, progressInBar: Int, durationMSec: Long) = launchCoroutineOnMain {
        val animation = ObjectAnimator.ofInt(progressBar, "progress", progressInBar)
        animation.duration = durationMSec
        animation.interpolator = LinearInterpolator()
        animation.start()
    }

    fun animateBlink(view: View) = animateView(view, R.anim.blink)
    fun animateBlinkWithCallback(view: View, callback: () -> Unit) = animateView(view, R.anim.blink, callback)
    fun animateFastBlinkWithCallback(view: View, callback: () -> Unit) = animateView(view, R.anim.blink_fast, callback)

    fun animateView(view: View, animationId: Int, callback: () -> Unit = {}) {
        val animation = getAnimationWithCallback(animationId) {
            callback()
        }
        launchCoroutineOnMain {
            view.startAnimation(animation)
        }
    }


    suspend fun characterLeavesScene(characterCellsLayout: ConstraintLayout) = suspendCoroutine { cont ->
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            4.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
        ).apply {
            duration = 500L
            fillAfter = true
        }
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                cont.resume(Unit)
            }
        })
        launchCoroutineOnMain {
            characterCellsLayout.startAnimation(animation)
        }
    }

    suspend fun characterReturnsOnScene(characterCellsLayout: ConstraintLayout) = suspendCoroutine { cont ->
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            4.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
        ).apply {
            duration = 500L
        }
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                cont.resume(Unit)
            }
        })
        launchCoroutineOnMain {
            characterCellsLayout.startAnimation(animation)
        }
    }

    companion object {
        private val EMPTY_ANIMATION = object : Animation() {}
    }
}