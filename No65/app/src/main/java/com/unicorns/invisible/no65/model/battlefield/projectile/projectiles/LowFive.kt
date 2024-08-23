package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class LowFive(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override fun getString(): String = "Ӎ"
    override fun getStringColor(): Int = R.color.black
}