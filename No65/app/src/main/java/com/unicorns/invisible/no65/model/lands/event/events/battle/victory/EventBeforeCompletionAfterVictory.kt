package com.unicorns.invisible.no65.model.lands.event.events.battle.victory

import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.npc.InnerTruth
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Waiting
import com.unicorns.invisible.no65.model.lands.cell.interactive.CellInfo
import com.unicorns.invisible.no65.model.lands.cell.interactive.CellMessage
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.Coordinates


class EventBeforeCompletionAfterVictory: Event({ manager ->
    val allCells = manager.gameState.currentMap.getTopCells()
    for (cell in allCells) {
        when (cell) {
            is TeleportCell -> {
                cell.toMapIndex = 1
                cell.toCoordinates = Coordinates(0, 0)
                val freeFloorCoordinates = CellUtils.findCurrentMapClosestFloor(manager, cell)
                if (freeFloorCoordinates != null) {
                    val innerTruth = manager.gameState.currentMap.createCellOnTop(freeFloorCoordinates, InnerTruth::class)
                    innerTruth.state = InnerTruth.State.HOSTILE
                }
            }
            is Waiting -> {
                manager.gameState.currentMap.moveOnDelta(cell, Coordinates(0, -3))
            }
            is CellInfo -> {
                cell.messageNumber = supportMessageNumbers.random()
                cell.state = CellMessage.State.NOT_CLICKED
            }
            else -> {}
        }
    }

    (manager.gameState.protagonist as CellProtagonist).youState = CellProtagonist.YouState.NOT_BC
}) {
    companion object {
        val supportMessageNumbers = (65..78) + listOf(321)
    }
}