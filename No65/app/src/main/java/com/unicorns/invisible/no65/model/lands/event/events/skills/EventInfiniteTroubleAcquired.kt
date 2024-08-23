package com.unicorns.invisible.no65.model.lands.event.events.skills

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.model.lands.cell.books.SkillBook

class EventInfiniteTroubleAcquired(bookCell: SkillBook): EventSkillAcquired(bookCell) {
    override val skillNameId: Int
        get() = R.string.skills_infinite_trouble_name
    override val effectDescriptionId: Int
        get() = R.string.skills_infinite_trouble_description
    override val trigram: Trigram
        get() = Wind
    override val isRequiem: Boolean
        get() = true
}