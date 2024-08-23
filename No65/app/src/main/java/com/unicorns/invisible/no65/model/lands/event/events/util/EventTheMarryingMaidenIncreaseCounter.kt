package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_MM_ENCOUNTERED_COUNTER
import com.unicorns.invisible.no65.saveload.GlobalState


class EventTheMarryingMaidenIncreaseCounter : Event({ manager ->
    val encCounter = GlobalState.getInt(manager.activity, THE_MM_ENCOUNTERED_COUNTER)
    GlobalState.putInt(manager.activity, THE_MM_ENCOUNTERED_COUNTER, encCounter + 1)
})