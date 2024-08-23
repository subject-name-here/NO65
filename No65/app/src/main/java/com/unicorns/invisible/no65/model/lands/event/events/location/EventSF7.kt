package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventSF7 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_fist_sf7_1,
                R.string.lands_fist_sf7_5,
                R.string.lands_fist_sf7_6,
                R.string.lands_fist_sf7_7,
                R.string.lands_fist_sf7_8,
                R.string.lands_fist_sf7_9,
                R.string.lands_fist_sf7_10,
            )
        )
    }
})