package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.choose

class Question(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    override var damage: Int = choose(0, 1, 1, 2, 3, 5, 8, 13, 21)
    override fun getString(): String = "?"
    override fun getStringColor(): Int = when (damage) {
        0 -> R.color.true_green
        in (1..4) -> R.color.true_yellow
        in (5..10) -> R.color.yellow
        in (10..20) -> R.color.red
        else -> R.color.dark_red
    }
}