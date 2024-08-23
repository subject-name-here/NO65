package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.ENTERED_TEMPLE
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.TREADING_EVENT_FIRED
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventTreadingAttack


class EventTreadingCheckAfterBattle : Event(lambda@ { manager ->
    if (manager.gameState !is GameState65) return@lambda
    if (
        manager.gameState.protagonist.killed > 32 &&
        TREADING_EVENT_FIRED !in manager.gameState.flagsMaster &&
        ENTERED_TEMPLE !in manager.gameState.flagsMaster
    ) {
        manager.gameState.flagsMaster.add(TREADING_EVENT_FIRED)
        EventTreadingAttack().fireEventChain(manager)
    }
})