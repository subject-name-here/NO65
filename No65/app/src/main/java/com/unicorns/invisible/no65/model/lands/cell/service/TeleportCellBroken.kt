package com.unicorns.invisible.no65.model.lands.cell.service

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStatic
import com.unicorns.invisible.no65.util.getNthBit
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
class TeleportCellBroken(override var cellBelow: Cell): CellStatic() {
    override val symbol: Char
        get() = 'ӄ'
    override val symbolColor
        get() = if (isOkay) {
            R.color.white
        } else {
            R.color.red
        }
    override val backgroundColor
        get() = R.color.black

    @Transient
    private var isOkay = false
    override fun onTick(tick: Int) {
        val periodNumber = tick / (LandsManager.TICKS_PER_SECOND / FREQUENCY)
        isOkay = getNthBit(BLINK_MASK, periodNumber % BLINK_MASK_SIZE) == 1
    }
    companion object {
        const val BLINK_MASK = 99
        const val BLINK_MASK_SIZE = 8
        const val FREQUENCY = 2
    }
}