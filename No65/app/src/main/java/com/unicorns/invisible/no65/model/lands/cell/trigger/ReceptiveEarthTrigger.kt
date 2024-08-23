package com.unicorns.invisible.no65.model.lands.cell.trigger

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ReceptiveEarth
import com.unicorns.invisible.no65.model.lands.cell.floor.CellFloor
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventReceptiveEarthBeforeMeet
import com.unicorns.invisible.no65.util.CellUtils
import kotlinx.serialization.Serializable


@Serializable
class ReceptiveEarthTrigger(override var cellBelow: Cell): CellFloor() {
    override val floorColor
        get() = R.color.white

    private var isTriggered = false
    override fun onStep(): Event = EventFactory.createWithNext lambda@ { manager ->
        if (isTriggered) {
            return@lambda Event.Null
        }

        val cells = manager.gameState.currentMap.getTopCells()
        val re = cells.filterIsInstance<ReceptiveEarth>().firstOrNull() ?: return@lambda Event.Null

        if (re.wearMaskEventFired || re in CellUtils.getCellsInSight(manager)) {
            return@lambda Event.Null
        }

        isTriggered = true
        disableTriggersBehind(manager)
        EventReceptiveEarthBeforeMeet(re)
    }

    private fun disableTriggersBehind(manager: LandsManager) {
        manager.gameState.currentMap.getTopCells()
            .filterIsInstance<ReceptiveEarthTrigger>()
            .filter { cell -> cell.coordinates.col <= this.coordinates.col }
            .forEach { cell -> cell.isTriggered = true }
    }
}