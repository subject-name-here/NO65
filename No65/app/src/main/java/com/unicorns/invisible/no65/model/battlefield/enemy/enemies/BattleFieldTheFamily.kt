package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.FamilyBlackHeart
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.FamilyHeart
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.WallStreet
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt
import com.unicorns.invisible.no65.util.takeRand


class BattleFieldTheFamily(val whatAShameLineSaid: Boolean) : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_BAD_HEALTH_MULTIPLIER * 2
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 26

    override val number: Int = 37
    override val nameId: Int
        get() = R.string.battlefield_the_family_name

    override val centerSymbolColorId: Int = R.color.true_green
    override val centerSymbol: String = "\uD83D\uDC9F"

    override val hexagramSymbol: String = "䷤"
    override val outerSkinTrigram = Wind
    override val innerHeartTrigram = Fire

    override var defaultFace: String = "\uD83D\uDE31\uD83D\uDE31"
    override val damageReceivedFace: String = "\uD83D\uDE31\uD83D\uDE31"
    override val noDamageReceivedFace: String = "\uD83D\uDE31\uD83D\uDE31"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = if (whatAShameLineSaid) {
            when (moveNumber) {
                1 -> R.string.battlefield_the_family_met_1
                2 -> R.string.battlefield_the_family_met_2
                3 -> R.string.battlefield_the_family_met_3
                4 -> R.string.battlefield_the_family_met_4
                else -> R.string.empty_line
            }
        } else {
            when (moveNumber) {
                1 -> R.string.battlefield_the_family_1
                2 -> R.string.battlefield_the_family_2
                3 -> R.string.battlefield_the_family_3
                else -> R.string.empty_line
            }
        }

        override fun getDefeatedLine(): Int = R.string.battlefield_the_family_d
        override fun getVictoryLine(): Int = R.string.battlefield_the_family_v
    }


    private val coords = arrayListOf<Coordinates>()
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            spawnWallsEnds(battleField)
        }

        if (tickNumber == 3) {
            spawnWalls(battleField)
        }

        when (tickNumber % 3) {
            0 -> {
                coords.clear()
                val numOfQuarters = when (battleField.moveNumber) {
                    1 -> 1
                    2 -> randInt(1, 3)
                    3 -> randInt(2, 4)
                    else -> 3
                }
                spawnRandomQuartersHearts(numOfQuarters, battleField)
            }
            1 -> {
                spawnBlackHearts(battleField)
            }
            2 -> {
                battleField.getAllObjects().filterIsInstance<FamilyBlackHeart>().forEach {
                    battleField.removeProjectile(it)
                }
            }
        }
    }

    private fun spawnRandomQuartersHearts(numOfQuarters: Int, battleField: BattleField65) {
        val quarters = (0..3).takeRand(numOfQuarters)
        for (q in quarters) {
            val leftUpperCol = if (q % 2 == 0) 0 else battleField.width / 2 + 1
            val leftUpperRow = if (q / 2 == 0) 0 else battleField.height / 2 + 1

            repeat(battleField.height / 2) { deltaRow ->
                repeat(battleField.width / 2) { deltaCol ->
                    val coordinates = Coordinates(leftUpperRow + deltaRow, leftUpperCol + deltaCol)
                    coords.add(coordinates)
                    battleField.addProjectile(FamilyHeart(coordinates))
                }
            }
        }
    }

    private fun spawnBlackHearts(battleField: BattleField65) {
        for (c in coords) {
            battleField.addProjectile(FamilyBlackHeart(c))
        }
    }


    private fun spawnWallsEnds(battleField: BattleField65) {
        val midVertical = battleField.width / 2
        val midHorizontal = battleField.height / 2

        battleField.addProjectile(WallStreet(Coordinates(0, midVertical)))
        battleField.addProjectile(WallStreet(Coordinates(battleField.height - 1, midVertical)))
        battleField.addProjectile(WallStreet(Coordinates(midHorizontal, 0)))
        battleField.addProjectile(WallStreet(Coordinates(midHorizontal, battleField.width - 1)))
    }


    private fun spawnWalls(battleField: BattleField65) {
        val midVertical = battleField.width / 2
        val midHorizontal = battleField.height / 2

        repeat(battleField.height) {
            battleField.addProjectile(WallStreet(Coordinates(it, midVertical)))
        }
        repeat(battleField.width) {
            battleField.addProjectile(WallStreet(Coordinates(midHorizontal, it)))
        }
    }
}