package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Viking(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.LEFT
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 2
    override fun getString() = "ғ"
    override fun getStringColor(): Int = R.color.brown
}