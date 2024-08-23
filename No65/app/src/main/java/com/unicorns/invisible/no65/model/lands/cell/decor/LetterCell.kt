package com.unicorns.invisible.no65.model.lands.cell.decor

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import kotlinx.serialization.Serializable

@Serializable
class LetterCell(override var cellBelow: Cell): CellStaticOnPassableDecor() {
    override var symbol: Char = '?'
    override val symbolColor
        get() = R.color.black
}