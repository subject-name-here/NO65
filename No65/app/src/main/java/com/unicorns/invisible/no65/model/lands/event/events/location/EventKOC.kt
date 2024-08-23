package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.BITING_THROUGH_DEAD
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.THE_WANDERER_STOLE_MONEYS
import com.unicorns.invisible.no65.model.lands.cell.character.npc.BitingThrough
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheWanderer
import com.unicorns.invisible.no65.model.lands.event.Event

class EventKOC : Event(lambda@ { manager ->
    if (THE_WANDERER_STOLE_MONEYS !in manager.gameState.flagsMaster) {
        manager.gameState.currentMap.getTopCells()
            .filterIsInstance<TheWanderer>()
            .forEach { manager.gameState.currentMap.removeCellFromTop(it) }
    }

    if (BITING_THROUGH_DEAD in manager.gameState.flagsMaster) {
        manager.gameState.currentMap.getTopCells()
            .filterIsInstance<BitingThrough>()
            .forEach { manager.gameState.currentMap.removeCellFromTop(it) }
    }
})