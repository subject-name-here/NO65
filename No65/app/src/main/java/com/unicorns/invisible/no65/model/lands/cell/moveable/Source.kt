package com.unicorns.invisible.no65.model.lands.cell.moveable

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.control.Socket
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.Serializable

@Serializable
class Source(override var cellBelow: Cell): CellNonStaticMoveable() {
    override val initCoordinates: Coordinates = cellBelow.coordinates

    override val symbol: Char
        get() = if (cellBelow is Socket) 'ђ' else 'ӱ'
    override val symbolColor: Int
        get() = R.color.black
}