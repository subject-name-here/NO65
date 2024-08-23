package com.unicorns.invisible.no65.model.lands.cell

import com.unicorns.invisible.no65.model.lands.event.Event

abstract class CellPassable: CellNonEmpty() {
    open fun onStep(): Event { return Event.Null }
}