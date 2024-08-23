package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.EventPlaced

class EventSF5RewindTutorial1 : EventPlaced({ manager ->
    val messages = listOf(
        R.string.lands_sf5_tutorial_1_1,
        R.string.lands_sf5_tutorial_1_2,
        R.string.lands_sf5_tutorial_1_3,
        R.string.lands_sf5_tutorial_1_4,
        R.string.lands_sf5_tutorial_1_5,
        R.string.lands_sf5_tutorial_1_6,
    )
    manager.wrapCutscene {
        drawer.showMessagesPhoneWithUnknownHead(messages)
    }
})