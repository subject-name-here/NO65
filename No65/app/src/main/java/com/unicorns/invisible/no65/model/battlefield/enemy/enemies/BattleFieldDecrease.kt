package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.DollarCrease
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean
import com.unicorns.invisible.no65.util.randInt


class BattleFieldDecrease(val isBrotherDead: Boolean) : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_decrease_name
    override val number: Int
        get() = 41

    override val attackTimeMvs: Int
        get() = 32

    override val defaultFace: String
        get() = if (isBrotherDead) "\uD83D\uDE24" else "\uD83E\uDD28"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE23"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDE1B"

    override val centerSymbol: String
        get() = "\uD83D\uDCC9"
    override val centerSymbolColorId: Int
        get() = R.color.dark_red

    override val hexagramSymbol: String
        get() = "䷨"
    override val outerSkinTrigram: Trigram
        get() = Mountain
    override val innerHeartTrigram: Trigram
        get() = Lake

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = if (isBrotherDead) {
            when (moveNumber) {
                1 -> R.string.battlefield_decrease_dead_bro_1
                else -> R.string.empty_line
            }
        } else {
            when (moveNumber) {
                1 -> R.string.battlefield_decrease_1
                2 -> R.string.battlefield_decrease_2
                3 -> R.string.battlefield_decrease_3
                else -> R.string.empty_line
            }
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_decrease_d
        override fun getVictoryLine(): Int = if (isBrotherDead) R.string.battlefield_decrease_v_dead_bro else R.string.battlefield_decrease_v
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
                    battleField.addProjectile(DollarCrease(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
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
                        battleField.addProjectile(DollarCrease(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
                    }
                }
            }
        }
    }
}