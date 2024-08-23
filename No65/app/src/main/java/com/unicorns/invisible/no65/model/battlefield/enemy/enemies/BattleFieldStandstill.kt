package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.StandstillAceOfSpades
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.StandstillEightOfClubs
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldStandstill : BattleFieldEnemy() {
    override val number: Int = 12
    override val hexagramSymbol: String = "䷋"
    override val nameId: Int = R.string.battlefield_standstill_name

    override var attackTimeMvs: Int = super.attackTimeMvs

    override val centerSymbolColorId: Int = R.color.red
    override val centerSymbol: String = "\uD83C\uDF39"

    override val outerSkinTrigram = Heaven
    override val innerHeartTrigram = Earth

    override val defaultFace: String = "\uD83D\uDE11"
    override val damageReceivedFace: String = "\uD83D\uDE23"
    override val noDamageReceivedFace: String = "\uD83D\uDE11"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_standstill_1
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_standstill_d
        override fun getVictoryLine(): Int = R.string.battlefield_standstill_v
    }

    override fun onMoveStart(battleField: BattleField) {
        attackTimeMvs = when (battleField.moveNumber) {
            1, 2 -> super.attackTimeMvs
            3 -> 30
            4 -> 35
            5 -> 45
            else -> 50
        }
    }

    private var shovelRows = mutableSetOf<Int>()
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            shovelRows.clear()
        }

        val projectiles = battleField.getAllObjects().filterIsInstance<BattleFieldProjectile>()
        projectiles.filterIsInstance<StandstillAceOfSpades>().forEach {
            it.position += it.direction.getDelta()
        }
        projectiles.filterIsInstance<StandstillEightOfClubs>().forEach {
            it.position += it.direction.getDelta()
        }
        battleField.setFieldPlayable()

        updateShovelRows(battleField)

        when (battleField.moveNumber) {
            1 -> {
                if (tickNumber % 4 == 0) {
                    shovelRows.add(0)
                }
                generateShovels(battleField)

                val col = battleField.protagonist.position.col
                battleField.addProjectile(
                    StandstillEightOfClubs(Coordinates(battleField.height - 1, col))
                )
            }
            2 -> {
                if (tickNumber % 4 == 0) {
                    shovelRows.add(0)
                }
                generateShovels(battleField)

                generateClubsAlmostFullRow(battleField)
            }
            3 -> {
                if (tickNumber % 3 == 0) {
                    shovelRows.add(0)
                }
                generateShovels(battleField)

                generateClubsAlmostFullRow(battleField)
            }
            4, 5 -> {
                if (tickNumber % 3 == 0) {
                    shovelRows.add(0)
                }
                generateShovels(battleField)

                if (tickNumber % 14 == 7) {
                    generateClubsFullRow(battleField)
                } else {
                    generateClubsAlmostFullRow(battleField)
                }
            }
            else -> {
                if (tickNumber % 3 == 0) {
                    shovelRows.add(0)
                }
                generateShovels(battleField)

                when (tickNumber % 28) {
                    7, 13, 20, 27 -> {
                        generateClubsFullRow(battleField)
                    }
                    else -> {
                        generateClubsAlmostFullRow(battleField)
                    }
                }
            }
        }
    }

    private fun generateShovels(battleField: BattleField65) {
        shovelRows.forEach { row ->
            repeat(battleField.width) {
                battleField.addProjectile(StandstillAceOfSpades(Coordinates(row, it)))
            }
        }
    }
    private fun updateShovelRows(battleField: BattleField65) {
        shovelRows = shovelRows
            .map { it + 1 }
            .filter { it in (0 until battleField.height) }
            .toMutableSet()
    }

    private fun generateClubsAlmostFullRow(battleField: BattleField65) {
        val col = battleField.protagonist.position.col
        repeat(battleField.width - 2) {
            battleField.addProjectile(
                StandstillEightOfClubs(
                    Coordinates(
                        battleField.height - 1,
                        (col + it - battleField.width / 2 + 1) % battleField.width
                    )
                )
            )
        }
    }
    private fun generateClubsFullRow(battleField: BattleField65) {
        repeat(battleField.width) {
            battleField.addProjectile(StandstillEightOfClubs(Coordinates(battleField.height - 1, it)))
        }
    }
}