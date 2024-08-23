package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class DeliveranceTwodent(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.UP
) {
    override var damage: Int = 2 * DEFAULT_PROJECTILE_DAMAGE
    override val priority: Int = 2
    override fun getString(): String = "ұ"
    override fun getStringColor(): Int = R.color.grey
}