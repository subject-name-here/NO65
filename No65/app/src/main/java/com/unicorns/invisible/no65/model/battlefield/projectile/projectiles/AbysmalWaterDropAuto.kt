package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.fighter.equal.BattleFieldEqualAbysmalWater
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.choose


class AbysmalWaterDropAuto(
    position: Coordinates,
    direction: Direction,
) : BattleFieldProjectile(
    position, direction
) {
    override var damage: Int = 4
    override fun getString(): String = "Ә"
    private val stringColor = when (direction) {
        Direction.DOWN -> R.color.dark_red
        else -> AbysmalWaterDrop.COLORS.random()
    }
    override fun getStringColor(): Int = stringColor

    private var wasInitialized = false
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        // we don't call super method because we don't want drops to move.
        if (!wasInitialized) {
            wasInitialized = true
        } else {
            battleField.removeProjectile(this)
            if (direction == Direction.NO_MOVEMENT) return

            directions.clear()
            val maxDist = battleField.height
            (0 until maxDist).forEach { dist ->
                val projectileCoordinates = position + direction.getDelta() * dist
                if (
                    projectileCoordinates.row !in (0 until battleField.height) ||
                    projectileCoordinates.col !in (0 until battleField.width)
                ) {
                    return@forEach
                }

                val projectileDirection = when (direction) {
                    Direction.DOWN -> generateDirectionFromDown()
                    Direction.RIGHT, Direction.LEFT -> Direction.NO_MOVEMENT
                    else -> return
                }
                battleField.addProjectile(AbysmalWaterDropAuto(projectileCoordinates, projectileDirection))
            }
        }
    }
    private val windowSize
        get() = choose(2, 3)
    private val directions = mutableListOf<Direction>()
    private fun generateDirectionFromDown(): Direction {
        val window = directions.takeLast(windowSize)
        return if (window.distinct().size != 1) {
            BattleFieldEqualAbysmalWater.SIDES_LIST.random()
        } else {
            val remainingDirection = BattleFieldEqualAbysmalWater.SIDES_LIST - window.toSet()
            remainingDirection.firstOrNull() ?: Direction.NO_MOVEMENT
        }
    }

    fun changeDirectionInstantly(newDirection: Direction) {
        direction = newDirection
    }
}