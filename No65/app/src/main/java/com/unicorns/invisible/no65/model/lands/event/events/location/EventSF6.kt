package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.coroutines.delay

class EventSF6 : Event({ manager ->
    manager.wrapCutscene {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_fist_sf6_3,
                R.string.lands_fist_sf6_4,
                R.string.lands_fist_sf6_5,
            ),
            delayAfterMessage = 0L
        )
        delay(1000L)
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_fist_sf6_6,
                R.string.lands_fist_sf6_7,
            )
        )
    }
})