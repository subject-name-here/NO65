package com.unicorns.invisible.no65.model.lands.event.events.skills

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.model.lands.cell.books.SkillBook

class EventWaterballAcquired(bookCell: SkillBook): EventSkillAcquired(bookCell) {
    override val skillNameId: Int
        get() = R.string.skills_waterball_name
    override val effectDescriptionId: Int
        get() = R.string.skills_waterball_description
    override val trigram: Trigram
        get() = Water
}