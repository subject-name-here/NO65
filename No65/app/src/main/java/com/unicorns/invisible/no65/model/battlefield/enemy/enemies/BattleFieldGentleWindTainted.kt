package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.House
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.King
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean
import com.unicorns.invisible.no65.util.randInt


class BattleFieldGentleWindTainted : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val nameId: Int
        get() = R.string.battlefield_gentle_wind_tainted_name
    override val number: Int
        get() = 65

    override val attackTimeMvs: Int
        get() = 50
    override val tickTime: Long
        get() = SUPER_FAST_PROJECTILE_SPEED_MILLISECONDS
    override val musicThemeId: Int
        get() = R.raw.battle_gentle_wind_tainted
    override val beatId: Int
        get() = R.raw.sfx_youbeat
    override val animation: CharacterAnimation
        get() = CharacterAnimation.FLOAT
    override val handsSymbol: String
        get() = "љ"
    override val legsSymbol: String
        get() = "~"

    override val defaultFace: String
        get() = "\uD83D\uDE36"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE36"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDE36"

    override val centerSymbol: String
        get() = "∞"
    override val centerSymbolColorId: Int
        get() = R.color.black

    override val hexagramSymbol: String
        get() = "?"
    override val outerSkinTrigram: Trigram
        get() = Wind
    override val innerHeartTrigram: Trigram
        get() = Wind

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_gentle_wind_tainted_1
            2 -> R.string.battlefield_gentle_wind_tainted_2
            3 -> R.string.battlefield_gentle_wind_tainted_3
            4 -> R.string.battlefield_gentle_wind_tainted_4
            5 -> R.string.battlefield_gentle_wind_tainted_5
            6 -> R.string.battlefield_gentle_wind_tainted_6
            7 -> R.string.battlefield_gentle_wind_tainted_7
            else -> R.string.battlefield_gentle_wind_tainted_0
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_gentle_wind_tainted_d
        override fun getVictoryLine(): Int = R.string.battlefield_gentle_wind_tainted_v
    }
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber) {
            1 -> {
                if (tickNumber % 3 == 0) {
                    spawnKingsLine(battleField)
                }
            }
            2, 5 -> {
                if (tickNumber % 3 == 0) {
                    spawnKingsLine(battleField)
                } else {
                    val row = randInt(battleField.height)
                    if (randBoolean()) {
                        battleField.addProjectile(House(Coordinates(row, battleField.width), BattleFieldProjectile.Direction.LEFT))
                    } else {
                        battleField.addProjectile(House(Coordinates(row, -1), BattleFieldProjectile.Direction.RIGHT))
                    }
                }
            }
            else -> {
                when (tickNumber % 3) {
                    0 -> {
                        spawnKingsLine(battleField)
                    }
                    1 -> {
                        val row = randInt(battleField.height)
                        battleField.addProjectile(House(Coordinates(row, battleField.width), BattleFieldProjectile.Direction.LEFT))
                        battleField.addProjectile(King(Coordinates(battleField.height, battleField.protagonist.position.col), BattleFieldProjectile.Direction.UP))
                    }
                    2 -> {
                        val row = randInt(battleField.height)
                        battleField.addProjectile(House(Coordinates(row,-1), BattleFieldProjectile.Direction.RIGHT))
                        battleField.addProjectile(King(Coordinates(battleField.height, battleField.protagonist.position.col), BattleFieldProjectile.Direction.UP))
                    }
                }
            }
        }
    }

    private fun spawnKingsLine(battleField: BattleField65) {
        repeat(battleField.width) { col ->
            battleField.addProjectile(King(Coordinates(battleField.height, col), BattleFieldProjectile.Direction.UP))
        }
    }
}