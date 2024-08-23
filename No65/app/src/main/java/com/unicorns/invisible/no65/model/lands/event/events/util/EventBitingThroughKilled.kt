package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.BITING_THROUGH_DEAD
import com.unicorns.invisible.no65.model.lands.event.Event

class EventBitingThroughKilled : Event({ manager ->
    manager.gameState.flagsMaster.add(BITING_THROUGH_DEAD)
})