package com.unicorns.invisible.no65.model.lands.cell

import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.Serializable

@Serializable
sealed class CellNonEmpty: Cell() {
    abstract var cellBelow: Cell

    final override val coordinates: Coordinates
        get() = cellBelow.coordinates
}