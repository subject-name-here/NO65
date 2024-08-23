package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBooleanPercent


class BCSpaceDistorterSpawner(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override fun getString(): String = "ӻ"
    override fun getStringColor(): Int = R.color.red
    override val priority: Int = 5

    private var initTick = -1
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)

        if (initTick == -1) {
            initTick = tickNumber
        } else {
            battleField.removeProjectile(this)
            val distorter = BCSpaceDistorter(position)
            if (randBooleanPercent(30)) {
                distorter.increaseCharge()
            }
            battleField.addProjectile(distorter)
        }
    }
}