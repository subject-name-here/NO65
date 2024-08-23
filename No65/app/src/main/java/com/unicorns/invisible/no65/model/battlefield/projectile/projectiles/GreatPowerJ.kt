package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates


class GreatPowerJ(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override val priority: Int = if (damage > 0) 2 else 0
    override fun getString(): String = "j"
    override fun getStringColor(): Int = when (damage) {
        0 -> R.color.true_green
        1, 2 -> R.color.true_yellow
        3, 4 -> R.color.orange
        5, 6 -> R.color.yellow
        7, 8 -> R.color.red
        else -> R.color.dark_red
    }

    fun setTTL(battleField: BattleField) {
        damage = battleField.height - 3
    }

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        if (damage-- == 0) {
            battleField.removeProjectile(this)
        }
    }
}