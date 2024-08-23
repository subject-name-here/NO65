package com.unicorns.invisible.no65.model.lands.cell.floor

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import kotlinx.serialization.Serializable

@Serializable
class CellFloorRed(override var cellBelow: Cell): CellFloor() {
    override val floorColor
        get() = R.color.red
}