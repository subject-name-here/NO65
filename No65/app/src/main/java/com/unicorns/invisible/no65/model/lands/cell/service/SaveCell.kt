package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStatic
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.EventSave
import kotlinx.serialization.Serializable


@Serializable
class SaveCell(override var cellBelow: Cell): CellStatic(), CellUsable {
    override val symbol: Char
        get() = '&'
    override val symbolColor
        get() = R.color.black
    override val backgroundColor
        get() = R.color.orange

    override fun use(): Event {
        return EventSave()
    }
}