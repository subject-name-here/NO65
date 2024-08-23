package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredCounters.Companion.WORLD_TRACE_NUMBER
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellControl
import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.cell.CellSemiStatic
import com.unicorns.invisible.no65.model.lands.cell.interactive.MacGuffin
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.EventTeleport
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.isCloseTo
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable


@OptIn(ExperimentalSerializationApi::class)
@Serializable
class TeleportCellSecret(override var cellBelow: Cell): CellSemiStatic(), CellControl {
    override val symbol
        get() = 'ӭ'

    private var state = State.UNPASSABLE

    override val symbolColor
        get() = when (state) {
            State.PASSABLE -> R.color.white
            State.UNPASSABLE -> R.color.black
        }
    override val backgroundColor
        get() = R.color.black

    @EncodeDefault
    var toMapIndex = -1
    @EncodeDefault
    var toCoordinates = Coordinates.ZERO

    override fun isPassable(): Boolean {
        return state == State.PASSABLE
    }

    override fun onStep() = EventFactory.createWithNext { manager ->
        val occupier = manager.gameState.currentMap.getTopCell(coordinates)
        if (occupier is CellNonEmpty) {
            EventTeleport(toMapIndex, toCoordinates, occupier)
        } else {
            Event.Null
        }
    }

    override fun onTickWithEvent(tick: Int): Event {
        return if (state == State.PASSABLE) {
            Event.Null
        } else {
            EventFactory.create { manager ->
                val traceNumber = manager.gameState.countersMaster[WORLD_TRACE_NUMBER]
                when (manager.gameState.currentMapIndex) {
                    "map_fst_sf0" -> {
                        if (traceNumber.isCloseTo(43, 6)) {
                            state = State.PASSABLE
                        }
                    }
                    "map_fst_ror_ur2" -> {
                        if (traceNumber.isCloseTo(907, 5)) {
                            state = State.PASSABLE
                        }
                    }
                    "map_str_48" -> {
                        if (traceNumber.isCloseTo(26, 4)) {
                            state = State.PASSABLE
                        }
                    }
                    "map_gds_wh4" -> {
                        val macGuffins = manager.gameState.currentMap
                            .getTopCells()
                            .filterIsInstance<MacGuffin>()
                        if (traceNumber.isCloseTo(8, 3) && macGuffins.isEmpty()) {
                            state = State.PASSABLE
                        }
                    }
                }
            }
        }
    }

    enum class State {
        PASSABLE,
        UNPASSABLE
    }
}