package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.LetterProjectile
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldInfluence : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_influence_name
    override val number: Int
        get() = 31

    override val attackTimeMvs: Int
        get() = 35

    override val defaultFace: String
        get() = "\uD83D\uDC73"
    override val damageReceivedFace: String
        get() = "\uD83D\uDC73"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDC73"

    override val centerSymbol: String
        get() = "\uD83D\uDDBC"
    override val centerSymbolColorId: Int
        get() = R.color.yellow

    override val hexagramSymbol: String
        get() = "䷞"
    override val outerSkinTrigram: Trigram
        get() = Lake
    override val innerHeartTrigram: Trigram
        get() = Mountain

    override var rowViewsShuffleSeed: Int = randInt(22229)

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_influence_1
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_influence_d
        override fun getVictoryLine(): Int = R.string.battlefield_influence_v
    }

    override fun onMoveStart(battleField: BattleField) {
        rowViewsShuffleSeed = randInt(22229)
    }

    private var freeCell = -1
    private var mod = 1
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            val center = Coordinates(battleField.height / 2, battleField.width / 2)
            freeCell = center.row * battleField.width + center.col
            mod = when {
                battleField.moveNumber == 1 -> 3
                health <= BASE_DAMAGE_TO_ENEMY * 3 -> 3
                else -> 4
            }
        }
        when (tickNumber % mod) {
            0 -> {
                val newFreeCell = randInt(battleField.width * battleField.height)
                repeat(battleField.height) { row ->
                    repeat(battleField.width) { col ->
                        if (row * battleField.width + col != freeCell) {
                            battleField.addProjectile(
                                LetterProjectile(
                                    Coordinates(row, col),
                                    BattleFieldProjectile.Direction.NO_MOVEMENT,
                                    newFreeCell.toString()
                                )
                            )
                        }
                    }
                }
                freeCell = newFreeCell
            }
            1 -> {
                battleField.removeProjectiles()
            }
        }
    }
}