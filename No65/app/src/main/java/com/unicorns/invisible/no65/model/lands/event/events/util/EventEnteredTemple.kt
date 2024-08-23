package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.ENTERED_TEMPLE
import com.unicorns.invisible.no65.model.lands.event.Event

class EventEnteredTemple : Event({ manager ->
    manager.gameState.flagsMaster.add(ENTERED_TEMPLE)
})