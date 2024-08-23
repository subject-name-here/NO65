package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventWM3 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_fist_worm_3_1,
                R.string.lands_fist_worm_3_2,
                R.string.lands_fist_worm_3_3,
                R.string.lands_fist_worm_3_4,
                R.string.lands_fist_worm_3_5,
                R.string.lands_fist_worm_3_6,
            )
        )
    }
})