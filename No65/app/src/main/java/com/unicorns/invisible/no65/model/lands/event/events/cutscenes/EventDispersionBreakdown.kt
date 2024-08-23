package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Dispersion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.view.music.MusicPlayer


class EventDispersionBreakdown(dispersion: Dispersion) : Event({ manager ->
    manager.wrapCutscene {
        activity.musicPlayer.playMusic(
            R.raw.cutscene_dispersion,
            behaviour = MusicPlayer.MusicBehaviour.PAUSE_ALL,
            isLooping = true
        )

        drawer.showTalkingHead(dispersion)
        drawer.showMessages(
            listOf(
                R.string.lands_dispersion_breakdown_1
            ),
            color = dispersion.speechColor,
            tapSoundId = dispersion.speechSound
        )
        dispersion.state = Dispersion.State.FEAR
        drawer.showMessages(
            listOf(
                R.string.lands_dispersion_breakdown_2,
                R.string.lands_dispersion_breakdown_3,
                R.string.lands_dispersion_breakdown_4,
                R.string.lands_dispersion_breakdown_5,
                R.string.lands_dispersion_breakdown_6,
            ),
            color = dispersion.speechColor,
            tapSoundId = dispersion.speechSound
        )

        activity.musicPlayer.stopMusicByResourceId(R.raw.cutscene_dispersion)
        activity.musicPlayer.resumeAllMusic()
        dispersion.state = Dispersion.State.ICE_CREAM
        drawer.showMessages(
            listOf(
                R.string.lands_dispersion_ice_cream
            ),
            color = dispersion.speechColor,
            tapSoundId = dispersion.speechSound
        )
        drawer.hideTalkingHead()
    }
})