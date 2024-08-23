package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.model.lands.cell.interactive.Note
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.model.lands.event.events.EventTeleport
import com.unicorns.invisible.no65.util.Coordinates

class EventTeleportStr46 : EventPlaced(lambda@ { manager ->
    val toCoordinates = manager.gameState.protagonist.coordinates
    EventTeleport(1, toCoordinates, manager.gameState.protagonist).fireEventChain(manager)
    reloadEvent(manager)

    val messagesCoordinates = listOf(Coordinates(-18, 5))
    val prevMap = manager.gameState.mapGraph.getMap("map_str_46_1")
    val newMap = manager.gameState.mapGraph.getMap("map_str_46_2")
    messagesCoordinates.forEach { coordinates ->
        val cell = prevMap.getTopCell(coordinates)
        val newCell = newMap.getTopCell(coordinates)
        if (cell is Note && newCell is Note) {
            newCell.state = cell.state
        }
    }
})