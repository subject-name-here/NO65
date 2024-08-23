package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Plateau
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.WallStreet
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean


class BattleFieldReceptiveEarth : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * TOUGH_BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 30
    override val defenceTimeSec: Int = 4

    override val number: Int = 2
    override val nameId: Int
        get() = R.string.battlefield_receptive_earth_name

    override val centerSymbolColorId: Int = R.color.brown
    override val centerSymbol: String = "\uD83C\uDF10"

    override val hexagramSymbol: String = "䷁"
    override val outerSkinTrigram = Earth
    override val innerHeartTrigram = Earth

    override var defaultFace: String = "\uD83D\uDE24"
    override val damageReceivedFace: String = "\uD83D\uDE24"
    override val noDamageReceivedFace: String = "\uD83D\uDE00"

    override val numberOfTaps: Int = 3
    override val musicThemeId: Int = R.raw.battle_receptive_earth
    override val animation: CharacterAnimation
        get() = CharacterAnimation.ROTATE_FULL

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_receptive_earth_1
                2 -> R.string.battlefield_receptive_earth_2
                3 -> R.string.battlefield_receptive_earth_3
                4 -> R.string.battlefield_receptive_earth_4
                5 -> R.string.battlefield_receptive_earth_5
                6 -> R.string.battlefield_receptive_earth_6
                7 -> R.string.battlefield_receptive_earth_7
                8 -> R.string.battlefield_receptive_earth_8
                9 -> R.string.battlefield_receptive_earth_9
                10 -> R.string.battlefield_receptive_earth_10
                11 -> R.string.battlefield_receptive_earth_11
                12 -> R.string.battlefield_receptive_earth_12
                13 -> R.string.battlefield_receptive_earth_13
                14 -> R.string.battlefield_receptive_earth_14
                15 -> R.string.battlefield_receptive_earth_15
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int {
            defaultFace = "\uD83D\uDE22"
            return R.string.battlefield_receptive_earth_d
        }
        override fun getVictoryLine(): Int = R.string.battlefield_receptive_earth_v
    }

    private var shifts = mutableListOf<Int>()
    private var isReversed = false
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        val moveNumber = battleField.moveNumber
        if (tickNumber == 0) {
            generateWalls(battleField, moveNumber == 1)
            shifts = (1 until battleField.width - 1).toMutableList()
            shifts.shuffle()
        }

        if (moveNumber == 1) {
            if (tickNumber % 5 == 0) {
                battleField.addProjectile(
                    Plateau(
                        Coordinates(
                            battleField.height,
                            battleField.width / 2
                        ),
                        BattleFieldProjectile.Direction.UP
                    )
                )
            }
            return
        }

        val mod = when (moveNumber) {
            2 -> 3
            3 -> 2
            else -> 1
        }
        if (tickNumber % mod == 0) {
            val shiftNumber = (tickNumber / mod) % (battleField.width - 2)
            if (shiftNumber == 0) {
                shifts = (1 until battleField.width - 1).toMutableList()

                if (moveNumber < 5 || moveNumber % 2 == 0) {
                    shifts.shuffle()
                } else {
                    if (tickNumber == 0) {
                        isReversed = randBoolean()
                    }
                    if (isReversed) {
                        shifts.reverse()
                    }
                }
            }
            battleField.addProjectile(
                Plateau(
                    Coordinates(
                        battleField.height,
                        shifts[shiftNumber]
                    ),
                    BattleFieldProjectile.Direction.UP
                )
            )
        }
    }
    private fun generateWalls(battleField: BattleField65, isSimple: Boolean) {
        if (isSimple) {
            repeat(battleField.width) { c ->
                if (c != battleField.width / 2) {
                    generateVerticalWall(battleField, c)
                }
            }
        } else {
            generateVerticalWall(battleField, 0)
            generateVerticalWall(battleField, battleField.width - 1)
        }
    }
    private fun generateVerticalWall(battleField: BattleField65, c: Int) {
        repeat(battleField.height) {
            battleField.addProjectile(WallStreet(Coordinates(it, c)))
        }
    }
}