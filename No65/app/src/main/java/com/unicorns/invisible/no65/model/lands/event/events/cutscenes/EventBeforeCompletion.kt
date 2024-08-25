package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.cell.character.npc.BeforeCompletion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.BC_BATTLE_EVENT_REACHED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class EventBeforeCompletion(
    private val characterCell: BeforeCompletion
): Event(lambda@ { manager ->
    val gameState = manager.gameState
    if (gameState !is GameState65) return@lambda

    manager.activity.musicPlayer.playMusic(
        R.raw.cutscene_bc,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = true
    )

    suspend fun awaitInteractionModeChange(manager: LandsManager) = suspendCoroutine { cont ->
        fun listener() {
            if (gameState.battleMode == BattleMode.ATTACK) {
                manager.controller.onChangeInteractionModeListeners.remove(::listener)
                cont.resume(Unit)
            }
        }
        manager.controller.onChangeInteractionModeListeners.add(::listener)
    }


    val reachedBattleAlready = GlobalState.getBoolean(manager.activity, BC_BATTLE_EVENT_REACHED)
    if (reachedBattleAlready) {
        manager.wrapCutscene {
            CellUtils.litUpCurrentMapFloors(this)
            gameState.battleMode = BattleMode.PEACE
            drawer.showCharacterMessages(characterCell, listOf(R.string.lands_before_completion_event_again_1))
        }
        return@lambda
    }

    val messages = arrayListOf(
        R.string.lands_before_completion_event_1,
        R.string.lands_before_completion_event_2,
    )
    val situationMessages = arrayListOf(
        R.string.lands_before_completion_event_3,
        R.string.lands_before_completion_event_4,
        R.string.lands_before_completion_event_5,
    )
    val afterSwordMessages = arrayListOf(
        R.string.lands_before_completion_event_6,
        R.string.lands_before_completion_event_7,
        R.string.lands_before_completion_event_8,
        R.string.lands_before_completion_event_9,
    )
    launchCoroutine {
        manager.wrapCutscene {
            drawer.showCharacterMessages(characterCell, messages)
            CellUtils.litUpCurrentMapFloors(this)
            delay(500L)

            drawer.showCharacterMessages(characterCell, situationMessages)

            drawer.postShowMessage()
            gameState.battleMode = BattleMode.PEACE
            awaitInteractionModeChange(this)
            delay(500L)
            drawer.preShowMessage()

            drawer.showCharacterMessages(characterCell, afterSwordMessages)

            drawer.postShowMessage()
            suspendCoroutine { cont ->
                characterCell.awaitingUseContinuation = cont
            }

            GlobalState.putBoolean(activity, BC_BATTLE_EVENT_REACHED, true)

            characterCell.trueAttackEvent.fireEventChain(manager)
        }
    }
})