package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.PaperPlane
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Papers
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldBitingThrough(private val hasFiredMadeline: Boolean) : BattleFieldEnemy() {
    override val maxHealth: Int
        get() = BASE_DAMAGE_TO_ENEMY * ENEMY_BAD_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override var attackTimeMvs: Int = super.attackTimeMvs

    override val number: Int = 21
    override val hexagramSymbol: String = "䷔"
    override val nameId: Int = R.string.battlefield_biting_through_name

    override val centerSymbolColorId: Int = R.color.dark_blue
    override val centerSymbol: String = "\uD83D\uDC54"

    override val outerSkinTrigram = Fire
    override val innerHeartTrigram = Thunder

    override val defaultFace: String = "\uD83E\uDD8A"
    override val damageReceivedFace: String = "\uD83D\uDC3A"
    override val noDamageReceivedFace: String = "\uD83D\uDC39"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_biting_through_1
            2 -> if (hasFiredMadeline) {
                R.string.battlefield_biting_through_2_fired
            } else {
                R.string.battlefield_biting_through_2
            }
            3 -> if (hasFiredMadeline) {
                R.string.battlefield_biting_through_3_fired
            } else {
                R.string.battlefield_biting_through_3
            }
            4 -> R.string.battlefield_biting_through_4
            else -> R.string.empty_line
        }
        override fun getDefeatedLine(): Int =
            if (hasFiredMadeline) {
                R.string.battlefield_biting_through_d_fired
            } else {
                R.string.battlefield_biting_through_d
            }
        override fun getVictoryLine(): Int =
            if (hasFiredMadeline) {
                R.string.battlefield_biting_through_v_fired
            } else {
                R.string.battlefield_biting_through_v
            }
    }

    override fun onMoveStart(battleField: BattleField) {
        attackTimeMvs = when (battleField.moveNumber) {
            1 -> 37
            2 -> if (!hasFiredMadeline) {
                8
            } else {
                super.attackTimeMvs
            }
            3, 5, 7 -> battleField.width * battleField.height / 2 - 1
            else -> super.attackTimeMvs
        }
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (battleField.moveNumber == 2 && (!hasFiredMadeline || tickNumber < 5)) {
            return
        }

        when (battleField.moveNumber) {
            1 -> {
                val col = battleField.protagonist.position.col
                battleField.addProjectile(Papers(Coordinates(-1, col)))
            }
            3, 5, 7 -> {
                val cols = setOf(battleField.protagonist.position.col, randInt(battleField.width))
                cols.forEach { col ->
                    battleField.addProjectile(Papers(Coordinates(-1, col)))
                }
            }
            else -> {
                if (tickNumber % 4 == 0) {
                    spawnPlaneWave(battleField)
                } else {
                    spawnPlaneOnProtagonist(battleField)
                }
            }
        }
    }

    private fun spawnPlaneWave(battleField: BattleField65) {
        repeat(battleField.height) { row ->
            battleField.addProjectile(PaperPlane(Coordinates(row, battleField.width)))
        }
    }

    private fun spawnPlaneOnProtagonist(battleField: BattleField65) {
        battleField.addProjectile(
            PaperPlane(
                Coordinates(
                    battleField.protagonist.position.row,
                    battleField.width
                )
            )
        )
    }
}