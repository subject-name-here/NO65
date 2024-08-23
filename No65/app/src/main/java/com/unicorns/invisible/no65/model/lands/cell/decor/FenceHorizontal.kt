package com.unicorns.invisible.no65.model.lands.cell.decor

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import kotlinx.serialization.Serializable

@Serializable
class FenceHorizontal(override var cellBelow: Cell): CellStaticOnPassableDecor() {
    override val symbol: Char
        get() = 'Ӆ'
    override val symbolColor
        get() = R.color.grey
}