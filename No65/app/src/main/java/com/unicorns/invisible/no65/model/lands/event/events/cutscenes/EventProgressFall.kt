package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Progress
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventProgressFall(progress: Progress) : Event({ manager ->
    manager.activity.musicPlayer.playMusic(
        R.raw.sfx_mechanical_failure,
        behaviour = MusicPlayer.MusicBehaviour.IGNORE,
        isLooping = false
    )
    progress.state = Progress.State.FALLEN
    val iterations = 15
    repeat(iterations) {
        progress.rotation = 360f - 90f * it / iterations
        delay(LandsManager.TICK_TIME_MILLISECONDS)
    }
    progress.rotation = 270f
})