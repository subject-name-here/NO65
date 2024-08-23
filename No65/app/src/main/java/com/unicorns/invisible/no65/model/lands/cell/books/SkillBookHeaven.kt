package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.events.skills.EventTimebackAcquired
import kotlinx.serialization.Serializable

@Serializable
class SkillBookHeaven(override var cellBelow: Cell): SkillBook() {
    override val symbolColor
        get() = R.color.black
    override val backgroundColor
        get() = R.color.white

    override val symbol: Char
        get() = 'Ӕ'

    override val acquiredEvent
        get() = EventTimebackAcquired(this)
}