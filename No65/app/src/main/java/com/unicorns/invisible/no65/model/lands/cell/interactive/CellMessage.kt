package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventShowMessageMultiline
import com.unicorns.invisible.no65.view.lands.NumberToStringId

abstract class CellMessage : CellStaticOnPassableDecor(), CellUsable {
    abstract var state: State
    abstract var messageNumber: Int

    override fun use(): Event {
        state = State.CLICKED
        return EventShowMessageMultiline(NumberToStringId.getMessage(messageNumber))
    }

    enum class State {
        NOT_CLICKED,
        CLICKED
    }
}