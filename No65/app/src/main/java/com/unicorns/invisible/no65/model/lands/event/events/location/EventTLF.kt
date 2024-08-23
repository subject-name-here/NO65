package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.AT_STARTED_SHOWDOWN
import com.unicorns.invisible.no65.model.lands.event.Event

class EventTLF : Event({ manager ->
    manager.gameState.flagsMaster.add(AT_STARTED_SHOWDOWN)
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_at_lair_finale_1,
                R.string.lands_at_lair_finale_2,
                R.string.lands_at_lair_finale_3,
                R.string.lands_at_lair_finale_4,
                R.string.lands_at_lair_finale_5,
                R.string.lands_at_lair_finale_6,
                R.string.lands_at_lair_finale_7,
            )
        )
    }
})