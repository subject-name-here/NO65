package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventAlienSpaceshipControl
import kotlinx.serialization.Serializable

@Serializable
class AlienSpaceshipControls(override var cellBelow: Cell): CellStaticOnPassableDecor(), CellUsable {
    override val symbol: Char
        get() = 'Ӄ'
    override val symbolColor
        get() = when (state) {
            State.READY -> R.color.true_green
            State.NOT_READY_RED -> R.color.red
            State.NOT_READY_ORANGE -> R.color.orange
        }

    var state = State.NOT_READY_RED
        set(value) {
            if (!locked) {
                field = value
            }
        }
    var locked = false

    override fun use(): Event {
        return EventAlienSpaceshipControl(this)
    }

    override fun onTick(tick: Int) {
        if (tick % LandsManager.TICKS_PER_SECOND == 0) {
            state = state.nextTransitionState()
        }
    }

    enum class State {
        READY,
        NOT_READY_RED,
        NOT_READY_ORANGE;

        fun nextOnPressState(): State {
            return when (this) {
                READY -> NOT_READY_RED
                NOT_READY_RED, NOT_READY_ORANGE -> READY
            }
        }

        fun nextTransitionState(): State {
            return when (this) {
                READY -> READY
                NOT_READY_RED -> NOT_READY_ORANGE
                NOT_READY_ORANGE -> NOT_READY_RED
            }
        }
    }
}