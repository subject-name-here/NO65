package com.unicorns.invisible.no65.model.lands.map

import com.unicorns.invisible.no65.model.lands.cell.*
import com.unicorns.invisible.no65.model.lands.cell.moveable.CellNonStaticMoveable
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.view.lands.NumberToStringId.Companion.MISSING_NAME_ID
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


@Serializable
class LandsMap(var name: String = "", private val map: MutableMap<Coordinates, CellNonEmpty> = HashMap()) {
    var visited = false
    var nameId = MISSING_NAME_ID

    @Transient
    private val mapLock = ReentrantReadWriteLock()

    fun getTopCell(coordinates: Coordinates): Cell {
        val cell = mapLock.read { map[coordinates] }
        return cell ?: CellEmpty(coordinates)
    }

    fun addCellOnTop(cell: Cell, coordinates: Coordinates) {
        if (cell is CellNonEmpty) {
            cell.cellBelow = getTopCell(coordinates)
            mapLock.write {
                map[coordinates] = cell
            }
        } else {
            mapLock.write {
                map.remove(coordinates)
            }
        }
    }
    fun <T : CellNonEmpty> createCellOnTop(coordinates: Coordinates, klass: KClass<T>): T {
        val newCell = klass.primaryConstructor!!.call(getTopCell(coordinates))
        mapLock.write {
            map[coordinates] = newCell
        }
        return newCell
    }

    fun removeCellFromTop(cell: CellNonEmpty) {
        val coordinates = cell.coordinates
        if (getTopCell(coordinates) != cell) {
            return
        }

        val cellBelow = cell.cellBelow
        if (cellBelow is CellNonEmpty) {
            mapLock.write {
                map[coordinates] = cellBelow
            }
        } else {
            mapLock.write {
                map.remove(coordinates)
            }
        }
    }
    fun clearCell(coordinates: Coordinates) {
        mapLock.write {
            map.remove(coordinates)
        }
    }

    fun moveTo(cell: CellNonEmpty, newCoordinates: Coordinates): Boolean {
        val opposingCell = getTopCell(newCoordinates)
        if (opposingCell is CellPassable || opposingCell is CellSemiStatic && opposingCell.isPassable()) {
            moveCellToRaw(cell, newCoordinates)
            return true
        }
        return false
    }
    fun moveOnDelta(cell: CellNonEmpty, delta: Coordinates): Boolean {
        val newCoordinates = cell.coordinates + delta
        if (moveTo(cell, newCoordinates)) {
            return true
        }

        val opposingCell = getTopCell(newCoordinates)
        if (opposingCell is CellNonStaticMoveable) {
            if (moveOnDelta(opposingCell, delta)) {
                moveCellToRaw(cell, newCoordinates)
                return true
            }
        }

        return false
    }
    private fun moveCellToRaw(cell: CellNonEmpty, newCoordinates: Coordinates) {
        removeCellFromTop(cell)
        addCellOnTop(cell, newCoordinates)
    }

    fun getTopCells(): List<Cell> {
        return mapLock.read { map.values.toList() }
    }
}