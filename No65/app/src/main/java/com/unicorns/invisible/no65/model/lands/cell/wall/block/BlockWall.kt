package com.unicorns.invisible.no65.model.lands.cell.wall.block

import com.unicorns.invisible.no65.model.lands.cell.CellStatic
import kotlinx.serialization.Serializable

@Serializable
abstract class BlockWall: CellStatic() {
    final override val symbol: Char
        get() = 'Ќ'
}