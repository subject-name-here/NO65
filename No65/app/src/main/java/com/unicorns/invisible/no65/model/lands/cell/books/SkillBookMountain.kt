package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.events.skills.EventAvalancheAcquired
import kotlinx.serialization.Serializable

@Serializable
class SkillBookMountain(override var cellBelow: Cell): SkillBook() {
    override val symbolColor
        get() = R.color.white
    override val backgroundColor
        get() = R.color.light_grey

    override val symbol: Char
        get() = 'Ӗ'

    override val acquiredEvent
        get() = EventAvalancheAcquired(this)
}