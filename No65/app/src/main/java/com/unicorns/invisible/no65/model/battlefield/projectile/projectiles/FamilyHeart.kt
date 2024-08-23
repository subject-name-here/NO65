package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class FamilyHeart(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override val priority: Int = 1
    override fun getString(): String = "Ҹ"
    override fun getStringColor(): Int = R.color.true_green
    override var damage: Int = 0
}