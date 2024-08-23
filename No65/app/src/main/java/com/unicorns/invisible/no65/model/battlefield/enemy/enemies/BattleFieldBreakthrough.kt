package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BreakthroughEye
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BreakthroughMouth
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BreakthroughNose
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import kotlin.math.min


class BattleFieldBreakthrough : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_breakthrough_name
    override val number: Int
        get() = 43

    override val defaultFace: String
        get() = "\uD83D\uDC51"
    override val damageReceivedFace: String
        get() = "\uD83D\uDC51"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDC51"

    override val centerSymbol: String
        get() = "\uD83D\uDC45"
    override val centerSymbolColorId: Int
        get() = R.color.black

    override val hexagramSymbol: String
        get() = "䷪"
    override val outerSkinTrigram: Trigram
        get() = Lake
    override val innerHeartTrigram: Trigram
        get() = Heaven

    override val handsSymbol: String
        get() = "ҹ"
    override val legsSymbol: String
        get() = "ҩ"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_breakthrough_1
                2 -> R.string.battlefield_breakthrough_2
                3 -> R.string.battlefield_breakthrough_3
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_breakthrough_d
        override fun getVictoryLine(): Int = R.string.battlefield_breakthrough_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber) {
            1 -> {
                continueMouthsToLeft(battleField)
                if (tickNumber % 2 == 0) {
                    tAttack(battleField, 3)
                }

            }
            2, 6 -> {
                continueMouthsToDown(battleField)
                attackFromAbove(battleField)
            }
            3, 5 -> {
                continueMouthsToLeft(battleField)
                if (tickNumber % 2 == 0) {
                    tAttack(battleField, 4)
                }
            }
            else -> {
                continueMouthsToLeft(battleField)
                tAttack(battleField, 2)
            }
        }
    }

    private fun continueMouthsToLeft(battleField: BattleField65) {
        battleField.getAllObjects()
            .filterIsInstance<BreakthroughMouth>()
            .filter { it.position.col == battleField.width - 1 }
            .map { it.position.row }
            .forEach { mouthRow ->
                battleField.addProjectile(BreakthroughEye(Coordinates(mouthRow - 1, battleField.width), BattleFieldProjectile.Direction.LEFT))
                battleField.addProjectile(BreakthroughNose(Coordinates(mouthRow, battleField.width), BattleFieldProjectile.Direction.LEFT))
                battleField.addProjectile(BreakthroughEye(Coordinates(mouthRow + 1, battleField.width), BattleFieldProjectile.Direction.LEFT))
            }
    }

    private fun continueMouthsToDown(battleField: BattleField65) {
        battleField.getAllObjects()
            .filterIsInstance<BreakthroughMouth>()
            .filter { it.position.row == 0 }
            .map { it.position.col }
            .forEach { mouthCol ->
                battleField.addProjectile(BreakthroughEye(Coordinates(-1, mouthCol - 1), BattleFieldProjectile.Direction.DOWN))
                battleField.addProjectile(BreakthroughNose(Coordinates(-1, mouthCol), BattleFieldProjectile.Direction.DOWN))
                battleField.addProjectile(BreakthroughEye(Coordinates(-1, mouthCol + 1), BattleFieldProjectile.Direction.DOWN))
            }
    }

    private fun tAttack(battleField: BattleField65, numberOfBlocks: Int) {
        val usedRows = battleField.getAllObjects().filter { it.position.col == battleField.width }.map { it.position.row }
        val remainingRows = ((0 until battleField.height) - usedRows.toSet()).shuffled().toMutableList()
        var blocksRemaining = numberOfBlocks
        val protagonistRow = battleField.protagonist.position.row
        if (protagonistRow in remainingRows) {
            battleField.addProjectile(BreakthroughMouth(Coordinates(protagonistRow, battleField.width), BattleFieldProjectile.Direction.LEFT))
            remainingRows.remove(protagonistRow)
            blocksRemaining--
        }

        repeat(min(blocksRemaining, remainingRows.size)) {
            battleField.addProjectile(BreakthroughMouth(Coordinates(remainingRows[it], battleField.width), BattleFieldProjectile.Direction.LEFT))
        }
    }

    private fun attackFromAbove(battleField: BattleField65) {
        val usedCols = battleField.getAllObjects().filter { it.position.row == -1 }.map { it.position.col }
        val remainingCols = ((0 until battleField.width) - usedCols.toSet()).shuffled()
        val protagonistCol = battleField.protagonist.position.col
        if (protagonistCol in remainingCols) {
            battleField.addProjectile(BreakthroughMouth(Coordinates(-1, protagonistCol), BattleFieldProjectile.Direction.DOWN))
        } else {
            battleField.addProjectile(BreakthroughMouth(Coordinates(-1, remainingCols[0]), BattleFieldProjectile.Direction.DOWN))
        }
    }
}