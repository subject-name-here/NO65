package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Avalanche
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Snow
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.KSM_BOSS_REVEALED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean
import com.unicorns.invisible.no65.util.randBooleanPercent
import com.unicorns.invisible.no65.view.music.MusicPlayer


class BattleFieldKeepingStillMountain(private val isFromExtras: Boolean = false) : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * TOUGH_BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 28

    override val number: Int = 52
    override val hexagramSymbol: String = "䷳"
    override val nameId: Int
        get() = R.string.battlefield_keeping_still_mountain_name

    override val centerSymbolColorId: Int = R.color.grey
    override val centerSymbol: String = "\uD83D\uDDFB"

    override val outerSkinTrigram = Mountain
    override val innerHeartTrigram = Mountain

    override val defaultFace: String = "\uD83D\uDDFF"
    override val damageReceivedFace: String = "\uD83D\uDDFF"
    override val noDamageReceivedFace: String = "\uD83D\uDDFF"

    override val areAttacksMirrored: Boolean = true

    override var musicThemeId: Int = R.raw.battle_enemy

    private var hasBossRevealed = false
    override suspend fun onBattleBegins(manager: BattleManager) {
        hasBossRevealed = if (isFromExtras) {
            true
        } else {
            GlobalState.getBoolean(manager.activity, KSM_BOSS_REVEALED)
        }
        if (hasBossRevealed) {
            musicThemeId = R.raw.battle_enemy_boss
        }
    }

    private var attackLinesSaid = 0
    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = if (hasBossRevealed) {
            when (attackLinesSaid++) {
                0 -> R.string.battlefield_keeping_still_mountain_boss_1
                5 -> R.string.battlefield_keeping_still_mountain_boss_2
                10 -> R.string.battlefield_keeping_still_mountain_boss_3
                15 -> R.string.battlefield_keeping_still_mountain_boss_4
                20 -> R.string.battlefield_keeping_still_mountain_boss_5
                else -> R.string.empty_line
            }
        } else {
            when (moveNumber) {
                1 -> R.string.battlefield_keeping_still_mountain_1
                else -> R.string.empty_line
            }
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_keeping_still_mountain_d
        override fun getVictoryLine(): Int = if (hasBossRevealed) {
            R.string.battlefield_keeping_still_mountain_v_boss
        } else {
            R.string.battlefield_keeping_still_mountain_v
        }
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (!hasBossRevealed) {
            if (tickNumber % 4 == 0) {
                generateSnow(battleField)
            }
            return
        }
        if (tickNumber % 6 < 3) {
            generateSnow(battleField, true)
        }
        if (tickNumber % 6 == 5) {
            generateAvalanche(battleField)
        }
    }

    private fun generateSnow(battleField: BattleField65, rand: Boolean = false) {
        repeat(battleField.width) {
            if (!rand || randBooleanPercent(66)) {
                battleField.addProjectile(Snow(Coordinates(-1, it), BattleFieldProjectile.Direction.DOWN))
            }
        }
    }
    private fun generateAvalanche(battleField: BattleField65) {
        if (randBoolean()) {
            repeat(battleField.height) {
                battleField.addProjectile(Avalanche(Coordinates(it, -1), BattleFieldProjectile.Direction.RIGHT))
            }
        } else {
            repeat(battleField.height) {
                battleField.addProjectile(Avalanche(Coordinates(it, battleField.width), BattleFieldProjectile.Direction.LEFT))
            }
        }
    }

    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        if (!hasBossRevealed && result.damage > 0) {
            manager.activity.musicPlayer.stopAllMusic()

            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_keeping_still_mountain_boss_reveal_1)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_keeping_still_mountain_boss_reveal_2)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_keeping_still_mountain_boss_reveal_3)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_keeping_still_mountain_boss_reveal_4)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_keeping_still_mountain_boss_reveal_5)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_keeping_still_mountain_boss_reveal_6)

            GlobalState.putBoolean(manager.activity, KSM_BOSS_REVEALED, true)
            hasBossRevealed = true
            manager.activity.musicPlayer.playMusic(
                R.raw.battle_enemy_boss,
                behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                isLooping = true
            )
        }
    }
}