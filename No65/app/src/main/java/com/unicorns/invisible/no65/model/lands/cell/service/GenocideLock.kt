package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellSemiStatic
import kotlinx.serialization.Serializable


@Serializable
class GenocideLock(override var cellBelow: Cell): CellSemiStatic() {
    override val symbol: Char
        get() = 'ҋ'
    override val symbolColor
        get() = R.color.dark_grey
    override val backgroundColor
        get() = R.color.almost_white

    override fun isPassable(): Boolean = true
}