package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.launchCoroutine
import kotlinx.coroutines.delay


class EventTLFOnLightningStrikeAcquired : Event(lambda@ { manager ->
    if (manager.gameState.currentMapIndex != "map_fst_atb") return@lambda

    val teleportAppearCoordinates = Coordinates(0, 51)

    manager.wrapCutsceneSkippable {
        delay(2500L)

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_lightning_strike_acquired_1,
            R.string.lands_lightning_strike_acquired_2,
        ))

        launchCoroutine {
            delay(1000L)
            gameState.currentMap.clearCell(teleportAppearCoordinates)
            val teleportCell = gameState.currentMap.createCellOnTop(teleportAppearCoordinates, TeleportCell::class)
            teleportCell.toMapIndex = 0
            teleportCell.toCoordinates = Coordinates.ZERO
        }

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_lightning_strike_acquired_3
        ),
            delayAfterMessage = 0L
        )

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_lightning_strike_acquired_4,
            R.string.lands_lightning_strike_acquired_5,
        ))
    }
})