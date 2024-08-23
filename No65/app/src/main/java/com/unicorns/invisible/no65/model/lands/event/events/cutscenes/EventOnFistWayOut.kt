package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.coroutines.delay


class EventOnFistWayOut : Event(lambda@ { manager ->
    val teleportAppearCoordinates = Coordinates(0, 15)
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_on_fist_way_out_sp_1,
                R.string.lands_on_fist_way_out_sp_2,
                R.string.lands_on_fist_way_out_sp_3,
                R.string.lands_on_fist_way_out_sp_4,
                R.string.lands_on_fist_way_out_sp_5,
                R.string.lands_on_fist_way_out_sp_6,
                R.string.lands_on_fist_way_out_sp_7,
            )
        )

        delay(5000L)

        val teleportCell = gameState.currentMap.createCellOnTop(teleportAppearCoordinates, TeleportCell::class)
        teleportCell.toMapIndex = 0
        teleportCell.toCoordinates = Coordinates(-6, 0)

        delay(1000L)

        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_on_fist_way_out_sp_11,
                R.string.lands_on_fist_way_out_sp_12,
                R.string.lands_on_fist_way_out_sp_18,
                R.string.lands_on_fist_way_out_sp_19,
            )
        )
    }
})