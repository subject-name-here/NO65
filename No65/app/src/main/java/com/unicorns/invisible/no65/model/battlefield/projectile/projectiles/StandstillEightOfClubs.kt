package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldDependentProjectile
import com.unicorns.invisible.no65.util.Coordinates


class StandstillEightOfClubs(
    position: Coordinates
) : BattleFieldDependentProjectile(
    position, Direction.UP
) {
    override var damage: Int = 8
    override val priority: Int = 4
    override fun getString(): String = "І"
    override fun getStringColor(): Int = R.color.green
}