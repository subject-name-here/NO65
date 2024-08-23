package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates


class GreatPowerJSpawner(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    override val priority: Int = 5
    override var damage: Int = DEFAULT_PROJECTILE_DAMAGE * 10
    override fun getString(): String = "J"
    override fun getStringColor(): Int = R.color.red

    private var loaded = false
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        if (!loaded) {
            super.onTick(tickNumber, battleField)
            loaded = true
        } else {
            battleField.removeProjectile(this)
            when (direction) {
                Direction.UP, Direction.DOWN -> {
                    repeat(battleField.height) { row ->
                        val projectile = GreatPowerJ(Coordinates(row, position.col))
                        projectile.setTTL(battleField)
                        battleField.addProjectile(projectile)
                    }
                }
                Direction.LEFT, Direction.RIGHT -> {
                    repeat(battleField.width) { col ->
                        val projectile = GreatPowerJ(Coordinates(position.row, col))
                        projectile.setTTL(battleField)
                        battleField.addProjectile(projectile)
                    }
                }
                Direction.NO_MOVEMENT -> { return }
            }
        }
    }
}