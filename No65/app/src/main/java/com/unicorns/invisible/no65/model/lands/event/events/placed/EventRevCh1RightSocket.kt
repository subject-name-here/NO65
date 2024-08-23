package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.REVOLUTION_CHOICE1_LEFT_SOCKET_FIRED
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.REVOLUTION_CHOICE1_RIGHT_SOCKET_FIRED
import com.unicorns.invisible.no65.model.lands.cell.moveable.Source
import com.unicorns.invisible.no65.model.lands.event.EventPlaced

class EventRevCh1RightSocket : EventPlaced(lambda@ { manager ->
    if (REVOLUTION_CHOICE1_LEFT_SOCKET_FIRED in manager.gameState.flagsMaster) {
        manager.wrapCutsceneSkippable {
            drawer.showMessagesPhoneWithUnknownHead(listOf(
                R.string.lands_rev_ch1_wrong_socket_1,
                R.string.lands_rev_ch1_wrong_socket_2,
            ))
        }
        return@lambda
    }

    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_rev_ch1_correct_socket_1,
            R.string.lands_rev_ch1_correct_socket_2,
        ))
        gameState.flagsMaster.add(REVOLUTION_CHOICE1_RIGHT_SOCKET_FIRED)
    }
}) {
    override fun getConditionToFire(manager: LandsManager): Boolean {
        return firingCell is Source
    }
}