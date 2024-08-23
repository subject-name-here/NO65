package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Bullet(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.LEFT
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 4
    override val priority: Int = 20
    override fun getString(): String = "җ"
    override fun getStringColor(): Int = R.color.yellow
}