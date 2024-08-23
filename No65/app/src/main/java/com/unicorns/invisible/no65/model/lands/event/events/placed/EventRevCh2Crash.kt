package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.lands.cell.moveable.Source
import com.unicorns.invisible.no65.model.lands.event.EventPlaced

class EventRevCh2Crash : EventPlaced({ manager ->
    manager.activity.exitGame()
}) {
    override fun getConditionToFire(manager: LandsManager): Boolean {
        return firingCell is Source
    }
}