package com.unicorns.invisible.no65.model.lands.cell

abstract class CellNonStatic: CellNonEmpty() {
    final override val backgroundColor: Int
        get() = cellBelow.backgroundColor
}