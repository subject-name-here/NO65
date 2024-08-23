package com.unicorns.invisible.no65.model.lands.event.events.battle.victory

import com.unicorns.invisible.no65.model.lands.RegisteredCounters.Companion.MONEYS_STOLEN_BY_THE_WANDERER
import com.unicorns.invisible.no65.model.lands.cell.interactive.CellMoney
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.Coordinates


class EventTheWandererAfterVictory(theWandererCoordinates: Coordinates) : Event({ manager ->
    val moneys = manager.gameState.countersMaster[MONEYS_STOLEN_BY_THE_WANDERER]
    val cell = manager.gameState.currentMap.getTopCell(theWandererCoordinates)
    repeat(moneys) {
        val floor = CellUtils.findCurrentMapClosestFloor(manager, cell) ?: theWandererCoordinates
        manager.gameState.currentMap.createCellOnTop(floor, CellMoney::class)
    }
    manager.gameState.countersMaster[MONEYS_STOLEN_BY_THE_WANDERER] = 0
})