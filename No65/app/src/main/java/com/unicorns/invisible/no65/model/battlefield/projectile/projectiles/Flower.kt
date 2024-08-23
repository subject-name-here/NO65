package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt

class Flower(
    position: Coordinates,
    direction: Direction
) : BattleFieldProjectile(
    position, direction
) {
    var loaded = false
    var charged = false

    override var damage: Int
        get() = if (loaded) BattleFieldProtagonist.BASIC_HEALTH / 2 else 2
        set(_) {}
    override fun getString(): String = "Ѿ"
    override fun getStringColor(): Int = R.color.true_yellow

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)

        if (charged && direction != Direction.NO_MOVEMENT) {
            direction = Direction.NO_MOVEMENT
            return
        }

        if (loaded && charged && tickNumber % 2 == 0) {
            unload(battleField)
        }

        if (!loaded) {
            when (direction) {
                Direction.UP -> {
                    battleField.addProjectile(Flower(position + Coordinates(0, -1), Direction.UP))
                    battleField.addProjectile(Flower(position + Coordinates(0, 1), Direction.UP))
                }
                Direction.DOWN -> {
                    battleField.addProjectile(Flower(position + Coordinates(0, -1), Direction.DOWN))
                    battleField.addProjectile(Flower(position + Coordinates(0, 1), Direction.DOWN))
                }
                Direction.LEFT -> {
                    battleField.addProjectile(Flower(position + Coordinates(1, 0), Direction.LEFT))
                    battleField.addProjectile(Flower(position + Coordinates(-1, 0), Direction.LEFT))
                }
                Direction.RIGHT -> {
                    battleField.addProjectile(Flower(position + Coordinates(-1, 0), Direction.RIGHT))
                    battleField.addProjectile(Flower(position + Coordinates(1, 0), Direction.RIGHT))
                }
                Direction.NO_MOVEMENT -> {}
            }
        }
    }

    private fun unload(battleField: BattleField) {
        val missingDirection = randInt(4)
        if (missingDirection != 0) {
            battleField.addProjectile(Flower(position + Coordinates(-1, 0), Direction.UP))
            battleField.addProjectile(Flower(position + Coordinates(-1 ,1), Direction.UP))
        }
        if (missingDirection != 1) {
            battleField.addProjectile(Flower(position + Coordinates(1, 0), Direction.DOWN))
            battleField.addProjectile(Flower(position + Coordinates(1, -1), Direction.DOWN))
        }
        if (missingDirection != 2) {
            battleField.addProjectile(Flower(position + Coordinates(-1, -1), Direction.LEFT))
            battleField.addProjectile(Flower(position + Coordinates(0, -1), Direction.LEFT))
        }
        if (missingDirection != 3) {
            battleField.addProjectile(Flower(position + Coordinates(0, 1), Direction.RIGHT))
            battleField.addProjectile(Flower(position + Coordinates(1, 1), Direction.RIGHT))
        }
    }
}