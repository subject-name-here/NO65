package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventSF9 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_fist_sf9_1,
            R.string.lands_fist_sf9_2,
            R.string.lands_fist_sf9_3,
            R.string.lands_fist_sf9_4,
            R.string.lands_fist_sf9_5,
            R.string.lands_fist_sf9_6,
        ))
    }
})