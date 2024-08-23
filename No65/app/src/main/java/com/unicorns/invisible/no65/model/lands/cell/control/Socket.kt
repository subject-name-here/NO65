package com.unicorns.invisible.no65.model.lands.cell.control

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellPassable
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
class Socket(override var cellBelow: Cell): CellPassable() {
    override val symbol
        get() = 'Ӳ'
    override val symbolColor
        get() = R.color.black
    override val backgroundColor
        get() = R.color.white

    @EncodeDefault
    var id = -1
}