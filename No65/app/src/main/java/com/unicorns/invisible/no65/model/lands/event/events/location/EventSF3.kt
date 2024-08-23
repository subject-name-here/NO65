package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventSF3 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_fist_sf3_1,
            R.string.lands_fist_sf3_2,
            R.string.lands_fist_sf3_3,
        ))
    }
})