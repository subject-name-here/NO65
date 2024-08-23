package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.OPPOSITION_DEAD
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Opposition
import com.unicorns.invisible.no65.model.lands.event.Event

class EventStr50 : Event(lambda@ { manager ->
    if (OPPOSITION_DEAD in manager.gameState.flagsMaster) {
        manager.gameState.currentMap.getTopCells()
            .filterIsInstance<Opposition>()
            .forEach { manager.gameState.currentMap.removeCellFromTop(it) }
    }
})