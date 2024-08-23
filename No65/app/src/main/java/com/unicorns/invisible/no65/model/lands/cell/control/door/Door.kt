package com.unicorns.invisible.no65.model.lands.cell.control.door

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.CellControl
import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.cell.CellSemiStatic
import com.unicorns.invisible.no65.model.lands.cell.control.Socket
import com.unicorns.invisible.no65.model.lands.cell.moveable.Source
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.map.LandsMap
import kotlinx.serialization.Transient


abstract class Door: CellSemiStatic(), CellControl {
    private var doorState = DoorState.UNLOCKED
    protected abstract val stateToSymbol: Map<DoorState, Char>

    override val symbol: Char
        get() = stateToSymbol[doorState] ?: '+'

    override val symbolColor: Int
        get() = R.color.black
    override val backgroundColor: Int
        get() = R.color.white

    open var id: Int = -1
    open var mode: DoorMode = DoorMode.ALL

    override fun isPassable(): Boolean {
        return doorState == DoorState.UNLOCKED
    }

    private fun open() {
        doorState = DoorState.UNLOCKED
    }
    private fun close(map: LandsMap) {
        val topCell = map.getTopCell(coordinates)
        doorState = if (topCell == this) {
            DoorState.LOCKED
        } else {
            DoorState.UNLOCKED
        }
    }

    override fun onTickWithEvent(tick: Int): Event = EventFactory.create lambda@ { manager ->
        val map = manager.gameState.currentMap
        if (!searchConducted) {
            findConnectedSockets(map)
        }

        for (socket in connectedSocketList) {
            val connected = manager.gameState.currentMap.getTopCell(socket.coordinates) is Source
            when {
                mode == DoorMode.AT_LEAST_ONE && connected -> {
                    open()
                    return@lambda
                }
                mode == DoorMode.ALL && !connected -> {
                    close(map)
                    return@lambda
                }
            }
        }

        when (mode) {
            DoorMode.AT_LEAST_ONE -> {
                close(map)
            }
            DoorMode.ALL -> {
                open()
            }
        }
    }

    @Transient
    private val connectedSocketList = ArrayList<Socket>()
    @Transient
    private var searchConducted = false
    private fun findConnectedSockets(map: LandsMap) {
        if (id == -1 || searchConducted) {
            return
        }

        val topCells = map.getTopCells()
        for (topCell in topCells) {
            var currentCell = topCell
            while (currentCell is CellNonEmpty) {
                if (currentCell is Socket && currentCell.id == id) {
                    connectedSocketList.add(currentCell)
                    break
                }
                currentCell = currentCell.cellBelow
            }
        }
        searchConducted = true
    }
}