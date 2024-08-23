package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventSF1 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_fist_sf1_1,
                R.string.lands_fist_sf1_2,
                R.string.lands_fist_sf1_3,
                R.string.lands_fist_sf1_4,
                R.string.lands_fist_sf1_5,
                R.string.lands_fist_sf1_6,
                R.string.lands_fist_sf1_7,
                R.string.lands_fist_sf1_8,
                R.string.lands_fist_sf1_9,
                R.string.lands_fist_sf1_10,
                R.string.lands_fist_sf1_11,
                R.string.lands_fist_sf1_12,
            )
        )
    }
})