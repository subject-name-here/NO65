package com.unicorns.invisible.no65.model.lands.cell.decor

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventShowMessages
import kotlinx.serialization.Serializable

@Serializable
class DecorDeadBody(override var cellBelow: Cell): CellStaticOnPassableDecor(), CellUsable {
    override val symbol: Char
        get() = 'Ӧ'
    override val symbolColor
        get() = R.color.black

    override fun use(): Event {
        return EventShowMessages(listOf(R.string.lands_fake_dead_body))
    }
}