package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventWM0 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_fist_worm_0_1,
                R.string.lands_fist_worm_0_2,
                R.string.lands_fist_worm_0_3,
                R.string.lands_fist_worm_0_4,
                R.string.lands_fist_worm_0_5,
                R.string.lands_fist_worm_0_6,
                R.string.lands_fist_worm_0_7,
                R.string.lands_fist_worm_0_8,
                R.string.lands_fist_worm_0_9,
            )
        )
    }
})