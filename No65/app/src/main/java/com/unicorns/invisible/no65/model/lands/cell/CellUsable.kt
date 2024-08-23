package com.unicorns.invisible.no65.model.lands.cell

import com.unicorns.invisible.no65.model.lands.event.Event

interface CellUsable {
    fun use(): Event
    fun isUsableDuringCutscene(): Boolean {
        return false
    }
}