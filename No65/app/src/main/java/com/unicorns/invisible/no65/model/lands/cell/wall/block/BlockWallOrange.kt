package com.unicorns.invisible.no65.model.lands.cell.wall.block

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import kotlinx.serialization.Serializable

@Serializable
class BlockWallOrange(override var cellBelow: Cell): BlockWall() {
    override val symbolColor
        get() = R.color.orange

    override val backgroundColor
        get() = R.color.almost_black
}