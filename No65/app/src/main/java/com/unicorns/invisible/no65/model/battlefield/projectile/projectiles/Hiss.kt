package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Hiss(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.DOWN
) {
    override fun getString(): String = "Ћ"
    override fun getStringColor(): Int = R.color.green
}
