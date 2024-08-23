package com.unicorns.invisible.no65.model.lands.cell

import com.unicorns.invisible.no65.model.lands.event.Event

interface CellControl {
    fun onTickWithEvent(tick: Int): Event
}