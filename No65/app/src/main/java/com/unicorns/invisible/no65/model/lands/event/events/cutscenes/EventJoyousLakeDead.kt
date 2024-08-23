package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.coroutines.delay


class EventJoyousLakeDead : Event({ manager ->
    val teleportCoordinates = Coordinates(-26, 0)

    manager.wrapCutscene {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_joyous_lake_dead_1,
            R.string.lands_joyous_lake_dead_2,
            R.string.lands_joyous_lake_dead_3,
        ))

        delay(1000L)
        gameState.currentMap.clearCell(teleportCoordinates)
        val teleport = gameState.currentMap.createCellOnTop(teleportCoordinates, TeleportCell::class)
        teleport.toCoordinates = Coordinates(0, 11)
        teleport.toMapIndex = 0
        delay(1000L)

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_joyous_lake_dead_4,
        ))
    }
})