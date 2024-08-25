package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates


class EventTeleportToFist : Event({ manager ->
    // TODO: test
    manager.gameState.apply {
        val newMap = mapGraph.getMapRelative(currentMapIndex, 1)
        currentMap.removeCellFromTop(protagonist)
        newMap.addCellOnTop(protagonist, Coordinates(0, 0))
        manager.changeCurrentMapIndex(1)
    }
})