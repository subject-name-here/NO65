package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.events.skills.EventWaterballAcquired
import kotlinx.serialization.Serializable

@Serializable
class SkillBookWater(override var cellBelow: Cell): SkillBook() {
    override val symbolColor
        get() = R.color.blue
    override val backgroundColor
        get() = R.color.waterball_background_color

    override val symbol: Char
        get() = 'Ә'

    override val acquiredEvent
        get() = EventWaterballAcquired(this)
}