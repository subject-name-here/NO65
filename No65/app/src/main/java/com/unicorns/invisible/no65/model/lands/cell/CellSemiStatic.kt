package com.unicorns.invisible.no65.model.lands.cell

import com.unicorns.invisible.no65.model.lands.event.Event

abstract class CellSemiStatic: CellNonEmpty() {
    abstract fun isPassable(): Boolean
    open fun onStep(): Event = Event.Null
}