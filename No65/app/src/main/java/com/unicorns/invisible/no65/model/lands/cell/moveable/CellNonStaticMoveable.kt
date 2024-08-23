package com.unicorns.invisible.no65.model.lands.cell.moveable

import com.unicorns.invisible.no65.model.lands.cell.CellNonStatic
import com.unicorns.invisible.no65.model.lands.map.LandsMap
import com.unicorns.invisible.no65.util.Coordinates

abstract class CellNonStaticMoveable: CellNonStatic() {
    abstract val initCoordinates: Coordinates
    fun restore(map: LandsMap) {
        map.removeCellFromTop(this)

        val occupant = map.getTopCell(initCoordinates)
        if (occupant is CellNonStatic) {
            map.moveTo(occupant, coordinates)
        }

        map.addCellOnTop(this, initCoordinates)
    }
}