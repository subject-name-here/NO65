package com.unicorns.invisible.no65.model.lands.cell.wall

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStatic
import kotlinx.serialization.Serializable

@Serializable
class TempleWallInner(override var cellBelow: Cell): CellStatic() {
    override val symbol
        get() = 'ҥ'

    override val symbolColor
        get() = R.color.pink
    override val backgroundColor
        get() = R.color.lighter_blue
}