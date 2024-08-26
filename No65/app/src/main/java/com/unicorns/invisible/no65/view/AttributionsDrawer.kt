package com.unicorns.invisible.no65.view

import android.view.View
import android.widget.TextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityAttributionsBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.music.MusicPlayer
import com.unicorns.invisible.no65.view.speech.SpeechProperties.Companion.DELAY_AFTER_MESSAGE
import com.unicorns.invisible.no65.view.speech.SpeechProperties.Companion.MILLISECONDS_PER_SYMBOL
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class AttributionsDrawer(
    override val activity: MainActivity,
    private val binding: ActivityAttributionsBinding
) : FadeDrawer {
    override val screen: View
        get() = binding.screen

    private val messageView: TextView
        get() = binding.attributionsMessage

    private var currentJob: Job? = null
    suspend fun showMessage(messageId: Int) = suspendCoroutine { cont ->
        val message = activity.getString(messageId)
        currentJob = launchCoroutineOnMain {
            messageView.text = ""
            if (message == "") {
                return@launchCoroutineOnMain
            }

            activity.musicPlayer.playMusic(
                R.raw.sfx_tap,
                MusicPlayer.MusicBehaviour.IGNORE,
                isLooping = true
            )

            repeat(message.length) {
                delay(MILLISECONDS_PER_SYMBOL)
                messageView.text = message.subSequence(0, it + 1)
            }
            activity.musicPlayer.stopMusicByResourceId(R.raw.sfx_tap)
            delay(DELAY_AFTER_MESSAGE)
        }.apply {
            invokeOnCompletion {
                activity.musicPlayer.stopMusicByResourceId(R.raw.sfx_tap)
                cont.resume(Unit)
            }
        }
    }

    fun stop() {
        currentJob?.cancel()
        activity.musicPlayer.stopMusicByResourceId(R.raw.sfx_tap)
    }
}