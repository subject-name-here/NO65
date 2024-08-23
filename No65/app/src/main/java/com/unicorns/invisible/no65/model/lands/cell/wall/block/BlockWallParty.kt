package com.unicorns.invisible.no65.model.lands.cell.wall.block

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.util.choose
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class BlockWallParty(override var cellBelow: Cell): BlockWall() {
    override val symbolColor
        get() = R.color.purple

    override val backgroundColor
        get() = currentBackGroundColor

    @Transient
    private var currentBackGroundColor: Int = R.color.true_yellow
    override fun onTick(tick: Int) {
        val frequency = LandsManager.TICKS_PER_SECOND / 3
        if (tick % frequency == 0) {
            currentBackGroundColor = choose(R.color.red, R.color.true_yellow, R.color.true_green, R.color.blue)
        }
    }
}