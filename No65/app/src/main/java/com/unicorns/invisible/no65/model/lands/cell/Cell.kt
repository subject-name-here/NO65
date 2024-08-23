package com.unicorns.invisible.no65.model.lands.cell

import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.Serializable

@Serializable
sealed class Cell {
    open fun onTick(tick: Int) {}

    abstract val coordinates: Coordinates
    abstract val symbol: Char
    abstract val symbolColor: Int
    abstract val backgroundColor: Int
}