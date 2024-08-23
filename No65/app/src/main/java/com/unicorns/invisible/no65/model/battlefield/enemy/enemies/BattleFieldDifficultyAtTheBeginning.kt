package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.SteelRain
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.takeRand


class BattleFieldDifficultyAtTheBeginning : BattleFieldEnemy() {
    override val attackTimeMvs: Int
        get() = 40

    override val number: Int = 3
    override val hexagramSymbol: String = "䷂"
    override val nameId: Int
        get() = R.string.battlefield_difficulty_at_the_beginning_name

    override val centerSymbolColorId: Int = R.color.red
    override val centerSymbol: String = "⛔"

    override val outerSkinTrigram = Water
    override val innerHeartTrigram = Thunder

    override val defaultFace: String = "\uD83D\uDE12"
    override val damageReceivedFace: String = "\uD83D\uDE2C"
    override val noDamageReceivedFace: String = "\uD83D\uDE0F"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_difficulty_at_the_beginning_1
                2 -> R.string.battlefield_difficulty_at_the_beginning_2
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_difficulty_at_the_beginning_d
        override fun getVictoryLine(): Int = R.string.battlefield_difficulty_at_the_beginning_v
    }

    private val currentCells = mutableListOf<Int>()
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            currentCells.clear()
        }
        val width = battleField.width
        when (tickNumber % 3) {
            0 -> {
                repeat(width) {
                    battleField.addProjectile(SteelRain(Coordinates(-1, it)))
                }
            }
            1 -> {
                when (battleField.moveNumber) {
                    1 -> {
                        val currentCells = (0 until width).takeRand(3)
                        currentCells.forEach {
                            battleField.addProjectile(SteelRain(Coordinates(-1, it)))
                        }
                    }
                    2 -> {
                        val currentCells = (0 until width).takeRand(2)
                        currentCells.forEach {
                            battleField.addProjectile(SteelRain(Coordinates(-1, it)))
                        }
                    }
                    3 -> {
                        if (currentCells.isEmpty()) {
                            currentCells.addAll(0 until width)
                        }
                        if ((tickNumber / 3) % width == 0) {
                            currentCells.shuffle()
                        }
                        val currentCell = currentCells[(tickNumber / 3) % width]
                        battleField.addProjectile(SteelRain(Coordinates(-1, currentCell)))
                    }
                    else ->  {
                        val currentCell = width / 2
                        battleField.addProjectile(SteelRain(Coordinates(-1, currentCell)))
                    }
                }
            }
        }
    }
}