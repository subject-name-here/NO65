package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.JAIL_ENTERED
import com.unicorns.invisible.no65.model.lands.event.Event


class EventJalCel : Event({ manager ->
    manager.gameState.flagsMaster.add(JAIL_ENTERED)
})