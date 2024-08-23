package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.events.skills.EventInfiniteTroubleAcquired
import kotlinx.serialization.Serializable

@Serializable
class SkillBookWindRequiem(override var cellBelow: Cell): SkillBookRequiem() {
    override val symbolColor
        get() = if (isTrueColors) R.color.light_grey else R.color.anti_light_grey
    override val backgroundColor
        get() = if (isTrueColors) R.color.light_blue else R.color.anti_light_blue

    override val symbol: Char
        get() = 'ә'

    override val acquiredEvent
        get() = EventInfiniteTroubleAcquired(this)
}