package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BrotherhoodMotto
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.LetterProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Star52
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldFellowship : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 13
    override val hexagramSymbol: String = "䷌"
    override val nameId: Int
        get() = R.string.battlefield_fellowship_name

    override val centerSymbolColorId: Int = R.color.pink
    override val centerSymbol: String = "⚝"

    override val outerSkinTrigram = Heaven
    override val innerHeartTrigram = Fire

    override val defaultFace: String = "\uD83D\uDE20"
    override val damageReceivedFace: String = "\uD83D\uDE24"
    override val noDamageReceivedFace: String = "\uD83D\uDE20"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_fellowship_1
                2 -> R.string.battlefield_fellowship_2
                3 -> R.string.battlefield_fellowship_3
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_fellowship_d
        override fun getVictoryLine(): Int = R.string.battlefield_fellowship_v
    }

    private var previousProtagonistPosition = Coordinates(-1, -1)
    private var currentRow = -1
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            currentRow = -1
        }
        when (battleField.moveNumber) {
            1, 2, 5, 7 -> {
                val protagonistCoordinates = battleField.protagonist.position
                battleField.addProjectile(
                    Star52(
                        Coordinates(
                            -1,
                            protagonistCoordinates.col
                        ),
                        BattleFieldProjectile.Direction.DOWN
                    )
                )
                battleField.addProjectile(
                    Star52(
                        Coordinates(
                            battleField.height,
                            protagonistCoordinates.col
                        ),
                        BattleFieldProjectile.Direction.UP
                    )
                )
                battleField.addProjectile(
                    Star52(
                        Coordinates(
                            protagonistCoordinates.row,
                            battleField.width
                        ),
                        BattleFieldProjectile.Direction.LEFT
                    )
                )
                battleField.addProjectile(
                    Star52(
                        Coordinates(
                            protagonistCoordinates.row,
                            -1
                        ),
                        BattleFieldProjectile.Direction.RIGHT
                    )
                )
            }
            3 -> {
                if (previousProtagonistPosition != battleField.protagonist.position) {
                    if (currentRow < battleField.height - 4) {
                        generateTogetherLine(battleField, currentRow++)
                    }
                }
                generateRandomFlyingLetter(battleField)
                previousProtagonistPosition = battleField.protagonist.position
            }
            else -> {
                if (previousProtagonistPosition != battleField.protagonist.position) {
                    if (currentRow < battleField.height - 3) {
                        generateTogetherLine(battleField, currentRow++)
                    }
                }
                repeat(2) {
                    generateRandomFlyingLetter(battleField)
                }
                previousProtagonistPosition = battleField.protagonist.position
            }
        }
    }

    private fun generateTogetherLine(battleField: BattleField65, row: Int) {
        repeat(battleField.width) {
            battleField.addProjectile(
                BrotherhoodMotto(
                    Coordinates(row, it)
                )
            )
        }
    }

    private val letters = "TOGETHER".chunked(1)
    private val colors = arrayListOf(R.color.red, R.color.blue, R.color.true_green, R.color.true_yellow)
    private fun generateRandomFlyingLetter(battleField: BattleField65) {
        val letter = letters.random()
        val col = randInt(battleField.width)
        val color = colors.random()
        battleField.addProjectile(
            LetterProjectile(
                Coordinates(battleField.height, col),
                BattleFieldProjectile.Direction.UP,
                letter,
                color
            )
        )
    }
}