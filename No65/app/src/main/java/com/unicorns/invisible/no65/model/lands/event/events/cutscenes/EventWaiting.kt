package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Waiting
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventShowMessages
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak


class EventWaiting(waiting: Waiting) : Event(lambda@ { manager ->
    if (manager.gameState !is GameState65) return@lambda
    if (manager.gameState.battleMode == BattleMode.ATTACK) {
        if (!manager.gameState.knowledge.knowsTrigram(Water)) {
            EventShowMessages(listOf(R.string.lands_waiting_attack_no_waterball))
        } else {
            waiting.attackEvent
        }
    } else {
        EventNPCSpeak(waiting) { R.string.lands_waiting }
    }.fireEventChain(manager)
})