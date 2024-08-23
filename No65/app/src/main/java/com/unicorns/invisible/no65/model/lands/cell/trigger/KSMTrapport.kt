package com.unicorns.invisible.no65.model.lands.cell.trigger

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.floor.CellFloor
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.placed.EventKSMTrapport
import kotlinx.serialization.Serializable


@Serializable
class KSMTrapport(override var cellBelow: Cell) : CellFloor() {
    private var timesStepped = 0
    override val floorColor: Int
        get() = when (timesStepped) {
            0 -> R.color.almost_white
            1, 2 -> R.color.light_grey
            3, 4 -> R.color.grey
            else -> R.color.dark_grey
        }

    override fun onStep(): Event = EventFactory.create { timesStepped++ }
        .then(EventKSMTrapport(coordinates))
}