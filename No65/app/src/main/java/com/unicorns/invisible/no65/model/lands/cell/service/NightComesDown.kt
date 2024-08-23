package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.AT_STARTED_SHOWDOWN
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellControl
import com.unicorns.invisible.no65.model.lands.cell.CellPassable
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.EventTeleport
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventATShowdownHit
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventCollidedWithATGhostGrub
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.Serializable

@Serializable
class NightComesDown(override var cellBelow: Cell) : CellStaticOnPassableDecor(), CellControl {
    override val symbol: Char
        get() = 'Ѯ'
    override val symbolColor: Int
        get() = R.color.white


    var direction = Direction.IDLE

    override fun onTickWithEvent(tick: Int): Event {
        return EventFactory.createWithNext lambda@ { manager ->
            val resultEvent = Event.Null

            val map = manager.gameState.currentMap
            val delta = direction.getDelta()
            val newCoordinates = coordinates + delta
            val occupier = map.getTopCell(newCoordinates)
            if (occupier is CellProtagonist) {
                resultEvent.then(EventTeleport(-1, Coordinates.ZERO, occupier))
                if (AT_STARTED_SHOWDOWN in manager.gameState.flagsMaster) {
                    resultEvent.then(EventATShowdownHit())
                } else {
                    resultEvent.then(EventCollidedWithATGhostGrub())
                }
            }
            if (occupier is CellPassable) {
                map.moveOnDelta(this@NightComesDown, delta)
            } else {
                map.removeCellFromTop(this@NightComesDown)
            }

            resultEvent
        }
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT, IDLE;

        fun getDelta(): Coordinates {
            return when (this) {
                UP -> Coordinates(-1, 0)
                DOWN -> Coordinates(1, 0)
                LEFT -> Coordinates(0, -1)
                RIGHT -> Coordinates(0, 1)
                IDLE -> Coordinates.ZERO
            }
        }
    }
}