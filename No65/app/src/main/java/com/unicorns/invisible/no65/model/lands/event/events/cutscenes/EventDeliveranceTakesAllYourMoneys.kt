package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldDeliverance
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.DELIVERANCE_BARGAIN_STARTED
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Deliverance
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.view.Lands65Drawer
import kotlinx.coroutines.delay
import kotlin.coroutines.suspendCoroutine

class EventDeliveranceTakesAllYourMoneys(deliverance: Deliverance, is5Moneys: Boolean) : Event(lambda@ { manager ->
    val gameState = manager.gameState
    if (gameState !is GameState65) return@lambda

    val messages = if (is5Moneys) {
        listOf(
            R.string.lands_deliverance_take_money_enough_1,
            R.string.lands_deliverance_take_money_enough_2,
            R.string.lands_deliverance_take_money_enough_3,
            R.string.lands_deliverance_take_money_enough_4,
        )
    } else {
        listOf(
            R.string.lands_deliverance_take_money_not_enough_1,
            R.string.lands_deliverance_take_money_not_enough_2,
            R.string.lands_deliverance_take_money_not_enough_3,
            R.string.lands_deliverance_take_money_not_enough_4,
            R.string.lands_deliverance_take_money_not_enough_5,
            R.string.lands_deliverance_take_money_not_enough_6,
        )
    } + listOf(
        R.string.lands_deliverance_take_money_transfer_1
    )

    manager.wrapCutscene {
        drawer.showCharacterMessages(deliverance, messages)
        val result = suspendCoroutine { cont ->
            deliverance.awaitingUseContinuation = cont
            launchCoroutine {
                delay(15000L)
                deliverance.resumeIfIsAwaiting(false)
            }
        }
        if (manager.stopped) {
            return@wrapCutscene
        }
        if (result) {
            gameState.protagonist.moneys = 0
            (drawer as Lands65Drawer).drawStats(gameState.protagonist)
            drawer.showCharacterMessages(deliverance, listOf(
                R.string.lands_deliverance_take_money_agree_1,
                R.string.lands_deliverance_take_money_agree_2,
                R.string.lands_deliverance_take_money_agree_3,
                R.string.lands_deliverance_take_money_agree_4,
                R.string.lands_deliverance_take_money_agree_5,
            ))
            drawer.showCharacterMessages(deliverance, listOf(
                R.string.lands_deliverance_take_money_agree_6
            ), delayAfterMessage = 0L)
            gameState.currentMap.removeCellFromTop(deliverance)
            delay(1000L)
            drawer.showCharacterMessages(deliverance, listOf(
                R.string.lands_deliverance_take_money_agree_7
            ))
        } else {
            drawer.showCharacterMessages(deliverance, listOf(
                R.string.lands_deliverance_take_money_refuse_1,
                R.string.lands_deliverance_take_money_refuse_2,
                R.string.lands_deliverance_take_money_refuse_3,
                R.string.lands_deliverance_take_money_refuse_4,
            ))
            val hadADeal = DELIVERANCE_BARGAIN_STARTED in gameState.flagsMaster
            EventAttack(
                deliverance,
                BattleFieldDeliverance(hadADeal),
                onAfterVictoryEvent = EventUnlockBattle(50)
            ).fireEventChain(manager)
        }
    }
})