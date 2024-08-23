package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.moveable.Source
import com.unicorns.invisible.no65.model.lands.event.EventPlaced


class EventSF3Stuck : EventPlaced({ manager ->
    manager.wrapCutscene {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_sf3_stuck_1,
                R.string.lands_sf3_stuck_2,
                R.string.lands_sf3_stuck_3,
                R.string.lands_sf3_stuck_4,
                R.string.lands_sf3_stuck_5,
                R.string.lands_sf3_stuck_6,
                R.string.lands_sf3_stuck_7,
            )
        )
    }
}) {
    override fun getConditionToFire(manager: LandsManager): Boolean {
        return firingCell is Source
    }
}