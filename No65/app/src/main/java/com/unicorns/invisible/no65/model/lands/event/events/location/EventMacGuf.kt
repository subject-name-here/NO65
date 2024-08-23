package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.MACGUFFIN_QUEST_STARTED
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.EventSave

class EventMacGuf : Event({ manager ->
    if (MACGUFFIN_QUEST_STARTED in manager.gameState.flagsMaster) {
        EventSave(silent = true).fireEventChain(manager)
    }
})