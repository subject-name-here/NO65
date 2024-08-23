package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventOppositionMacGuffinDestroyed
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventDestroyMacGuffin
import kotlinx.serialization.Serializable


@Serializable
class MacGuffinOpposition(override var cellBelow: Cell) : MacGuffin() {
    override val symbolColor
        get() = R.color.yellow

    override val destroyEvent: Event
        get() = EventDestroyMacGuffin(this).then(EventOppositionMacGuffinDestroyed())
}