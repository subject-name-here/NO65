package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.OPPOSITION_CONTRACT_IN_PLACE
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Peace
import com.unicorns.invisible.no65.model.lands.event.Event

class EventSec3Barracks : Event({ manager ->
    if (OPPOSITION_CONTRACT_IN_PLACE !in manager.gameState.flagsMaster) {
        manager.gameState.currentMap.getTopCells()
            .filterIsInstance<Peace>()
            .forEach { manager.gameState.currentMap.removeCellFromTop(it) }
    }
})