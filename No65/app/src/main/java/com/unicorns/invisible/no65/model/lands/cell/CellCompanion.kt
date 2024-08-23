package com.unicorns.invisible.no65.model.lands.cell

import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates

interface CellCompanion {
    fun onProtagonistMoveOnDelta(delta: Coordinates): Event
    fun onCurrentMapChange(prevIndex: String): Event
    fun isCompanion(): Boolean
}