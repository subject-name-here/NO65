package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldDeliverance
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStatic
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Deliverance
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import kotlinx.serialization.Serializable

@Serializable
class DeliveranceActivator(override var cellBelow: Cell): CellStatic(), CellUsable {
    override val symbolColor
        get() = R.color.green
    override val backgroundColor
        get() = R.color.almost_white
    override val symbol: Char
        get() = '$'

    override fun use(): Event = EventFactory.createWithNext { manager ->
        val cell = manager.gameState.currentMap.getTopCells().filterIsInstance<Deliverance>()
        if (cell.isNotEmpty()) {
            EventAttack(cell[0], BattleFieldDeliverance(true))
        } else {
            Event.Null
        }
    }
}