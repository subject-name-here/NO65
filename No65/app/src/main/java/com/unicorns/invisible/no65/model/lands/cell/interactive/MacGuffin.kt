package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event


abstract class MacGuffin: CellStaticOnPassableDecor(), CellUsable {
    protected open var triggerState = MacGuffinState.WHOLE

    override val symbol: Char
        get() = when (triggerState) {
            MacGuffinState.WHOLE -> 'һ'
            MacGuffinState.TOUCHED -> 'ӽ'
            MacGuffinState.ALMOST_BROKEN -> 'Ӿ'
            MacGuffinState.BROKEN -> 'ӛ'
            MacGuffinState.NON_EXISTENT -> ' '
        }

    protected abstract val destroyEvent: Event
    override fun use(): Event {
        triggerState = triggerState.next()
        if (triggerState == MacGuffinState.NON_EXISTENT) {
            return destroyEvent
        }
        return Event.Null
    }

    enum class MacGuffinState {
        WHOLE,
        TOUCHED,
        ALMOST_BROKEN,
        BROKEN,
        NON_EXISTENT;

        fun next(): MacGuffinState {
            return when (this) {
                WHOLE -> TOUCHED
                TOUCHED -> ALMOST_BROKEN
                ALMOST_BROKEN -> BROKEN
                BROKEN -> NON_EXISTENT
                NON_EXISTENT -> NON_EXISTENT
            }
        }
    }
}