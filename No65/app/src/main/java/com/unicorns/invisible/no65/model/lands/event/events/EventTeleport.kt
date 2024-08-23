package com.unicorns.invisible.no65.model.lands.event.events

import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.cell.CellPassable
import com.unicorns.invisible.no65.model.lands.cell.CellSemiStatic
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates

class EventTeleport(
    private val toMapIndex: Int,
    private val toCoordinates: Coordinates,
    private val occupier: CellNonEmpty
): Event(lambda@ { manager ->
    fun checkOntoCellBelow(ontoCell: Cell): Boolean {
        return ontoCell is CellPassable || ontoCell is CellSemiStatic && ontoCell.isPassable()
    }

    manager.gameState.apply {
        val newMap = mapGraph.getMapRelative(currentMapIndex, toMapIndex)
        val ontoCell = newMap.getTopCell(toCoordinates)
        if (checkOntoCellBelow(ontoCell)) {
            currentMap.removeCellFromTop(occupier)
            newMap.addCellOnTop(occupier, toCoordinates)

            if (occupier == protagonist && toMapIndex >= 0) {
                manager.changeCurrentMapIndex(toMapIndex)
            }
        }
    }
})