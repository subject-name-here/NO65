package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.BattleResult
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldD99
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class EventAttackD99 : Event() {
    override val thisEventLambda: suspend Event.(LandsManager) -> Unit = { manager ->
        manager.stop()
        val gameState = manager.gameState as GameState65
        launchCoroutineOnMain {
            BattleManager(
                manager.activity,
                BattleFieldProtagonist(gameState.knowledge),
                nonEmptyListOf(BattleFieldD99()),
                BattleManager.Mode.STANDARD_D99,
                fieldWidth = 9,
                fieldHeight = 11
            ) { result ->
                defaultResultHandler(manager, result)
            }.apply {
                launchBattle()
            }
        }
    }

    private fun defaultResultHandler(
        manager: LandsManager,
        result: BattleResult
    ) {
        launchCoroutine {
            when (result) {
                BattleResult.BATTLE_DEFEAT -> {
                    EventBattleDefeat()
                }
                BattleResult.BATTLE_VICTORY -> {
                    EventBattleD99Victory()
                }
            }.fireEventChain(manager)
        }
    }
}