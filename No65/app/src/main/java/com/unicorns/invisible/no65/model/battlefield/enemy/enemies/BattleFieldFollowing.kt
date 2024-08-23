package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Hiss
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldFollowing : BattleFieldEnemy() {
    override val number: Int = 17
    override val nameId: Int
        get() = R.string.battlefield_following_name

    override val centerSymbolColorId: Int = R.color.red
    override val centerSymbol: String = "⚝"

    override val hexagramSymbol: String = "䷐"
    override val outerSkinTrigram = Lake
    override val innerHeartTrigram = Thunder

    override val defaultFace: String = "\uD83D\uDE22"
    override val damageReceivedFace: String = "\uD83D\uDE25"
    override val noDamageReceivedFace: String = "\uD83D\uDE22"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_following_1
                2 -> R.string.battlefield_following_2
                3 -> R.string.battlefield_following_3
                4 -> R.string.battlefield_following_4
                5 -> R.string.battlefield_following_5
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_following_d
        override fun getVictoryLine(): Int = R.string.battlefield_following_v
    }

    private var currentFreeCol = -1
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            currentFreeCol = -1
        }
        val gap = when (battleField.moveNumber) {
            1 -> 5
            2, 3 -> 4
            else -> 3
        }

        when {
            tickNumber % gap == 0 -> {
                battleField.addProjectile(Hiss(Coordinates(-1, currentFreeCol)))
                currentFreeCol = randInt(battleField.width)
                battleField.addProjectile(Hiss(Coordinates(-1, currentFreeCol)))
            }
            else -> {
                repeat(battleField.width) {
                    if (it != currentFreeCol) {
                        battleField.addProjectile(Hiss(Coordinates(-1, it)))
                    }
                }
            }
        }
    }
}