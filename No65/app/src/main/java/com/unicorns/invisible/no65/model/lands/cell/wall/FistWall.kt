package com.unicorns.invisible.no65.model.lands.cell.wall

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStatic
import kotlinx.serialization.Serializable

@Serializable
class FistWall(override var cellBelow: Cell): CellStatic() {
    override val symbol
        get() = 'ӵ'

    override val symbolColor
        get() = R.color.light_grey
    override val backgroundColor
        get() = R.color.black
}