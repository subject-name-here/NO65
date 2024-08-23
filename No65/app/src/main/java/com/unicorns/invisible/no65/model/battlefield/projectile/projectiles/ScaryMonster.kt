package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class ScaryMonster(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.RIGHT
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 2
    override fun getString(): String = "Є"
    override fun getStringColor(): Int = R.color.red
}
