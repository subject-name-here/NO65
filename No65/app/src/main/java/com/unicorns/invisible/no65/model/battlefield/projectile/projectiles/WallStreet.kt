package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class WallStreet(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override val priority: Int = 22229
    override var damage: Int = BattleFieldProtagonist.BASIC_HEALTH / 2 + 1
    override fun getString(): String = "ӳ"
    override fun getStringColor(): Int = R.color.brick_color
}