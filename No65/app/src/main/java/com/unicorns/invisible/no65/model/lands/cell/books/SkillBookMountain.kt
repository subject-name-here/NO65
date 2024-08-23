package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellControl
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.EventTeleport
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventShowMessages
import com.unicorns.invisible.no65.model.lands.event.events.skills.EventAvalancheAcquired
import com.unicorns.invisible.no65.util.CellUtils
import kotlinx.serialization.Serializable

@Serializable
class SkillBookMountain(override var cellBelow: Cell): SkillBook(), CellControl {
    override val symbolColor
        get() = R.color.white
    override val backgroundColor
        get() = R.color.light_grey

    override val symbol: Char
        get() = 'Ӗ'

    override val acquiredEvent
        get() = EventAvalancheAcquired(this)

    override fun onTickWithEvent(tick: Int): Event = EventFactory.createWithNext { manager ->
        if (manager.gameState.currentMapIndex != "map_jal_of2") return@createWithNext Event.Null

        if (this@SkillBookMountain !in CellUtils.getCellsInSight(manager)) {
            val coordinatesToReappear = CellUtils.findCurrentMapClosestFloor(manager, this@SkillBookMountain)
            if (coordinatesToReappear != null) {
                return@createWithNext EventTeleport(-1, coordinatesToReappear, manager.gameState.protagonist)
                    .then(EventShowMessages(listOf(R.string.lands_skill_book_mountain_force_pick_up)))
            }
        }
        Event.Null
    }
}