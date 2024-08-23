package com.unicorns.invisible.no65.model.lands.event.events.battle.victory

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.GREAT_POSSESSION_GAVE_MONEY
import com.unicorns.invisible.no65.model.lands.cell.interactive.CellMoneyGP
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCellBroken
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates


class EventGreatPossessionAfterVictory(coordinates: Coordinates) : Event({ manager ->
    if (GREAT_POSSESSION_GAVE_MONEY !in manager.gameState.flagsMaster) {
        manager.gameState.currentMap.createCellOnTop(coordinates, CellMoneyGP::class)
    }
    val teleportCell = manager.gameState.currentMap.getTopCells()
        .filterIsInstance<TeleportCell>()
        .firstOrNull { it.toMapIndex == 1 }
    if (teleportCell != null) {
        manager.gameState.currentMap.clearCell(teleportCell.coordinates)
        manager.gameState.currentMap.createCellOnTop(teleportCell.coordinates, TeleportCellBroken::class)
    }
})