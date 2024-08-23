package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.CREATIVE_HEAVEN_DEAD
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.EventSave
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.CREATIVE_HEAVEN_BATTLE_IN_PROGRESS
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.view.music.MusicPlayer


class EventCreativeHeavenDead : Event({ manager ->
    manager.gameState.flagsMaster.add(CREATIVE_HEAVEN_DEAD)
    GlobalState.putBoolean(manager.activity, CREATIVE_HEAVEN_BATTLE_IN_PROGRESS, false)
    EventSave(silent = true).fireEventChain(manager)
    manager.activity.musicPlayer.playMusic(
        manager.getCurrentMapMusicThemeId(),
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = true
    )
})