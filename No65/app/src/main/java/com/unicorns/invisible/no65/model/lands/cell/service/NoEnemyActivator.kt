package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStatic
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttackNoEnemy
import kotlinx.serialization.Serializable


@Serializable
class NoEnemyActivator(override var cellBelow: Cell): CellStatic(), CellUsable {
    override val symbolColor
        get() = R.color.white
    override val backgroundColor
        get() = R.color.black
    override val symbol: Char
        get() = if (!isActive) '0' else ' '

    private var isActive = false
    override fun use(): Event = if (isActive) {
        Event.Null
    } else {
        isActive = true
        EventAttackNoEnemy()
    }
}