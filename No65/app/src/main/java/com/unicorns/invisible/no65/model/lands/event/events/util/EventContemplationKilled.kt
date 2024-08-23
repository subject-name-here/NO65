package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.CONTEMPLATION_WAS_KILLED
import com.unicorns.invisible.no65.saveload.GlobalState

class EventContemplationKilled : Event({ manager ->
    GlobalState.putBoolean(manager.activity, CONTEMPLATION_WAS_KILLED, true)
})