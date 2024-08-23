package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event

class EventHoldingTogether : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_meet_holding_together_1,
            R.string.lands_meet_holding_together_2,
            R.string.lands_meet_holding_together_3,
            R.string.lands_meet_holding_together_4,
            R.string.lands_meet_holding_together_5,
        ))
    }
})