package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.EMPTY_STREET_ENTERED
import com.unicorns.invisible.no65.model.lands.event.Event

class EventEmptyStreet : Event({ manager ->
    manager.gameState.flagsMaster.add(EMPTY_STREET_ENTERED)
})