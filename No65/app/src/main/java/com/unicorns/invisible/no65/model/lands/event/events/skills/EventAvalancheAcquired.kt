package com.unicorns.invisible.no65.model.lands.event.events.skills

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.lands.cell.books.SkillBook

class EventAvalancheAcquired(bookCell: SkillBook): EventSkillAcquired(bookCell) {
    override val skillNameId: Int
        get() = R.string.skills_avalanche_name
    override val effectDescriptionId: Int
        get() = R.string.skills_avalanche_description
    override val trigram: Trigram
        get() = Mountain
}