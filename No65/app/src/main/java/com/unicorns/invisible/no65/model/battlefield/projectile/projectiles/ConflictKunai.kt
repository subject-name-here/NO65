package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldDependentProjectile
import com.unicorns.invisible.no65.util.Coordinates


class ConflictKunai(
    position: Coordinates
) : BattleFieldDependentProjectile(
    position, Direction.RIGHT
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 2
    override fun getString(): String = "Ӵ"
    override fun getStringColor(): Int = R.color.almost_black
}