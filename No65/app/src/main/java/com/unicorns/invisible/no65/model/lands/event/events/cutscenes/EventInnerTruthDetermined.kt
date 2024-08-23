package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.InnerTruth
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.view.music.MusicPlayer


class EventInnerTruthDetermined(
    private val characterCell: InnerTruth,
    private val foughtWithITT: Boolean,
): Event({ manager ->
    if (foughtWithITT) {
        manager.activity.musicPlayer.stopAllMusic()
    } else {
        manager.activity.musicPlayer.playMusic(
            R.raw.cutscene_inner_truth_determined,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )
    }

    val messages = if (foughtWithITT)
        listOf(
            R.string.lands_inner_truth_determined_again_1
        )
    else
        listOf(
            R.string.lands_inner_truth_determined_1,
            R.string.lands_inner_truth_determined_2,
            R.string.lands_inner_truth_determined_3,
            R.string.lands_inner_truth_determined_4,
        )

    manager.wrapCutscene {
        drawer.showCharacterMessages(characterCell, messages)
    }
})