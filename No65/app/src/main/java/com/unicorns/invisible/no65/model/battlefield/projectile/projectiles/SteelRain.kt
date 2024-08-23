package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates


class SteelRain(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.DOWN
) {
    override var damage: Int = 24
    override val priority: Int = 200
    override fun getString(): String = "Ӹ"
    override fun getStringColor(): Int = R.color.black
}