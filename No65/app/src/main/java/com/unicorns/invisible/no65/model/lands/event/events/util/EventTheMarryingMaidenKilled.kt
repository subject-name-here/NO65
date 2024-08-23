package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.SOUND_TEST_TMM_DEAD
import com.unicorns.invisible.no65.saveload.GlobalState

class EventTheMarryingMaidenKilled : Event({ manager ->
    GlobalState.putBoolean(manager.activity, SOUND_TEST_TMM_DEAD, true)
})