package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldDecrease
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldIncrease
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Decrease
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Increase
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle


class EventAttackIncreaseDecrease(isIncreaseFirst: Boolean) : Event(lambda@ { manager ->
    val increase = manager.gameState.currentMap.getTopCells().filterIsInstance<Increase>().firstOrNull()
    val decrease = manager.gameState.currentMap.getTopCells().filterIsInstance<Decrease>().firstOrNull()
    if (increase == null || decrease == null) {
        return@lambda
    }

    if (isIncreaseFirst) {
        EventAttackDouble(
            increase,
            decrease,
            nonEmptyListOf(BattleFieldIncrease(false), BattleFieldDecrease(true)),
            onAfterVictoryEvent = EventUnlockBattle(54, 55)
        )
    } else {
        EventAttackDouble(
            decrease,
            increase,
            nonEmptyListOf(BattleFieldDecrease(false), BattleFieldIncrease(true)),
            onAfterVictoryEvent = EventUnlockBattle(54, 55)
        )
    }.fireEventChain(manager)
})