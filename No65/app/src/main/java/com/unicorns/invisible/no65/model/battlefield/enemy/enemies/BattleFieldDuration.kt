package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.LetterProjectile
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldDuration : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_duration_name
    override val number: Int
        get() = 32

    override val defaultFace: String
        get() = "\uD83E\uDDD1"
    override val damageReceivedFace: String
        get() = "\uD83E\uDDD1"
    override val noDamageReceivedFace: String
        get() = "\uD83E\uDDD2"

    override val centerSymbol: String
        get() = "\uD83D\uDDD3"
    override val centerSymbolColorId: Int
        get() = R.color.red

    override val hexagramSymbol: String
        get() = "䷟"
    override val outerSkinTrigram: Trigram
        get() = Thunder
    override val innerHeartTrigram: Trigram
        get() = Wind

    override val animation: CharacterAnimation
        get() = CharacterAnimation.LITTLE_SWING

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_duration_1
                2 -> R.string.battlefield_duration_2
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_duration_d
        override fun getVictoryLine(): Int = R.string.battlefield_duration_v
    }

    private var freeCell = -1
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            val center = Coordinates(battleField.height / 2, battleField.width / 2)
            freeCell = center.row * battleField.width + center.col
        }
        val mod = if (battleField.moveNumber % 2 == 0) 2 else 3
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