package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.ALIEN_SPACESHIP_TO_LEAVE
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Retreat
import com.unicorns.invisible.no65.model.lands.cell.interactive.AlienSpaceshipControls
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.view.music.MusicPlayer


class EventRetreatCheckShipIsReady(retreat: Retreat) : Event(lambda@ { manager ->
    if (retreat.leaveEventFired) {
        return@lambda
    }

    val controls = manager.gameState.currentMap.getTopCells().filterIsInstance<AlienSpaceshipControls>()
    if (controls.all { it.state == AlienSpaceshipControls.State.READY }) {
        retreat.leaveEventFired = true
        controls.forEach { it.locked = true }
        manager.gameState.flagsMaster.add(ALIEN_SPACESHIP_TO_LEAVE)
        manager.activity.musicPlayer.playMusic(
            manager.getCurrentMapMusicThemeId(),
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = true
        )
        manager.wrapCutsceneSkippable {
            drawer.showCharacterMessages(retreat, listOf(
                R.string.lands_retreat_ship_ready_1,
                R.string.lands_retreat_ship_ready_2,
                R.string.lands_retreat_ship_ready_3,
                R.string.lands_retreat_ship_ready_4,
            ))
        }
    }
})