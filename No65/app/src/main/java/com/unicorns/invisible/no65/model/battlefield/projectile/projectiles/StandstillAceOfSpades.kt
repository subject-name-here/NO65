package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldDependentProjectile
import com.unicorns.invisible.no65.util.Coordinates


class StandstillAceOfSpades(
    position: Coordinates
) : BattleFieldDependentProjectile(
    position, Direction.DOWN
) {
    override fun getString(): String = "Ѕ"
    override fun getStringColor(): Int = R.color.almost_black
}