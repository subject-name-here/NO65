package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventWM2 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_fist_worm_2_1,
                R.string.lands_fist_worm_2_2,
                R.string.lands_fist_worm_2_3,
                R.string.lands_fist_worm_2_4,
                R.string.lands_fist_worm_2_5,
                R.string.lands_fist_worm_2_6,
                R.string.lands_fist_worm_2_7,
            )
        )
    }
})