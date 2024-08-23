package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class Censorship(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    override val priority: Int = 2
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 2
    override fun getString(): String = "Ѣ"
    override fun getStringColor(): Int = R.color.black
}