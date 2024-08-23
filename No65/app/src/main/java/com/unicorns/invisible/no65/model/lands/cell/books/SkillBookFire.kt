package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.events.skills.EventFieryAnkhAcquired
import kotlinx.serialization.Serializable

@Serializable
class SkillBookFire(override var cellBelow: Cell): SkillBook() {
    override val symbolColor
        get() = R.color.red
    override val backgroundColor
        get() = R.color.light_grey

    override val symbol: Char
        get() = 'ӓ'

    override val acquiredEvent
        get() = EventFieryAnkhAcquired(this)
}