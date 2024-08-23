package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.LEFT_RHQ_HALL
import com.unicorns.invisible.no65.model.lands.event.Event

class EventLeftRHQHall : Event({ manager ->
    manager.gameState.flagsMaster.add(LEFT_RHQ_HALL)
})