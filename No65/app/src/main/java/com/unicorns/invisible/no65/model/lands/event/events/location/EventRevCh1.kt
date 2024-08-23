package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventRevCh1 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_rev_choice_1_1,
                R.string.lands_rev_choice_1_2,
                R.string.lands_rev_choice_1_3,
                R.string.lands_rev_choice_1_4,
                R.string.lands_rev_choice_1_5,
                R.string.lands_rev_choice_1_6,
                R.string.lands_rev_choice_1_7,
                R.string.lands_rev_choice_1_8,
            )
        )
    }
})