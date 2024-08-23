package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.RVStars
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldWorkOnTheDecayed : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 18
    override val nameId: Int
        get() = R.string.battlefield_work_on_the_decayed_name

    override val centerSymbolColorId: Int = R.color.green
    override val centerSymbol: String = "☝"

    override val hexagramSymbol: String = "䷑"
    override val outerSkinTrigram = Mountain
    override val innerHeartTrigram = Wind

    override var defaultFace: String = "\uD83E\uDD13"
    override val damageReceivedFace: String = "\uD83D\uDE28"
    override val noDamageReceivedFace: String = "\uD83D\uDE02"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_work_on_the_decayed_1
                4 -> R.string.battlefield_work_on_the_decayed_4
                5 -> R.string.battlefield_work_on_the_decayed_5
                6 -> R.string.battlefield_work_on_the_decayed_6
                7 -> R.string.battlefield_work_on_the_decayed_7
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_work_on_the_decayed_d
        override fun getVictoryLine(): Int = R.string.battlefield_work_on_the_decayed_v
    }


    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 4 == 0) {
            repeat(battleField.width) {
                battleField.addProjectile(RVStars(Coordinates(-1, it), BattleFieldProjectile.Direction.DOWN))
            }
        }

        if (tickNumber % 5 == 2) {
            repeat(battleField.width) {
                battleField.addProjectile(RVStars(Coordinates(battleField.height, it), BattleFieldProjectile.Direction.UP))
            }
        }
    }
}