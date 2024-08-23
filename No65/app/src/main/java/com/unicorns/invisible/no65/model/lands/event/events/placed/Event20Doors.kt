package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.EventPlaced

class Event20Doors : EventPlaced({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_20_doors_1,
            R.string.lands_20_doors_2,
        ))
    }
})