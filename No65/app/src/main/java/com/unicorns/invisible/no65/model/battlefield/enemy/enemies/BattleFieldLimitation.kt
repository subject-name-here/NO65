package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Derivative
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Sqrt
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldLimitation : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_limitation_name
    override val number: Int
        get() = 60
    override val centerSymbol: String
        get() = "∰"
    override val centerSymbolColorId: Int
        get() = R.color.black

    override var attackTimeMvs: Int = super.attackTimeMvs

    override val goNumbersToDelays = listOf(
        3 to tickTime / 2,
        2 to tickTime / 2,
        1 to tickTime / 2,
        0 to tickTime / 2,
    )

    override val hexagramSymbol: String
        get() = "䷻"
    override val outerSkinTrigram: Trigram
        get() = Water
    override val innerHeartTrigram: Trigram
        get() = Lake

    override val defaultFace: String
        get() = "\uD83E\uDDD0"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE2B"
    override val noDamageReceivedFace: String
        get() = "\uD83E\uDDD0"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_limitation_1
                2 -> R.string.battlefield_limitation_2
                3 -> R.string.battlefield_limitation_3
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_limitation_d
        override fun getVictoryLine(): Int = R.string.battlefield_limitation_v
    }

    override fun onMoveStart(battleField: BattleField) {
        attackTimeMvs = when (battleField.moveNumber) {
            1 -> super.attackTimeMvs
            2 -> battleField.height * 3
            else -> battleField.height * 5 + 3
        }
    }

    private var freeCol = -1
    private var turnsSinceFreeCol = 0
    private var currentDirection = BattleFieldProjectile.Direction.UP
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            freeCol = -1
            turnsSinceFreeCol = 0
        }

        when (battleField.moveNumber) {
            1 -> {
                standardMove(battleField, tickNumber)
            }
            else -> {
                val areSqrts = battleField.moveNumber > 2
                when (tickNumber % battleField.height) {
                    0 -> {
                        changeProjectilesDirection(battleField)
                    }
                    else -> {
                        standardMove(battleField, tickNumber, areSqrts = areSqrts)
                    }
                }
            }
        }
    }

    private fun standardMove(battleField: BattleField65, tickNumber: Int, areSqrts: Boolean = false) {
        when (tickNumber % 3) {
            0 -> {
                launchSqrts(battleField)
                defineFreeCol(battleField)
            }
            else -> {
                launchMiddleProjectiles(battleField, areSqrts)
            }
        }
    }

    private fun defineFreeCol(battleField: BattleField65) {
        freeCol = randInt(-1, battleField.width)
        if (freeCol == -1) {
            turnsSinceFreeCol++
        }
        if (turnsSinceFreeCol >= battleField.height / 3 - 1) {
            freeCol = randInt(battleField.width)
        }
    }

    private fun changeProjectilesDirection(battleField: BattleField65) {
        val newDirection = if (currentDirection == BattleFieldProjectile.Direction.UP) {
            BattleFieldProjectile.Direction.DOWN
        } else {
            BattleFieldProjectile.Direction.UP
        }

        battleField.getAllObjects()
            .filterIsInstance<BattleFieldProjectile>()
            .forEach {
                it.changeDirection(newDirection)
            }

        currentDirection = newDirection
    }

    private fun getRowFromDirection(battleField: BattleField65): Int {
        return if (currentDirection == BattleFieldProjectile.Direction.UP) {
            battleField.height
        } else {
            -1
        }
    }

    private fun launchSqrts(battleField: BattleField65) {
        repeat(battleField.width) {
            battleField.addProjectile(Sqrt(Coordinates(getRowFromDirection(battleField), it), currentDirection))
        }
    }
    private fun launchMiddleProjectiles(battleField: BattleField65, areSqrts: Boolean = false) {
        if (areSqrts) {
            repeat(battleField.width) {
                if (it != freeCol) {
                    battleField.addProjectile(Sqrt(Coordinates(getRowFromDirection(battleField), it), currentDirection))
                }
            }
        } else {
            repeat(battleField.width) {
                if (it != freeCol) {
                    battleField.addProjectile(Derivative(Coordinates(getRowFromDirection(battleField), it), currentDirection))
                }
            }
        }
    }
}