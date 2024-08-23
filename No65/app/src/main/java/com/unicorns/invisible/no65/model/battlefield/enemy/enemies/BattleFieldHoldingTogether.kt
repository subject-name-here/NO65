package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.ScaryMonster
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean
import kotlin.math.abs


class BattleFieldHoldingTogether : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int
        get() = 44

    override val number: Int = 8
    override val hexagramSymbol: String = "䷇"
    override val nameId: Int
        get() = R.string.battlefield_holding_together_name

    override val centerSymbolColorId: Int = R.color.blue
    override val centerSymbol: String = "\uD83D\uDC65"

    override val outerSkinTrigram = Water
    override val innerHeartTrigram = Earth

    override val defaultFace: String = "\uD83D\uDE00"
    override val damageReceivedFace: String = "\uD83D\uDE1E"
    override val noDamageReceivedFace: String = "\uD83D\uDE43"

    override val animation: CharacterAnimation = CharacterAnimation.LITTLE_SWING

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_holding_together_1
                2 -> R.string.battlefield_holding_together_2
                3 -> R.string.battlefield_holding_together_3
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_holding_together_d
        override fun getVictoryLine(): Int = R.string.battlefield_holding_together_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber % 13) {
            0 -> {
                generateFullLine(battleField)
            }
            4, 5 -> {
                generateRandomLine(battleField)
            }
            8, 9 -> {
                generateHalfRandomLine(battleField)
            }
        }
    }

    private fun generateFullLine(battleField: BattleField65) {
        repeat(battleField.height) {
            battleField.addProjectile(ScaryMonster(Coordinates(it, -1)))
        }
    }

    private fun generateRandomLine(battleField: BattleField65) {
        repeat(battleField.height) {
            if (randBoolean()) {
                battleField.addProjectile(ScaryMonster(Coordinates(it, -1)))
            }
        }
    }

    private fun generateHalfRandomLine(battleField: BattleField65) {
        repeat(battleField.height) {
            if (randBoolean() || abs(battleField.protagonist.position.row - it) < 4) {
                battleField.addProjectile(ScaryMonster(Coordinates(it, -1)))
            }
        }
    }
}