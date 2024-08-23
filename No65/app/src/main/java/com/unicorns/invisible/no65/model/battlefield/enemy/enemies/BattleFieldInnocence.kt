package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Arrow15
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldInnocence : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_BAD_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val nameId: Int
        get() = R.string.battlefield_innocence_name
    override val number: Int
        get() = 25

    override val defaultFace: String
        get() = "\uD83D\uDC36"
    override val damageReceivedFace: String
        get() = "\uD83D\uDC36"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDC36"

    override val centerSymbol: String
        get() = "\uD83C\uDF23"
    override val centerSymbolColorId: Int
        get() = R.color.true_yellow

    override val hexagramSymbol: String
        get() = "䷘"
    override val outerSkinTrigram: Trigram
        get() = Heaven
    override val innerHeartTrigram: Trigram
        get() = Thunder

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_innocence_1
            5 -> R.string.battlefield_innocence_5
            6 -> R.string.battlefield_innocence_6
            else -> R.string.empty_line
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_innocence_d
        override fun getVictoryLine(): Int = R.string.battlefield_innocence_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber != 0) {
            if (battleField.protagonist.position.row != 0) {
                battleField.addProjectile(Arrow15(Coordinates(0, battleField.protagonist.position.col)))
            } else {
                battleField.addProjectile(Arrow15(Coordinates(1, battleField.protagonist.position.col)))
            }
        }
    }
}