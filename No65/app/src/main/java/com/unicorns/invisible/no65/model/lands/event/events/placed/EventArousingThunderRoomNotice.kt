package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.control.Socket
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import kotlinx.coroutines.delay

class EventArousingThunderRoomNotice : EventPlaced({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_arousing_thunder_room_notice_1,
            )
        )
        delay(1500L)
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_arousing_thunder_room_notice_2,
                R.string.lands_arousing_thunder_room_notice_3,
                R.string.lands_arousing_thunder_room_notice_4,
            )
        )
    }
}) {
    override fun getConditionToFire(manager: LandsManager): Boolean {
        val sockets = manager.gameState.currentMap.getTopCells().filterIsInstance<Socket>()
        return firingCell is CellProtagonist && sockets.isNotEmpty()
    }
}