package com.unicorns.invisible.no65.util

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.cell.floor.CellFloor
import com.unicorns.invisible.no65.model.lands.map.LandsMap
import kotlinx.coroutines.delay

class CellUtils {
    companion object {
        fun distanceToProtagonist(
            manager: LandsManager,
            coordinates: Coordinates
        ): Int {
            val diff = manager.gameState.protagonist.coordinates - coordinates
            return diff.abs()
        }

        fun getCellsInSight(manager: LandsManager): List<Cell> = with(manager) {
            val map = gameState.currentMap
            val center = gameState.protagonist.coordinates
            val cellsList = ArrayList<Cell>()
            repeat(landsHeight) { r ->
                repeat(landsWidth) { c ->
                    val mapCoordinates = getMapCoordinatesByScreenCoordinates(
                        landsWidth,
                        landsHeight,
                        Coordinates(r, c),
                        center
                    )
                    cellsList.add(map.getTopCell(mapCoordinates))
                }
            }
            return cellsList
        }

        fun findCurrentMapClosestFloor(manager: LandsManager, origin: Cell): Coordinates? {
            return manager.gameState.currentMap.getTopCells()
                .filterIsInstance<CellFloor>()
                .map { it.coordinates }
                .minByOrNull {
                    val diff = it - origin.coordinates
                    diff.abs()
                }
        }

        fun litUpCurrentMapFloors(manager: LandsManager) {
            val cells = manager.gameState.currentMap.getTopCells()
            for (cell in cells) {
                when (cell) {
                    is CellFloor -> {
                        cell.litUp = true
                    }
                    is CellNonEmpty -> {
                        val cellBelow = cell.cellBelow
                        if (cellBelow is CellFloor) {
                            cellBelow.litUp = true
                        }
                    }
                    else -> {}
                }
            }
        }

        suspend fun moveOnTrajectory(
            trajectory: List<Coordinates>,
            cell: CellNonEmpty,
            map: LandsMap,
            delayBetweenMoves: Long
        ) {
            for (coordinates in trajectory) {
                map.moveTo(cell, coordinates)
                delay(delayBetweenMoves)
            }
        }
    }
}