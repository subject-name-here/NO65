package com.unicorns.invisible.no65.model.lands.cell.books

import com.unicorns.invisible.no65.LandsManager.Companion.TICKS_PER_SECOND

abstract class SkillBookRequiem: SkillBook() {
    protected var isTrueColors = false
    override fun onTick(tick: Int) {
        isTrueColors = tick % TICKS_PER_SECOND < TICKS_PER_SECOND / 2
    }
}