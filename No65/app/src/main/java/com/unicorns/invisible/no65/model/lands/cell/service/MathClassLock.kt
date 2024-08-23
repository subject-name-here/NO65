package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellControl
import com.unicorns.invisible.no65.model.lands.cell.CellSemiStatic
import com.unicorns.invisible.no65.model.lands.cell.control.Socket
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import kotlinx.serialization.Serializable

@Serializable
class MathClassLock(override var cellBelow: Cell): CellSemiStatic(), CellControl {
    private var state = LockState.LOCKED

    override val symbol: Char
        get() = when (state) {
            LockState.LOCKED -> 'Ґ'
            LockState.UNLOCKED -> 'ґ'
        }
    override val symbolColor
        get() = R.color.black
    override val backgroundColor
        get() = R.color.white
    override fun isPassable(): Boolean {
        return state == LockState.UNLOCKED
    }

    override fun onTickWithEvent(tick: Int): Event {
        return EventFactory.create { manager ->
            val map = manager.gameState.mapGraph.getMap("map_str_55")
            if (map.visited) {
                val sockets = map.getTopCells().filterIsInstance<Socket>()
                if (sockets.isEmpty()) {
                    state = LockState.UNLOCKED
                    return@create
                }
            }

            state = LockState.LOCKED
        }
    }

    enum class LockState {
        LOCKED,
        UNLOCKED
    }
}