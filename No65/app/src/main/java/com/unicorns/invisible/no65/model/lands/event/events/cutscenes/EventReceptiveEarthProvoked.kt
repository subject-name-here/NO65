package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ReceptiveEarth
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.view.music.MusicPlayer


class EventReceptiveEarthProvoked(
    private val characterCell: ReceptiveEarth
): Event({ manager ->
    manager.wrapCutscene {
        activity.musicPlayer.playMusic(
            R.raw.cutscene_receptive_earth_triggered,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )
        characterCell.state = ReceptiveEarth.State.PROVOKED
        drawer.showCharacterMessages(characterCell, listOf(
            R.string.lands_receptive_earth_provoked_1,
            R.string.lands_receptive_earth_provoked_2,
            R.string.lands_receptive_earth_provoked_3,
        ))
        characterCell.state = ReceptiveEarth.State.ANGRY
        drawer.showCharacterMessages(characterCell, listOf(
            R.string.lands_receptive_earth_provoked_4,
            R.string.lands_receptive_earth_provoked_5,
        ))
    }
})