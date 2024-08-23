package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventRevChF : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_rev_choice_final_1,
            R.string.lands_rev_choice_final_2,
            R.string.lands_rev_choice_final_3,
            R.string.lands_rev_choice_final_4,
            R.string.lands_rev_choice_final_5,
            R.string.lands_rev_choice_final_6,
            R.string.lands_rev_choice_final_7,
            R.string.lands_rev_choice_final_8,
            R.string.lands_rev_choice_final_9,
        ))
    }
})