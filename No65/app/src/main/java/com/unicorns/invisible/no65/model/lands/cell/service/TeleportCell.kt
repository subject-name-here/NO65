package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.cell.CellPassable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.EventTeleport
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
class TeleportCell(override var cellBelow: Cell): CellPassable() {
    override val symbol
        get() = 'ӭ'
    override val symbolColor
        get() = R.color.white
    override val backgroundColor
        get() = R.color.black

    @EncodeDefault
    var toMapIndex = -1
    @EncodeDefault
    var toCoordinates = Coordinates.ZERO

    override fun onStep() = EventFactory.createWithNext { manager ->
        val occupier = manager.gameState.currentMap.getTopCell(coordinates)
        if (occupier is CellNonEmpty) {
            EventTeleport(toMapIndex, toCoordinates, occupier)
        } else {
            Event.Null
        }
    }
}