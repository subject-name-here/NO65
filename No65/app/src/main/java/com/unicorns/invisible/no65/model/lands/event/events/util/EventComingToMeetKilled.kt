package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.COMING_TO_MEET_DEAD
import com.unicorns.invisible.no65.model.lands.event.Event

class EventComingToMeetKilled : Event({ manager ->
    manager.gameState.flagsMaster.add(COMING_TO_MEET_DEAD)
})