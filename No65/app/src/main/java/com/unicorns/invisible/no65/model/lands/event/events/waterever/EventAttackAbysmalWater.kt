package com.unicorns.invisible.no65.model.lands.event.events.waterever

import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.BattleResult
import com.unicorns.invisible.no65.model.battlefield.fighter.equal.BattleFieldEqualAbysmalWater
import com.unicorns.invisible.no65.model.battlefield.fighter.equal.BattleFieldEqualBeforeCompletion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventBattleDefeat
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class EventAttackAbysmalWater : Event() {
    override val thisEventLambda: suspend Event.(LandsManager) -> Unit = { manager ->
        manager.stop()
        launchCoroutineOnMain {
            BattleManager(
                manager.activity,
                BattleFieldEqualBeforeCompletion(),
                nonEmptyListOf(BattleFieldEqualAbysmalWater()),
                BattleManager.Mode.EQUAL,
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
                    EventAbysmalWaterDefeated()
                }
            }.fireEventChain(manager)
        }
    }
}