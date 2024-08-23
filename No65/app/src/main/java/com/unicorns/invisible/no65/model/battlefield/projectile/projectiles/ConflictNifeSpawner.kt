package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldDependentProjectile
import com.unicorns.invisible.no65.util.Coordinates


class ConflictNifeSpawner(
    position: Coordinates
) : BattleFieldDependentProjectile(
    position, Direction.DOWN
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 5
    override val priority: Int = 2
    override fun getString(): String = "Ӣ"
    override fun getStringColor(): Int = R.color.grey
}