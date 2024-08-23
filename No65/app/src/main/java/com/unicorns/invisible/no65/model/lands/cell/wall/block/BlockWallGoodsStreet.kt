package com.unicorns.invisible.no65.model.lands.cell.wall.block

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class BlockWallGoodsStreet(override var cellBelow: Cell): BlockWall() {
    override val symbolColor
        get() = R.color.dark_blue

    override val backgroundColor
        get() = currentBackGroundColor

    @Transient
    private var currentBackGroundColor: Int = R.color.dark_red
    override fun onTick(tick: Int) {
        val frequency = LandsManager.TICKS_PER_SECOND
        val list = listOf(R.color.dark_red, R.color.yellow, R.color.green)
        if (tick % frequency == 0) {
            currentBackGroundColor = list[(tick / frequency) % list.size]
        }
    }
}