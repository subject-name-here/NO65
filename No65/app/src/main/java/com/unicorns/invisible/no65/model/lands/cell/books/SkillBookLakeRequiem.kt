package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.events.skills.EventQueenLakeAcquired
import kotlinx.serialization.Serializable

@Serializable
class SkillBookLakeRequiem(override var cellBelow: Cell): SkillBookRequiem() {
    override val symbolColor
        get() = if (isTrueColors) R.color.pink else R.color.anti_pink
    override val backgroundColor
        get() = if (isTrueColors) R.color.purple else R.color.anti_purple

    override val symbol: Char
        get() = 'ӕ'

    override val acquiredEvent
        get() = EventQueenLakeAcquired(this)
}