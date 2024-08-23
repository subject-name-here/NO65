package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.DollarCrease
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean
import com.unicorns.invisible.no65.util.randInt


class BattleFieldIncrease(val isBrotherDead: Boolean) : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_increase_name
    override val number: Int
        get() = 42

    override val attackTimeMvs: Int
        get() = 32

    override val defaultFace: String
        get() = if (isBrotherDead) "\uD83D\uDE24" else "\uD83D\uDE00"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE23"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDE1B"

    override val centerSymbol: String
        get() = "\uD83D\uDCC8"
    override val centerSymbolColorId: Int
        get() = R.color.green

    override val hexagramSymbol: String
        get() = "䷩"
    override val outerSkinTrigram: Trigram
        get() = Wind
    override val innerHeartTrigram: Trigram
        get() = Thunder

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = if (isBrotherDead) {
            when (moveNumber) {
                1 -> R.string.battlefield_increase_dead_bro_1
                else -> R.string.empty_line
            }
        } else {
            when (moveNumber) {
                1 -> R.string.battlefield_increase_1
                2 -> R.string.battlefield_increase_2
                3 -> R.string.battlefield_increase_3
                else -> R.string.empty_line
            }
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_increase_d
        override fun getVictoryLine(): Int = if (isBrotherDead) R.string.battlefield_increase_v_dead_bro else R.string.battlefield_increase_v
    }

    private var safeCol = -1
    private var wallTimes = 0
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            safeCol = -1
            wallTimes = 0
        }

        when (tickNumber % 3) {
            0 -> {
                repeat(battleField.width) { col ->
                    battleField.addProjectile(DollarCrease(Coordinates(battleField.height, col), BattleFieldProjectile.Direction.UP))
                }
                safeCol = if (randBoolean()) {
                    wallTimes = 0
                    randInt(battleField.width)
                } else {
                    wallTimes++
                    if (wallTimes >= 3) {
                        wallTimes = 0
                        randInt(battleField.width)
                    } else {
                        -1
                    }
                }
            }
            else -> {
                repeat(battleField.width) { col ->
                    if (col != safeCol) {
                        battleField.addProjectile(DollarCrease(Coordinates(battleField.height, col), BattleFieldProjectile.Direction.UP))
                    }
                }
            }
        }
    }
}