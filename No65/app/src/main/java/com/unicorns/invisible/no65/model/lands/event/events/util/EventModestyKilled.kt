package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.MODESTY_DEAD
import com.unicorns.invisible.no65.model.lands.event.Event


class EventModestyKilled : Event({ manager ->
    manager.gameState.flagsMaster.add(MODESTY_DEAD)
})