package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist.Companion.HEAL_POINTS
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Avalanche(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    override var damage: Int = HEAL_POINTS
    override fun getString(): String = "\\"
    override fun getStringColor(): Int = R.color.light_grey
}