package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventSF8 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_fist_sf8_1,
            R.string.lands_fist_sf8_2,
            R.string.lands_fist_sf8_3,
            R.string.lands_fist_sf8_4,
            R.string.lands_fist_sf8_5,
            R.string.lands_fist_sf8_6,
        ))
    }
})