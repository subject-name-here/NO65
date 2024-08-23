package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.EventPlaced

class EventSF4JumpTutorial : EventPlaced({ manager ->
    val messages = listOf(
        R.string.lands_sf4_tutorial_1,
        R.string.lands_sf4_tutorial_2,
        R.string.lands_sf4_tutorial_3,
        R.string.lands_sf4_tutorial_4,
        R.string.lands_sf4_tutorial_5,
        R.string.lands_sf4_tutorial_7,
        R.string.lands_sf4_tutorial_8,
        R.string.lands_sf4_tutorial_9,
    )
    manager.wrapCutscene {
        drawer.showMessagesPhoneWithUnknownHead(messages)
    }
})