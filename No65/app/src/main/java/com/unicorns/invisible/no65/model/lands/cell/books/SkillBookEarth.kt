package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.events.skills.EventPlateauUppercutAcquired
import kotlinx.serialization.Serializable

@Serializable
class SkillBookEarth(override var cellBelow: Cell): SkillBook() {
    override val symbolColor
        get() = R.color.green
    override val backgroundColor
        get() = R.color.brown

    override val symbol: Char
        get() = 'Ӓ'

    override val acquiredEvent
        get() = EventPlateauUppercutAcquired(this)
}