package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.view.music.MusicPlayer

class EventChaseOver : EventPlaced({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_oppression_chase_over_1,
            R.string.lands_oppression_chase_over_2,
            R.string.lands_oppression_chase_over_3,
        ))
    }
    manager.activity.musicPlayer.playMusic(
        manager.getCurrentMapMusicThemeId(),
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = true
    )
})