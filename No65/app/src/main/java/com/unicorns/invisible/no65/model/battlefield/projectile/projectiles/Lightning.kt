package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Lightning(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override var damage: Int = DAMAGE
    override fun getString(): String = "Ѱ"
    override fun getStringColor(): Int = R.color.true_yellow

    companion object {
        const val DAMAGE = DEFAULT_PROJECTILE_DAMAGE * 4
    }
}