package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Arrow1(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.DOWN
) {
    override var damage: Int = 1
    override fun getString(): String = "ҕ"
    override fun getStringColor(): Int = R.color.true_green
}