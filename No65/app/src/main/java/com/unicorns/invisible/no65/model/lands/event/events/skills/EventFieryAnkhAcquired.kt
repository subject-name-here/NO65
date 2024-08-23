package com.unicorns.invisible.no65.model.lands.event.events.skills

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.lands.cell.books.SkillBook

class EventFieryAnkhAcquired(bookCell: SkillBook): EventSkillAcquired(bookCell) {
    override val skillNameId: Int
        get() = R.string.skills_fiery_ankh_name
    override val effectDescriptionId: Int
        get() = R.string.skills_fiery_ankh_description
    override val trigram: Trigram
        get() = Fire
}