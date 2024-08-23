package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Paw(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 3 / 2
    override fun getString(): String = "Ҿ"
    override fun getStringColor(): Int = R.color.yellow
}