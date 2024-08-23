package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.moveable.Source
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.REVOLUTION_RED_BUTTON_PRESSED
import com.unicorns.invisible.no65.saveload.GlobalState

class EventRevCorSocket : EventPlaced({ manager ->
    if (!GlobalState.getBoolean(manager.activity, REVOLUTION_RED_BUTTON_PRESSED)) {
        manager.wrapCutscene {
            drawer.showMessagesPhoneWithUnknownHead(listOf(
                R.string.lands_rev_cor_socket_1,
                R.string.lands_rev_cor_socket_2,
                R.string.lands_rev_cor_socket_3,
                R.string.lands_rev_cor_socket_4,
                R.string.lands_rev_cor_socket_5,
                R.string.lands_rev_cor_socket_6,
                R.string.lands_rev_cor_socket_7,
                R.string.lands_rev_cor_socket_8,
            ))
        }
        GlobalState.putBoolean(manager.activity, REVOLUTION_RED_BUTTON_PRESSED, true)
    }
    manager.activity.exitGame()
}) {
    override fun getConditionToFire(manager: LandsManager): Boolean {
        return firingCell is Source
    }
}