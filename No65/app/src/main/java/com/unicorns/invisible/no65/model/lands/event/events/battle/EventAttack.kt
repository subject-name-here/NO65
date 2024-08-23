package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.quickbirdstudios.nonEmptyCollection.list.NonEmptyList
import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
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


class EventAttack(
    private val characterCell: CellNPC,
    private val enemies: NonEmptyList<BattleFieldEnemy>,
    private val isTutorial: Boolean = false,
    private val onAfterVictoryEvent: Event = Null
): Event() {
    constructor(
        characterCell: CellNPC,
        enemy: BattleFieldEnemy,
        isTutorial: Boolean = false,
        onAfterVictoryEvent: Event = Null
    ) : this(characterCell, nonEmptyListOf(enemy), isTutorial, onAfterVictoryEvent)

    override val thisEventLambda: suspend Event.(LandsManager) -> Unit = { manager ->
        manager.stop()
        val gameState = manager.gameState as GameState65
        launchCoroutineOnMain {
            BattleManager(
                manager.activity,
                BattleFieldProtagonist(gameState.knowledge),
                enemies,
                managerMode = if (isTutorial) BattleManager.Mode.STANDARD_TUTORIAL else BattleManager.Mode.STANDARD
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
                    EventBattleVictory(characterCell, onAfterVictoryEvent)
                }
            }.fireEventChain(manager)
        }
    }
}