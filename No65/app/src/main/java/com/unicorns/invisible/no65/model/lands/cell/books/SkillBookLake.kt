package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.events.skills.EventLadyLakeAcquired
import kotlinx.serialization.Serializable

@Serializable
class SkillBookLake(override var cellBelow: Cell): SkillBook() {
    override val symbolColor
        get() = R.color.pink
    override val backgroundColor
        get() = R.color.purple

    override val symbol: Char
        get() = 'ӕ'

    override val acquiredEvent
        get() = EventLadyLakeAcquired(this)
}