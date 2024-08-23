package com.unicorns.invisible.no65.view

import androidx.constraintlayout.widget.Guideline
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityTurnOnBinding
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class TurnOnDrawer(
    private val activity: MainActivity,
    private val binding: ActivityTurnOnBinding
) {
    private val topGuideline: Guideline
        get() = binding.guidelineTvTop
    private val bottomGuideline: Guideline
        get() = binding.guidelineTvBottom
    private val startGuideline: Guideline
        get() = binding.guidelineTvStart
    private val endGuideline: Guideline
        get() = binding.guidelineTvEnd

    private fun getTopPercent(frameNum: Int): Float {
        val c1 = 0.5
        val c2 = 0.02
        return 0.5f - when {
            frameNum <= 100 -> {
                (frameNum * c2 * 1e-2).toFloat()
            }
            else -> {
                (c2 + (frameNum - 100) * (c1 - c2) * 1e-2).toFloat()
            }
        }
    }

    private fun getStartPercent(frameNum: Int): Float {
        return if (frameNum <= 100) {
            0.5f - (frameNum / 2) / 100f
        } else {
            0f
        }
    }

    suspend fun start() {
        activity.musicPlayer.playMusic(
            R.raw.sfx_tv_on,
            isLooping = false,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL
        )
        delay(1000L)
        repeat(ANIMATION_LENGTH_FRAMES + 1) {
            delay(FRAME_LENGTH)
            topGuideline.setGuidelinePercent(getTopPercent(it))
            bottomGuideline.setGuidelinePercent(1f - getTopPercent(it))
            startGuideline.setGuidelinePercent(getStartPercent(it))
            endGuideline.setGuidelinePercent(1f - getStartPercent(it))
        }
        delay(100L)
    }

    companion object {
        const val FRAME_LENGTH = 1L
        const val ANIMATION_LENGTH_FRAMES = 200
    }
}