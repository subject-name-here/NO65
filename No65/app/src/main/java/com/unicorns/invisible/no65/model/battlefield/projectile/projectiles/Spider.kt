package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Spider(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.DOWN
) {
    override var damage: Int = 8
    override fun getString(): String = "Ї"
    override fun getStringColor(): Int = R.color.black
}