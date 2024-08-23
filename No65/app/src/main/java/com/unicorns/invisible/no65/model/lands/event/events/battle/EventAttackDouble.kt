package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.quickbirdstudios.nonEmptyCollection.list.NonEmptyList
import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.BattleResult
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.npc.CellNPC
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class EventAttackDouble(
    private val characterCell1: CellNPC,
    private val characterCell2: CellNPC,
    private val enemies: NonEmptyList<BattleFieldEnemy>,
    private val onAfterVictoryEvent: Event = Null
): Event() {
    override val thisEventLambda: suspend Event.(LandsManager) -> Unit = { manager ->
        manager.stop()
        val gameState = manager.gameState as GameState65
        launchCoroutineOnMain {
            BattleManager(
                manager.activity,
                BattleFieldProtagonist(gameState.knowledge),
                enemies,
                BattleManager.Mode.STANDARD
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
                    EventBattleDoubleVictory(characterCell1, characterCell2, onAfterVictoryEvent)
                }
            }.fireEventChain(manager)
        }
    }
}