package com.unicorns.invisible.no65.model.lands.cell


abstract class CellStaticOnPassableDecor: CellStaticOnPassable() {
    final override val backgroundColor: Int
        get() = cellBelow.backgroundColor
}