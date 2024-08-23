package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class PaperPlane(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.LEFT
) {
    override fun getString(): String = "Ѡ"
    override fun getStringColor(): Int = R.color.light_grey
}