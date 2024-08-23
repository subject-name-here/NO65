package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class BalloonBroken(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.UP
) {
    override val priority: Int
        get() = 2
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE
    override fun getString(): String = "ѝ"
    override fun getStringColor(): Int = R.color.almost_black
}