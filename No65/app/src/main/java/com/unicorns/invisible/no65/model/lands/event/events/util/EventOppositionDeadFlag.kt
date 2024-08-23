package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.OPPOSITION_DEAD
import com.unicorns.invisible.no65.model.lands.event.Event

class EventOppositionDeadFlag : Event({ manager ->
    manager.gameState.flagsMaster.add(OPPOSITION_DEAD)
})