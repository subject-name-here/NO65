package com.unicorns.invisible.no65.model.lands.event.events.d99

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttackD99
import com.unicorns.invisible.no65.view.music.MusicPlayer


class EventD99Short : Event(lambda@ { manager ->
    val gameState = manager.gameState
    if (gameState !is GameState65) return@lambda
    gameState.protagonist.moneys = 0

    manager.activity.musicPlayer.playMusic(
        R.raw.cutscene_d99_2,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = true
    )
    val d99Drawer = manager.drawer.showD99Layout(withFadeIn = false)
    d99Drawer.launchHands()
    d99Drawer.setAll()
    d99Drawer.showMessage(R.string.d99_i_missed_you)
    d99Drawer.showMessage(R.string.d99_lets_go)

    EventAttackD99().fireEventChain(manager)
})