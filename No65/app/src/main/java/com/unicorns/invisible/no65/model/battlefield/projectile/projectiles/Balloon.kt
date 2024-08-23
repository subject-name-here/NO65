package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Balloon(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.UP
) {
    override val priority: Int
        get() = 1
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE / 2
    override fun getString(): String = "ћ"
    override fun getStringColor(): Int = R.color.red
}