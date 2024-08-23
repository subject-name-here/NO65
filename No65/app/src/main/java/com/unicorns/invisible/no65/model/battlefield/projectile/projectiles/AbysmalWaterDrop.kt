package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class AbysmalWaterDrop(
    position: Coordinates,
    direction: Direction = Direction.NO_MOVEMENT,
) : BattleFieldProjectile(
    position, direction
) {
    private var stringColor: Int = COLORS.random()
    override var damage: Int = 4
    override fun getString(): String = "Ә"
    override fun getStringColor(): Int = stringColor

    fun changeColor(newColor: Int) {
        stringColor = newColor
    }

    companion object {
        val COLORS = listOf(R.color.blue, R.color.light_blue, R.color.lighter_blue, R.color.dark_blue)
    }
}