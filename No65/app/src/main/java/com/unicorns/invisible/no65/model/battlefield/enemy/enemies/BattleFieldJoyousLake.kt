package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.JoyousLakeHeart
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.SixtyFiveHeart
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldJoyousLake : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER
    override var health: Int = maxHealth
    override val attackTimeMvs: Int = 15

    override val number: Int = 58

    override val nameId: Int
        get() = R.string.battlefield_joyous_lake_name

    override val centerSymbolColorId: Int = R.color.red
    override val centerSymbol: String = "\uD83D\uDC9D"

    override val hexagramSymbol: String = "䷹"
    override val outerSkinTrigram = Lake
    override val innerHeartTrigram = Lake

    override val defaultFace: String = "\uD83D\uDC78"
    override val damageReceivedFace: String = "\uD83D\uDC78"
    override val noDamageReceivedFace: String = "\uD83D\uDC78"

    override val musicThemeId: Int = R.raw.battle_joyous_lake

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_joyous_lake_1
                else -> R.string.battlefield_joyous_lake_0
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_joyous_lake_d
        override fun getVictoryLine(): Int = R.string.battlefield_joyous_lake_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber > 3) {
            spawnLineOfRedHearts(battleField)
        } else {
            spawnLineOfBlackHearts(battleField)
        }

        val numberOfRedHearts = battleField
            .getAllObjects()
            .filterIsInstance<JoyousLakeHeart>()
            .size
        if (numberOfRedHearts % 5 != 0) {
            health = 0
        }
    }

    private fun spawnLineOfRedHearts(battleField: BattleField65) {
        repeat(battleField.width) {
            battleField.addProjectile(JoyousLakeHeart(Coordinates(-1, it)))
        }
    }
    private fun spawnLineOfBlackHearts(battleField: BattleField65) {
        repeat(battleField.width) {
            battleField.addProjectile(SixtyFiveHeart(Coordinates(-1, it), BattleFieldProjectile.Direction.DOWN))
        }
    }
}