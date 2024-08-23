package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class EventSF5RewindTutorial2 : EventPlaced(lambda@ { manager ->
    val gameState = manager.gameState
    if (gameState !is GameState65) return@lambda

    suspend fun awaitRewind() = suspendCoroutine { cont ->
        fun listener() {
            manager.controller.onRewindListeners.remove(::listener)
            cont.resume(Unit)
        }
        manager.controller.onRewindListeners.add(::listener)
    }


    manager.wrapCutscene {
        val messages = listOf(
            R.string.lands_sf5_tutorial_2_1,
        )
        drawer.showMessagesPhoneWithUnknownHead(messages)

        drawer.postShowMessage()
        rewindActiveOverride = true
        awaitRewind()
        rewindActiveOverride = false
        drawer.preShowMessage()

        val messages2 = listOf(
            R.string.lands_sf5_tutorial_3_1,
            R.string.lands_sf5_tutorial_3_2,
            R.string.lands_sf5_tutorial_3_3,
            R.string.lands_sf5_tutorial_3_4,
        )
        drawer.showMessagesPhoneWithUnknownHead(messages2)
    }
    gameState.rewindAvailable = true
})