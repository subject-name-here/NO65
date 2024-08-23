package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.GrowingPlant
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.choose


class BattleFieldReturn : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_return_name
    override val number: Int = 24

    override val attackTimeMvs: Int
        get() = 32

    override val defaultFace: String
        get() = "\uD83E\uDD89"
    override val damageReceivedFace: String
        get() = "\uD83E\uDD89"
    override val noDamageReceivedFace: String
        get() = "\uD83E\uDD89"

    override val centerSymbol: String
        get() = "\uD83C\uDF33"
    override val centerSymbolColorId: Int
        get() = R.color.green

    override val hexagramSymbol: String
        get() = "䷗"
    override val outerSkinTrigram: Trigram
        get() = Earth
    override val innerHeartTrigram: Trigram
        get() = Thunder

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_return_1
                2 -> R.string.battlefield_return_2
                3 -> R.string.battlefield_return_3
                4 -> R.string.battlefield_return_4
                else -> R.string.battlefield_return_0
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_return_d
        override fun getVictoryLine(): Int = R.string.battlefield_return_v
    }

    private var upperLeftCorner = Coordinates.ZERO
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            upperLeftCorner = Coordinates(battleField.height / 2, battleField.width / 2)
            fillTheField(battleField)
            return
        }
        if (tickNumber % 2 == 1) {
            battleField.getAllObjects()
                .filterIsInstance<GrowingPlant>()
                .forEach { it.changeDirection(BattleFieldProjectile.Direction.NO_MOVEMENT) }
            return
        }

        val direction = when (battleField.moveNumber) {
            1 -> {
                val rowDiff = battleField.protagonist.position.row - upperLeftCorner.row
                val colDiff = battleField.protagonist.position.col - upperLeftCorner.col
                val yAxisDirection = if (rowDiff == 0) {
                    BattleFieldProjectile.Direction.DOWN
                } else {
                    BattleFieldProjectile.Direction.UP
                }
                val xAxisDirection = if (colDiff == 0) {
                    BattleFieldProjectile.Direction.RIGHT
                } else {
                    BattleFieldProjectile.Direction.LEFT
                }
                choose(xAxisDirection, yAxisDirection)
            }
            2 -> {
                choose(
                    BattleFieldProjectile.Direction.RIGHT,
                    BattleFieldProjectile.Direction.LEFT,
                    BattleFieldProjectile.Direction.UP,
                    BattleFieldProjectile.Direction.DOWN,
                )
            }
            3 -> {
                choose(BattleFieldProjectile.Direction.RIGHT, BattleFieldProjectile.Direction.LEFT)
            }
            else -> {
                choose(BattleFieldProjectile.Direction.RIGHT, BattleFieldProjectile.Direction.NO_MOVEMENT)
            }
        }

        when (direction) {
            BattleFieldProjectile.Direction.UP, BattleFieldProjectile.Direction.DOWN -> {
                battleField.getAllObjects()
                    .filterIsInstance<GrowingPlant>()
                    .filter {
                        val diff = it.position.col - upperLeftCorner.col
                        diff in 0..1 || diff == -(battleField.width - 1)
                    }
                    .forEach { it.changeDirection(direction) }
            }
            BattleFieldProjectile.Direction.LEFT, BattleFieldProjectile.Direction.RIGHT -> {
                battleField.getAllObjects()
                    .filterIsInstance<GrowingPlant>()
                    .filter {
                        val diff = it.position.row - upperLeftCorner.row
                        diff in 0..1 || diff == -(battleField.height - 1)
                    }
                    .forEach { it.changeDirection(direction) }
            }
            BattleFieldProjectile.Direction.NO_MOVEMENT -> {}
        }

        upperLeftCorner += direction.getDelta()
        when {
            upperLeftCorner.row == -1 -> upperLeftCorner.row = battleField.height - 1
            upperLeftCorner.row == battleField.height -> upperLeftCorner.row = 0
            upperLeftCorner.col == -1 -> upperLeftCorner.col = battleField.width - 1
            upperLeftCorner.col == battleField.width -> upperLeftCorner.col = 0
        }
    }

    private fun fillTheField(battleField: BattleField65) {
        repeat(battleField.width) { col ->
            repeat(battleField.height) { row ->
                val rowDiff = row - upperLeftCorner.row
                val colDiff = col - upperLeftCorner.col
                if (!(rowDiff in 0..1 && colDiff in 0..1)) {
                    battleField.addProjectile(GrowingPlant(Coordinates(row, col)))
                }
            }
        }
    }
}