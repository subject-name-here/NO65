package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class UpcomingFireYellowOverdrive(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 4
    override var priority: Int = 2
    override fun getString(): String = "ӓ"
    override fun getStringColor(): Int = R.color.true_yellow
}