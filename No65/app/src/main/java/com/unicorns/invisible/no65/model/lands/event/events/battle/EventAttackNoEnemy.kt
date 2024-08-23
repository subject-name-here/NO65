package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.BattleResult
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldNoEnemy
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.MACGUFFIN_QUEST_COMPLETED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class EventAttackNoEnemy : Event() {
    override val thisEventLambda: suspend Event.(LandsManager) -> Unit = { manager ->
        val questCompleted = GlobalState.getBoolean(manager.activity, MACGUFFIN_QUEST_COMPLETED)
        if (!questCompleted) {
            manager.wrapCutsceneSkippable {
                drawer.showMessages(
                    listOf(
                        R.string.lands_macguffin_quest_before_battle_1,
                        R.string.lands_macguffin_quest_before_battle_2,
                        R.string.lands_macguffin_quest_before_battle_3,
                        R.string.lands_macguffin_quest_before_battle_4,
                        R.string.lands_macguffin_quest_before_battle_5,
                        R.string.lands_macguffin_quest_before_battle_6,
                    ),
                    color = R.color.black,
                    tapSoundId = R.raw.sfx_tap
                )
            }
        }

        manager.stop()
        val gameState = manager.gameState as GameState65
        launchCoroutineOnMain {
            BattleManager(
                manager.activity,
                BattleFieldProtagonist(gameState.knowledge),
                nonEmptyListOf(BattleFieldNoEnemy()),
                BattleManager.Mode.STANDARD,
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
                    EventNoEnemyVictory()
                }
            }.fireEventChain(manager)
        }
    }
}