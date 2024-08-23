package com.unicorns.invisible.no65.view

import android.animation.ObjectAnimator
import android.graphics.Path
import android.view.View
import android.view.animation.LinearInterpolator
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.D99LayoutBinding
import com.unicorns.invisible.no65.util.getColorById
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.music.MusicPlayer
import com.unicorns.invisible.no65.view.speech.SpeechProperties
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class D99Drawer(
    private val activity: MainActivity,
    private val binding: D99LayoutBinding
) {
    private val defaultTapSoundId = R.raw.sfx_coin
    private val defaultColor = R.color.green

    private val speechTextView
        get() = binding.speechTextView

    private val optionsContainer
        get() = binding.optionsContainer
    private val option1View
        get() = binding.option1
    private val option2View
        get() = binding.option2
    private val option3View
        get() = binding.option3

    private val faceCell
        get() = binding.d99Body.d99MainBody.cell1D99
    private val centerLeftCell
        get() = binding.d99Body.d99MainBody.cell3D99
    private val centerCenterCell
        get() = binding.d99Body.d99MainBody.cell4D99
    private val centerRightCell
        get() = binding.d99Body.d99MainBody.cell5D99

    private val hand1
        get() = binding.d99Body.hand1
    private val hand2
        get() = binding.d99Body.hand2
    private val hand3
        get() = binding.d99Body.hand3
    private val hand4
        get() = binding.d99Body.hand4
    private val hand5
        get() = binding.d99Body.hand5
    private val hand6
        get() = binding.d99Body.hand6

    fun setFace() = launchCoroutineOnMain {
        faceCell.text = activity.getString(R.string.d99_face)
    }

    fun setLeftCenterCell() = launchCoroutineOnMain {
        centerLeftCell.text = activity.getString(R.string.d99_coming_to_meet_symbol)
    }
    fun setCenterCenterCell() = launchCoroutineOnMain {
        centerCenterCell.text = activity.getString(R.string.d99_deliverance_symbol)
    }
    fun setRightCenterCell() = launchCoroutineOnMain {
        centerRightCell.text = activity.getString(R.string.d99_modesty_symbol)
    }
    fun paintRightCenterCellToRed() = launchCoroutineOnMain {
        centerRightCell.setTextColor(activity.getColorById(R.color.red))
    }

    fun setAll() {
        setFace()
        setLeftCenterCell()
        setCenterCenterCell()
        setRightCenterCell()
        paintRightCenterCellToRed()
    }

    suspend fun showMessage(
        messageId: Int,
        color: Int = defaultColor,
        tapSoundId: Int = defaultTapSoundId,
        delayAfterMessage: Long = SpeechProperties.DELAY_AFTER_MESSAGE
    ) = suspendCoroutine { cont ->
        val message = activity.getString(messageId)
        if (message == "") {
            cont.resume(Unit)
            return@suspendCoroutine
        }

        launchCoroutineOnMain {
            speechTextView.visibility = View.VISIBLE
            optionsContainer.visibility = View.GONE

            activity.musicPlayer.playMusic(
                tapSoundId,
                MusicPlayer.MusicBehaviour.IGNORE,
                isLooping = true
            )
            speechTextView.setTextColor(activity.getColorById(color))

            repeat(message.length) {
                delay(SpeechProperties.MILLISECONDS_PER_SYMBOL)
                speechTextView.text = message.subSequence(0, it + 1)
            }
            activity.musicPlayer.stopMusicByResourceId(tapSoundId)
            delay(delayAfterMessage)
            speechTextView.text = ""
            cont.resume(Unit)
        }
    }

    private var optionChosen = false
    suspend fun showOptions(option1Id: Int, option2Id: Int, option3Id: Int) = suspendCoroutine { cont ->
        val option1 = activity.getString(option1Id)
        val option2 = activity.getString(option2Id)
        val option3 = activity.getString(option3Id)
        optionChosen = false
        launchCoroutineOnMain {
            speechTextView.visibility = View.GONE
            optionsContainer.visibility = View.VISIBLE
            if (option1 == "") {
                option1View.text = ""
            } else {
                option1View.text = activity.getString(R.string.d99_option_placeholder).format(option1)
                option1View.setOnClickListener {
                    if (!optionChosen) {
                        optionChosen = true
                        cont.resume(OptionsResult.ONE)
                    }
                }
            }
            if (option2 == "") {
                option2View.text = ""
            } else {
                option2View.text = activity.getString(R.string.d99_option_placeholder).format(option2)
                option2View.setOnClickListener {
                    if (!optionChosen) {
                        optionChosen = true
                        cont.resume(OptionsResult.TWO)
                    }
                }
            }
            if (option3 == "") {
                option3View.text = ""
            } else {
                option3View.text = activity.getString(R.string.d99_option_placeholder).format(option3)
                option3View.setOnClickListener {
                    if (!optionChosen) {
                        optionChosen = true
                        cont.resume(OptionsResult.THREE)
                    }
                }
            }
        }
    }

    fun hideOptions()  = launchCoroutineOnMain {
        speechTextView.visibility = View.VISIBLE
        optionsContainer.visibility = View.GONE
    }

    fun launchHands() = launchCoroutineOnMain {
        launchHand(hand1, 100f)
        delay(250L)
        launchHand(hand2, 100f)
        delay(250L)
        launchHand(hand3, 100f)
        delay(250L)
        launchHand(hand4, 100f)
        delay(250L)
        launchHand(hand5, 100f)
        delay(250L)
        launchHand(hand6, 100f)
    }

    @Suppress("SameParameterValue")
    private fun launchHand(hand: View, apl: Float) {
        hand.clearAnimation()
        val path = Path()
        path.rLineTo(0f, apl)
        path.rLineTo(0f, -2 * apl)
        path.rLineTo(0f, apl)
        ObjectAnimator.ofFloat(hand, "translationX", "translationY", path).apply {
            duration = 2500L
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            interpolator = LinearInterpolator()
        }.start()
    }

    enum class OptionsResult {
        ONE, TWO, THREE
    }
}