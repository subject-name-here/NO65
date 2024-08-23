package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventSF5_5 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_fist_sf5_5_1,
            R.string.lands_fist_sf5_5_2,
            R.string.lands_fist_sf5_5_3,
            R.string.lands_fist_sf5_5_4,
            R.string.lands_fist_sf5_5_5,
        ))
    }
})