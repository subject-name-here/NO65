package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventWM1 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_fist_worm_1_1,
                R.string.lands_fist_worm_1_2,
                R.string.lands_fist_worm_1_3,
                R.string.lands_fist_worm_1_4,
                R.string.lands_fist_worm_1_5,
            )
        )
    }
})