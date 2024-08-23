package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.RevolutionBomb
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.RevolutionExplodingBomb
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt
import com.unicorns.invisible.no65.util.takeRand


class BattleFieldRevolution : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_revolution_name
    override val number: Int
        get() = 49

    override val attackTimeMvs: Int
        get() = 40

    override val defaultFace: String
        get() = "\uD83D\uDE20"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE20"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDE20"

    override val centerSymbol: String
        get() = "\uD83D\uDDE1"
    override val centerSymbolColorId: Int
        get() = R.color.dark_red

    override val hexagramSymbol: String
        get() = "䷰"
    override val outerSkinTrigram: Trigram
        get() = Lake
    override val innerHeartTrigram: Trigram
        get() = Fire

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_revolution_1
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_revolution_d
        override fun getVictoryLine(): Int = R.string.battlefield_revolution_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber) {
            1 -> {
                when (tickNumber % 3) {
                    0 -> {
                        repeat(battleField.height) { row ->
                            spawnBomb(battleField, row)
                        }
                    }
                    2 -> {
                        val freeRow = randInt(battleField.height)
                        repeat(battleField.height) { row ->
                            if (row != freeRow) {
                                spawnBomb(battleField, row)
                            }
                        }
                    }
                }
            }
            2 -> {
                if (tickNumber == 0) {
                    spawnExplodingBombs(battleField)
                }
                val bombs = battleField.getAllObjects().filterIsInstance<RevolutionExplodingBomb>()
                if (bombs.isNotEmpty()) {
                    bombs.random().setToExplode()
                }
            }
            3 -> {
                if (tickNumber == 0) {
                    spawnExplodingBombs(battleField)
                }
                threeBombsAttack(battleField)
            }
            else -> {
                if (tickNumber == 0) {
                    spawnExplodingBombs(battleField)
                }
                threeBombsAttack(battleField)

                // -1 added to give a chance of bomb not spawning
                if (tickNumber % 3 == 0) {
                    val col = (-1..battleField.width / 2).map { it * 2 }.random()
                    battleField.addProjectile(RevolutionBomb(
                        Coordinates(battleField.height, col),
                        BattleFieldProjectile.Direction.UP
                    ))
                }
            }
        }
    }

    private fun threeBombsAttack(battleField: BattleField65) {
        val maxBombs = (battleField.height / 3) * (battleField.width / 2) - 1
        val bombs = battleField.getAllObjects()
            .filterIsInstance<RevolutionExplodingBomb>()
            .filter { it.isCalm() }
        bombs.takeRand(maxBombs / 2).forEach {
            it.setToExplode()
        }
    }

    private fun spawnBomb(battleField: BattleField65, row: Int) {
        battleField.addProjectile(RevolutionBomb(Coordinates(row, battleField.width), BattleFieldProjectile.Direction.LEFT))
    }
    private fun spawnExplodingBombs(battleField: BattleField65) {
        val cols = (0 until battleField.width / 2).map { it * 2 + 1 }
        cols.forEach { col ->
            repeat(battleField.height) { row ->
                battleField.addProjectile(RevolutionExplodingBomb(Coordinates(row, col)))
            }
        }
    }
}