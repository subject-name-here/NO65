package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.view.music.MusicPlayer


class EventTeleportToFist : Event({ manager ->
    manager.wrapCutscene {
        // TODO: test
        manager.activity.musicPlayer.playMusicSuspendTillStart(
            R.raw.sfx_teleport_bc,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
        manager.drawer.fadeToBlack(200L).join()
    }
    manager.gameState.apply {
        val newMap = mapGraph.getMapRelative(currentMapIndex, 1)
        currentMap.removeCellFromTop(protagonist)
        newMap.addCellOnTop(protagonist, Coordinates(0, 0))
        manager.changeCurrentMapIndex(1)
    }
    manager.drawer.hideIncludeLayout()
})