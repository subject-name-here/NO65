package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventRevPz15 : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_rev_puzzle_1_5_1,
                R.string.lands_rev_puzzle_1_5_2,
            )
        )
    }
})