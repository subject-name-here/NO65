package com.unicorns.invisible.no65.view.music

import android.media.MediaPlayer
import com.unicorns.invisible.no65.Gradation
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.util.SurjectiveMap
import com.unicorns.invisible.no65.util.launchCoroutine
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MusicPlayer(private val activity: MainActivity) {
    private val mediaPlayers: SurjectiveMap<Int, MediaPlayer> = SurjectiveMap()
    private val listLock = ReentrantLock()
    var currentVolume: Gradation = Gradation.YES

    fun playMusic(
        resourceId: Int,
        behaviour: MusicBehaviour = MusicBehaviour.IGNORE,
        isLooping: Boolean = false
    ) = launchCoroutine { playMusicSuspendTillStart(resourceId, behaviour, isLooping) }

    suspend fun playMusicSuspendTillEnd(
        resourceId: Int,
        behaviour: MusicBehaviour = MusicBehaviour.IGNORE,
        isLooping: Boolean = false
    ) = playMusicSuspend(resourceId, behaviour, isLooping, suspendTillEnd = true)

    suspend fun playMusicSuspendTillStart(
        resourceId: Int,
        behaviour: MusicBehaviour = MusicBehaviour.IGNORE,
        isLooping: Boolean = false
    ) = playMusicSuspend(resourceId, behaviour, isLooping, suspendTillEnd = false)

    private suspend fun playMusicSuspend(
        resourceId: Int,
        behaviour: MusicBehaviour,
        isLooping: Boolean,
        suspendTillEnd: Boolean
    ) = suspendCoroutine { cont ->
        if (resourceId == 0) {
            cont.resume(Unit)
            return@suspendCoroutine
        }

        when (behaviour) {
            MusicBehaviour.PAUSE_ALL -> {
                pauseAllMusic()
            }
            MusicBehaviour.STOP_ALL -> {
                stopAllMusic()
            }
            MusicBehaviour.IGNORE -> {}
        }

        val volume = currentVolume.getFloatValue()
        listLock.withLock {
            mediaPlayers.put(resourceId, MediaPlayer.create(activity, resourceId).apply {
                this.isLooping = isLooping
                setVolume(volume, volume)
                setOnCompletionListener {
                    listLock.withLock {
                        if (mediaPlayers.containsValue(it)) {
                            stopMediaPlayer(it)
                            mediaPlayers.removeValue(it)
                        }
                    }
                    if (suspendTillEnd) {
                        cont.resume(Unit)
                    }
                }
                start()
                if (!suspendTillEnd) {
                    cont.resume(Unit)
                }
            })
        }
    }

    fun pauseAllMusic() {
        listLock.withLock {
            mediaPlayers.values.forEach {
                it.pause()
            }
        }
    }

    fun resumeAllMusic() {
        listLock.withLock {
            mediaPlayers.values.forEach {
                if (!it.isPlaying) {
                    it.start()
                }
            }
        }
    }

    fun stopAllMusic() {
        listLock.withLock {
            mediaPlayers.values.forEach {
                stopMediaPlayer(it)
            }

            mediaPlayers.clear()
        }
    }

    fun nextVolumeMode() {
        currentVolume = currentVolume.next()
        val volumeFloat = currentVolume.getFloatValue()
        listLock.withLock {
            mediaPlayers.values.forEach {
                it.setVolume(volumeFloat, volumeFloat)
            }
        }
    }

    fun stopMusicByResourceId(resourceId: Int) {
        listLock.withLock {
            mediaPlayers[resourceId].forEach {
                stopMediaPlayer(it)
            }
            mediaPlayers.removeKey(resourceId)
        }
    }

    private fun stopMediaPlayer(player: MediaPlayer?) {
        if (player == null) return

        if (player.isPlaying) {
            player.stop()
        }
        player.reset()
        player.release()
    }

    enum class MusicBehaviour {
        IGNORE,
        PAUSE_ALL,
        STOP_ALL
    }
}