package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventTheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.events.util.EventRemoveCell
import kotlinx.serialization.Serializable

@Serializable
class TheMarryingMaidenHolder(override var cellBelow: Cell) : CellStaticOnPassableDecor(), CellUsable {
    override val symbol: Char
        get() = 'Қ'
    override val symbolColor: Int
        get() = R.color.pink

    override fun use(): Event {
        return EventRemoveCell(this).then(EventTheMarryingMaiden(coordinates))
    }
}