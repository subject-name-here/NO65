package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Alien
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.takeRand
import kotlin.math.min


class BattleFieldRetreat : BattleFieldEnemy() {
    override val attackTimeMvs: Int
        get() = 48

    override val nameId: Int
        get() = R.string.battlefield_retreat_name
    override val number: Int
        get() = 33

    override var defaultFace: String = "\uD83D\uDC7E"
    override val damageReceivedFace: String
        get() = defaultFace
    override val noDamageReceivedFace: String
        get() = defaultFace

    override val centerSymbol: String
        get() = "\uD835\uDEBA"
    override val centerSymbolColorId: Int
        get() = R.color.dark_blue

    override val hexagramSymbol: String
        get() = "䷠"
    override val outerSkinTrigram: Trigram
        get() = Heaven
    override val innerHeartTrigram: Trigram
        get() = Mountain

    override val lineGenerator: BattleFieldLineGenerator
        get() = object : BattleFieldLineGenerator {
            override fun getLine(moveNumber: Int): Int {
                return when (moveNumber) {
                    1 -> R.string.battlefield_retreat_1
                    2 -> R.string.battlefield_retreat_2
                    3 -> R.string.battlefield_retreat_3
                    else -> R.string.empty_line
                }
            }
            override fun getDefeatedLine(): Int = R.string.battlefield_retreat_d
            override fun getVictoryLine(): Int = R.string.battlefield_retreat_v
        }

    override suspend fun onBattleBegins(manager: BattleManager) {
        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_retreat_begin_1)
        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_retreat_begin_2)

        defaultFace = "\uD83D\uDC7D"
        manager.drawer.updateEnemyFace(this)
        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_retreat_begin_3)
        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_retreat_begin_4)
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 3 == 1) {
            val numOfAliens = min(battleField.moveNumber, (battleField.height - 1) / 3)
            spawnAliens(battleField, numOfAliens)
        }
    }

    private fun spawnAliens(battleField: BattleField65, numOfAliens: Int) {
        val rows = (0 until battleField.height).takeRand(numOfAliens)
        rows.forEach { row ->
            battleField.addProjectile(Alien(Coordinates(row, battleField.width - 1)))
        }
    }
}