package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.RockIt
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldTheArmy : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 90
    override val defenceTimeSec: Int = 10

    override val number: Int = 7
    override val hexagramSymbol: String = "䷆"
    override val nameId: Int
        get() = R.string.battlefield_the_army_name

    override val centerSymbolColorId: Int = R.color.grey
    override val centerSymbol: String = "⚔"

    override val outerSkinTrigram = Earth
    override val innerHeartTrigram = Water

    override val defaultFace: String = "\uD83D\uDC77\uD83D\uDC77\uD83D\uDC77\uD83D\uDC77"
    override val damageReceivedFace: String = "\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D"
    override val noDamageReceivedFace: String = "\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02"

    override val goNumbersToDelays = listOf(
        100 to 1000L,
        30 to 1000L
    )
    override val goText: String = "3!"
    override val afterGoTextDelay: Long = 1000L

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_the_army_1
                2 -> R.string.battlefield_the_army_2
                3 -> R.string.battlefield_the_army_3
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_the_army_d
        override fun getVictoryLine(): Int = R.string.battlefield_the_army_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        repeat(battleField.moveNumber.coerceAtMost(3)) {
            spawnRockIt(battleField)
        }
    }

    private fun spawnRockIt(battleField: BattleField65) {
        battleField.addProjectile(RockIt(Coordinates(battleField.height, randInt(battleField.width))))
    }
}