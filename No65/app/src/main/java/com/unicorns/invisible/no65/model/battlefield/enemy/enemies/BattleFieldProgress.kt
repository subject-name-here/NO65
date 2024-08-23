package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Leg
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldProgress : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 30

    override val number: Int = 35
    override val nameId: Int
        get() = R.string.battlefield_progress_name

    override val centerSymbolColorId: Int = R.color.pink
    override val centerSymbol: String = "\uD83E\uDDE0"

    override val hexagramSymbol: String = "䷢"
    override val outerSkinTrigram = Fire
    override val innerHeartTrigram = Earth

    override val defaultFace: String = "\uD83D\uDE41"
    override val damageReceivedFace: String = "\uD83D\uDE41"
    override val noDamageReceivedFace: String = "\uD83D\uDE41"

    override val legsSymbol: String = " "

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_progress_1
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_progress_dont_care
        override fun getVictoryLine(): Int = R.string.battlefield_progress_dont_care
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        val blockSize = when (battleField.moveNumber) {
            1, 3 -> battleField.height
            2 -> battleField.height / 2 + 1
            else -> battleField.height / 3 + 1
        }
        if (tickNumber % blockSize < blockSize - 2) {
            repeat(battleField.width) {
                battleField.addProjectile(Leg(Coordinates(-1, it), BattleFieldProjectile.Direction.DOWN))
            }
        }
    }
}