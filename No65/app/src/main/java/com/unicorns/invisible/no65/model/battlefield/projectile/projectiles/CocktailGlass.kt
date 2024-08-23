package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class CocktailGlass(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 2
    override fun getString(): String = "Џ"
    override fun getStringColor(): Int = R.color.dark_red
}