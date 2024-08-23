package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ReceptiveEarth
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventReceptiveEarthMeet(
    private val characterCell: ReceptiveEarth
): Event({ manager ->
    manager.activity.musicPlayer.playMusic(
        R.raw.cutscene_receptive_earth_appears,
        behaviour = MusicPlayer.MusicBehaviour.PAUSE_ALL,
        isLooping = false
    )
    val messages = listOf(
        R.string.lands_receptive_earth_meet_1,
        R.string.lands_receptive_earth_meet_2,
    )

    val maskedMessages = listOf(
        R.string.lands_receptive_earth_meet_3,
        R.string.lands_receptive_earth_meet_4,
        R.string.lands_receptive_earth_meet_5,
        R.string.lands_receptive_earth_meet_6,
        R.string.lands_receptive_earth_meet_7,
    )

    manager.wrapCutscene {
        drawer.showCharacterMessages(characterCell, messages)

        delay(500L)
        characterCell.state = ReceptiveEarth.State.MASKED
        delay(500L)

        drawer.showCharacterMessages(characterCell, maskedMessages)

        activity.musicPlayer.stopMusicByResourceId(R.raw.cutscene_receptive_earth_appears)
        activity.musicPlayer.resumeAllMusic()
    }
})