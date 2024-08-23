package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Battery(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    override fun getString(): String = "Ў"
    override fun getStringColor(): Int = R.color.grey
}