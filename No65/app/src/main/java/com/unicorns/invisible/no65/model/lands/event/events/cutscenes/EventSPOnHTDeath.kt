package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event


class EventSPOnHTDeath : Event({ manager ->
    manager.wrapCutsceneSkippable {
        val messages = listOf(
            R.string.lands_on_holding_together_death_1,
            R.string.lands_on_holding_together_death_2,
            R.string.lands_on_holding_together_death_3,
        )
        drawer.showMessagesPhoneWithUnknownHead(messages)
    }
})