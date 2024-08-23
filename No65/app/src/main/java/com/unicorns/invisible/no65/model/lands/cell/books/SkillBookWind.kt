package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.events.skills.EventTripleTroubleAcquired
import kotlinx.serialization.Serializable

@Serializable
class SkillBookWind(override var cellBelow: Cell): SkillBook() {
    override val symbolColor
        get() = R.color.light_grey
    override val backgroundColor
        get() = R.color.light_blue

    override val symbol: Char
        get() = 'ә'

    override val acquiredEvent
        get() = EventTripleTroubleAcquired(this)
}