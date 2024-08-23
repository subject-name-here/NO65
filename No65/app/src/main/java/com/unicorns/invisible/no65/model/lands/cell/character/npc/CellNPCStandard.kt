package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory

abstract class CellNPCStandard: CellNPC() {
    protected abstract val attackEvent: Event
    protected abstract val speakEvent: Event
    protected open val chillEvent: Event = Event.Null

    protected open fun chillCheck(): Boolean = false

    final override fun use(): Event {
        return EventFactory.createWithNext { manager ->
            if (manager.gameState !is GameState65) {
                return@createWithNext Event.Null
            }

            when (manager.gameState.battleMode) {
                BattleMode.ATTACK -> {
                    attackEvent
                }
                BattleMode.PEACE, BattleMode.FIXED_PEACE -> {
                    if (chillCheck()) {
                        chillEvent
                    } else {
                        speakEvent
                    }
                }
            }
        }
    }
}