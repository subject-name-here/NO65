package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates

class LetterReverseProjectile(
    position: Coordinates,
    direction: Direction,
    val symbol: String,
    val color: Int = R.color.black
) : BattleFieldProjectile(
    position, direction
) {
    override fun getString(): String = symbol
    override fun getStringColor(): Int = color

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        position -= direction.getDelta()
    }
}