package com.unicorns.invisible.no65.model.lands.event.events.battle.victory

import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates


class EventKSMAfterVictory: Event({ manager ->
    manager.gameState.currentMap.getTopCells()
        .filterIsInstance<TeleportCell>()
        .forEach { cell ->
            cell.toMapIndex = 1
            cell.toCoordinates = Coordinates(0, 0)
        }
})