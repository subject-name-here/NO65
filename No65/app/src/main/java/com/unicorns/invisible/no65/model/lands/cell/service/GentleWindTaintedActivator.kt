package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldGentleWindTainted
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStatic
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.cell.character.npc.GentleWind
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import kotlinx.serialization.Serializable

@Serializable
class GentleWindTaintedActivator(override var cellBelow: Cell): CellStatic(), CellUsable {
    override val symbolColor
        get() = R.color.purple
    override val backgroundColor
        get() = R.color.grey
    override val symbol: Char
        get() = 'ә'

    override fun use(): Event = EventFactory.createWithNext { manager ->
        val cell = manager.gameState.currentMap.getTopCells().filterIsInstance<GentleWind>()
        if (cell.isNotEmpty()) {
            EventAttack(cell[0], BattleFieldGentleWindTainted())
        } else {
            Event.Null
        }
    }
}