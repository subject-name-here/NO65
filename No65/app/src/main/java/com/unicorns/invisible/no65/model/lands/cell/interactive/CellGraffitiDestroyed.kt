package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventShowMessageMultiline
import kotlinx.serialization.Serializable

@Serializable
class CellGraffitiDestroyed(override var cellBelow: Cell): CellStaticOnPassableDecor(), CellUsable {
    override val symbol
        get() = '¤'
    override val symbolColor
        get() = R.color.blurple

    override fun use(): Event {
        return EventShowMessageMultiline(R.string.graffiti_destroyed)
    }
}