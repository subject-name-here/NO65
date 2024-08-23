package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.JoyousLake
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.view.music.MusicPlayer


class EventJoyousLake(
    private val characterCell: JoyousLake,
): Event({ manager ->
    manager.wrapCutscene {
        activity.musicPlayer.playMusic(
            R.raw.cutscene_joyous_lake,
            behaviour = MusicPlayer.MusicBehaviour.PAUSE_ALL,
            isLooping = false
        )

        drawer.showCharacterMessages(characterCell, listOf(
            R.string.lands_joyous_lake_event_1,
            R.string.lands_joyous_lake_event_2,
            R.string.lands_joyous_lake_event_3,
            R.string.lands_joyous_lake_event_4,
            R.string.lands_joyous_lake_event_5,
            R.string.lands_joyous_lake_event_6,
            R.string.lands_joyous_lake_event_7,
            R.string.lands_joyous_lake_event_8,
            R.string.lands_joyous_lake_event_9,
        ))
        activity.musicPlayer.stopMusicByResourceId(R.raw.cutscene_joyous_lake)
        activity.musicPlayer.resumeAllMusic()
    }
})