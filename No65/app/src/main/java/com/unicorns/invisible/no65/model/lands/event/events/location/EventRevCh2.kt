package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventRevCh2 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_rev_choice_2_1,
                R.string.lands_rev_choice_2_2,
                R.string.lands_rev_choice_2_3,
                R.string.lands_rev_choice_2_4,
                R.string.lands_rev_choice_2_5,
            )
        )
    }
})