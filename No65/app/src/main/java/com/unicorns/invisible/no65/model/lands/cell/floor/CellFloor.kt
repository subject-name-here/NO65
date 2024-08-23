package com.unicorns.invisible.no65.model.lands.cell.floor

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.CellPassable
import kotlinx.serialization.Serializable

@Serializable
abstract class CellFloor: CellPassable() {
    final override val symbol: Char
        get() = '+'

    var litUp = false

    abstract val floorColor: Int
    final override val backgroundColor
        get() = when {
            litUp -> R.color.white
            else -> floorColor
        }
    final override val symbolColor
        get() = when {
            litUp -> R.color.white
            else -> floorColor
        }
}