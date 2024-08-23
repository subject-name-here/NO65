package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.BattleResult
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldTheCreature
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.STARTED_GAME
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_CREATURE_KILLED
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_CREATURE_MET_COUNTER
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class EventAttackTheCreature : Event() {
    override val thisEventLambda: suspend Event.(LandsManager) -> Unit = { manager ->
        val wasKilled = GlobalState.getBoolean(manager.activity, THE_CREATURE_KILLED)
        val isCheating = !manager.gameState.flagsMaster.contains(STARTED_GAME)
        manager.stop()
        val gameState = manager.gameState as GameState65
        launchCoroutineOnMain {
            BattleManager(
                manager.activity,
                BattleFieldProtagonist(gameState.knowledge),
                nonEmptyListOf(BattleFieldTheCreature(gameState.protagonist.killed, wasKilled, isCheating)),
                BattleManager.Mode.STANDARD_THE_CREATURE,
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
            EventTheCreatureBattleResult(result).fireEventChain(manager)
        }
    }
}