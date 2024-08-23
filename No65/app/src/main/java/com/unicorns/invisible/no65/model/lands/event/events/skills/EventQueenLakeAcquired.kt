package com.unicorns.invisible.no65.model.lands.event.events.skills

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.lands.cell.books.SkillBook

class EventQueenLakeAcquired(bookCell: SkillBook): EventSkillAcquired(bookCell) {
    override val skillNameId: Int
        get() = R.string.skills_queen_lake_name
    override val effectDescriptionId: Int
        get() = R.string.skills_queen_lake_description
    override val trigram: Trigram
        get() = Lake
    override val isRequiem: Boolean
        get() = true
}