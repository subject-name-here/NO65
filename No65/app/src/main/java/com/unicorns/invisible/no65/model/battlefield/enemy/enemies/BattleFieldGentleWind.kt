package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.House
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.King
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.GENTLE_WIND_TAINTED_SHOWN
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt


class BattleFieldGentleWind(private val isFromExtras: Boolean = false) : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int
        get() = 40
    override val tickTime: Long
        get() = FAST_PROJECTILE_SPEED_MILLISECONDS
    override val musicThemeId: Int
        get() = R.raw.battle_gentle_wind
    override val animation: CharacterAnimation
        get() = CharacterAnimation.FLOAT

    override val nameId: Int
        get() = R.string.battlefield_gentle_wind_name
    override val number: Int
        get() = 57

    override val defaultFace: String
        get() = "\uD83E\uDD29"
    override val damageReceivedFace: String
        get() = "\uD83E\uDD2A"
    override val noDamageReceivedFace: String
        get() = "\uD83E\uDD2A"

    override val centerSymbol: String
        get() = "⧓"
    override val centerSymbolColorId: Int
        get() = R.color.red

    override val hexagramSymbol: String
        get() = "䷸"
    override val outerSkinTrigram: Trigram
        get() = Wind
    override val innerHeartTrigram: Trigram
        get() = Wind

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_gentle_wind_1
            2 -> R.string.battlefield_gentle_wind_2
            3 -> R.string.battlefield_gentle_wind_3
            4 -> R.string.battlefield_gentle_wind_4
            5 -> R.string.battlefield_gentle_wind_5
            6 -> R.string.battlefield_gentle_wind_6
            else -> R.string.empty_line
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_gentle_wind_d
        override fun getVictoryLine(): Int = R.string.battlefield_gentle_wind_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber) {
            1 -> {
                if (tickNumber % 3 == 0) {
                    repeat(battleField.width) { col ->
                        battleField.addProjectile(King(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
                    }
                }
            }
            2 -> {
                if (tickNumber % 3 == 0) {
                    repeat(battleField.height) { row ->
                        battleField.addProjectile(House(Coordinates(row, battleField.width), BattleFieldProjectile.Direction.LEFT))
                    }
                }
            }
            3 -> {
                if (tickNumber % 3 == 0) {
                    repeat(battleField.width) { col ->
                        battleField.addProjectile(King(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
                    }
                } else {
                    val row = randInt(battleField.height)
                    battleField.addProjectile(House(Coordinates(row, battleField.width), BattleFieldProjectile.Direction.LEFT))
                }
            }
            else -> {
                when (tickNumber % 3) {
                    0 -> {
                        repeat(battleField.width) { col ->
                            battleField.addProjectile(King(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
                        }
                    }
                    1 -> {
                        val row = randInt(battleField.height)
                        battleField.addProjectile(House(Coordinates(row, battleField.width), BattleFieldProjectile.Direction.LEFT))
                        battleField.addProjectile(House(Coordinates(battleField.protagonist.position.row, battleField.width), BattleFieldProjectile.Direction.LEFT))
                    }
                    2 -> {
                        val row = randInt(battleField.height)
                        battleField.addProjectile(House(Coordinates(row,-1), BattleFieldProjectile.Direction.RIGHT))
                        battleField.addProjectile(House(Coordinates(battleField.protagonist.position.row,-1), BattleFieldProjectile.Direction.RIGHT))
                    }
                }
            }
        }
    }

    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        if (health <= 0) {
            if (GlobalState.getBoolean(manager.activity, GENTLE_WIND_TAINTED_SHOWN) || isFromExtras) {
                manager.activity.musicPlayer.stopMusicByResourceId(musicThemeId)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_gentle_wind_on_death_short_1)
            } else {
                GlobalState.putBoolean(manager.activity, GENTLE_WIND_TAINTED_SHOWN, true)
                manager.activity.musicPlayer.stopMusicByResourceId(musicThemeId)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_gentle_wind_on_death_long_1, withDelay = false)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_gentle_wind_on_death_long_2)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_gentle_wind_on_death_long_3)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_gentle_wind_on_death_long_4)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_gentle_wind_on_death_long_5)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_gentle_wind_on_death_long_6, withDelay = false)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_gentle_wind_on_death_long_7)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_gentle_wind_on_death_long_8)
            }
        }
    }
}