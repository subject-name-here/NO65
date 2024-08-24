package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventTLFOnLightningStrikeAcquired
import com.unicorns.invisible.no65.model.lands.event.events.skills.EventLightningStrikeAcquired
import kotlinx.serialization.Serializable

@Serializable
class SkillBookThunder(override var cellBelow: Cell): SkillBook() {
    override val symbolColor
        get() = R.color.purple
    override val backgroundColor
        get() = R.color.true_yellow

    override val symbol: Char
        get() = 'ӗ'

    override val acquiredEvent
        get() = EventLightningStrikeAcquired(this@SkillBookThunder)
}