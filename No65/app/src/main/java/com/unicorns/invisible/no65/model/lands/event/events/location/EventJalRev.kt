package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.MODESTY_DEAD
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Modesty
import com.unicorns.invisible.no65.model.lands.event.Event

class EventJalRev : Event(lambda@ { manager ->
    if (MODESTY_DEAD in manager.gameState.flagsMaster) {
        manager.gameState.currentMap.getTopCells()
            .filterIsInstance<Modesty>()
            .forEach { manager.gameState.currentMap.removeCellFromTop(it) }
    }
})