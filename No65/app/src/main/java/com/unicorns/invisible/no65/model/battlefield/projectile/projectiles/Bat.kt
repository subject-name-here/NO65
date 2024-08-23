package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates


class Bat(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    override fun getString(): String = "ї"
    override fun getStringColor(): Int = R.color.black

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)
        when (position.col) {
            0 -> {
                direction = Direction.RIGHT
            }
            battleField.width - 1 -> {
                direction = Direction.LEFT
            }
        }
        position.row++
        if (battleField.getMap()[position] is WallStreet) {
            when (direction) {
                Direction.LEFT -> position.col--
                Direction.RIGHT -> position.col++
                else -> {}
            }
        }
    }
}