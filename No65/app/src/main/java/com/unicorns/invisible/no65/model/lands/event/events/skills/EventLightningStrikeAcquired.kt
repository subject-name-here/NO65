package com.unicorns.invisible.no65.model.lands.event.events.skills

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.lands.cell.books.SkillBook

class EventLightningStrikeAcquired(bookCell: SkillBook): EventSkillAcquired(bookCell) {
    override val skillNameId: Int
        get() = R.string.skills_lightning_strike_name
    override val effectDescriptionId: Int
        get() = R.string.skills_lightning_strike_description
    override val trigram: Trigram
        get() = Thunder
}