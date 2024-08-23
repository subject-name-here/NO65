package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.cell.floor.CellFloor
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.Coordinates

class EventRemoveEstonianRoomRoad : EventPlaced({ manager ->
    val row = 4
    val colBegin = 9
    val colEnd = 19
    (colBegin..colEnd).forEach { col ->
        val coordinates = Coordinates(row, col)
        val cell = manager.gameState.currentMap.getTopCell(coordinates)
        if (cell !is CellFloor && cell is CellNonEmpty) {
            val toCell = CellUtils.findCurrentMapClosestFloor(manager, manager.gameState.protagonist)!!
            manager.gameState.currentMap.moveTo(cell, toCell)
        }
        manager.gameState.currentMap.clearCell(coordinates)
    }
})