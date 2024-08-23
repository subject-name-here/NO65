package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class ThreeWaySharp(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    override var damage: Int = 3
    override val priority: Int
        get() = if (direction == Direction.DOWN) 1 else 2
    override fun getString(): String = "Ң"
    override fun getStringColor(): Int = R.color.light_grey

    override fun onTick(tickNumber: Int, battleField: BattleField) {}
}