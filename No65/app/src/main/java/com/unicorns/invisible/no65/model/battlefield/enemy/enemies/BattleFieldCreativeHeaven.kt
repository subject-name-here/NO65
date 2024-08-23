package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.LittleAngel
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.SordidDetails
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.CREATIVE_HEAVEN_BATTLE_IN_PROGRESS
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.getNthBit
import com.unicorns.invisible.no65.view.music.MusicPlayer


class BattleFieldCreativeHeaven(
    private val hasBeginning: Boolean,
    private val isFromExtras: Boolean = false
) : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 30
    override val defenceTimeSec: Int = 2

    override val projectilePassesCellsPerMove: Int = 2

    override val number: Int = 1
    override val hexagramSymbol: String = "䷀"
    override val nameId: Int
        get() = R.string.battlefield_creative_heaven_name

    override val centerSymbolColorId: Int = R.color.black
    override val centerSymbol: String = "⏳"

    override val outerSkinTrigram = Heaven
    override val innerHeartTrigram = Heaven

    override var defaultFace: String = "\uD83D\uDE0F"
    override val damageReceivedFace: String = "\uD83D\uDE2C"
    override val noDamageReceivedFace: String = "\uD83D\uDE0F"

    override val goNumbersToDelays
        get() = if (hasBeginning) {
            super.goNumbersToDelays
        } else {
            emptyList()
        }
    override val goText: String = ""
    override val animation: CharacterAnimation = CharacterAnimation.FLOAT
    override val musicThemeId: Int = R.raw.battle_creative_heaven

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_creative_heaven_1
                2 -> R.string.battlefield_creative_heaven_2
                3 -> R.string.battlefield_creative_heaven_3
                4 -> R.string.battlefield_creative_heaven_4
                5 -> R.string.battlefield_creative_heaven_5
                6 -> R.string.battlefield_creative_heaven_6
                7 -> R.string.battlefield_creative_heaven_7
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int {
            defaultFace = "\uD83D\uDE28"
            return R.string.battlefield_creative_heaven_d
        }
        override fun getVictoryLine(): Int = R.string.battlefield_creative_heaven_v
    }

    override fun onTick(manager: BattleManager, tickNumber: Int) {
        if (manager.battleField.moveNumber == 1 && tickNumber == 3 && !isFromExtras) {
            GlobalState.putBoolean(manager.activity, CREATIVE_HEAVEN_BATTLE_IN_PROGRESS, true)
        }
    }

    private var currentShift = 0
    private val shiftRange = (1..6)
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber) {
            1 -> {
                if (tickNumber % 4 == 0) {
                    generateProjectiles(battleField)
                }
            }
            2 -> {
                if (tickNumber % 4 == 0) {
                    generateProjectiles(battleField)
                    generateProjectiles(battleField, 1)
                }
            }
            3 -> {
                val currentDupletInSextuplet = (tickNumber % 6) / 2
                if (tickNumber == 0) {
                    currentShift = shiftRange.random()
                }
                val shift = getNthBit(currentShift, currentDupletInSextuplet)
                generateProjectiles(battleField, shift)
            }
            4, 5 -> {
                val currentDupletInSextuplet = (tickNumber % 6) / 2
                if (currentDupletInSextuplet == 0) {
                    currentShift = shiftRange.random()
                }
                val shift = getNthBit(currentShift, currentDupletInSextuplet)
                generateProjectiles(battleField, shift)
            }
            6, 7 -> {
                val currentDupletInSextuplet = (tickNumber % 6) / 2
                if (currentDupletInSextuplet == 0) {
                    currentShift = (shiftRange - currentShift).random()
                }
                val shift = getNthBit(currentShift, currentDupletInSextuplet)
                generateProjectiles(battleField, shift)
            }
            else -> {}
        }
    }

    private fun generateProjectiles(battleField: BattleField65, shift: Int = 0) {
        val height = battleField.height
        val direction = BattleFieldProjectile.Direction.UP
        val row = height + shift
        if (shift == 0) {
            repeat(battleField.width) {
                battleField.addProjectile(LittleAngel(Coordinates(row, it), direction))
            }
        } else {
            repeat(battleField.width) {
                battleField.addProjectile(SordidDetails(Coordinates(row, it), direction))
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
            return
        }
        when {
            result.damage > BASE_DAMAGE_TO_ENEMY * 2 -> {
                defaultFace = "\uD83D\uDE15"
                manager.drawer.updateEnemyFace(this)
                restart(battleField, manager, R.string.battlefield_creative_heaven_much_damage)
                defaultFace = "\uD83D\uDE0F"
                manager.drawer.updateEnemyFace(this)
            }
            health <= BASE_DAMAGE_TO_ENEMY * 2 -> {
                restart(battleField, manager, R.string.battlefield_creative_heaven_close_to_victory)
            }
            battleField.moveNumber >= 7 -> {
                restart(battleField, manager, R.string.battlefield_creative_heaven_witness_power)
            }
        }
    }

    private suspend fun restart(battleField: BattleField65, manager: BattleManager, messageId: Int) {
        manager.activity.musicPlayer.stopMusicByResourceId(musicThemeId)
        manager.drawer.showTextInEnemySpeechBubbleSuspend(messageId)
        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_creative_heaven_ashes_to_ashes)
        manager.drawer.hideAll()
        manager.drawer.showBlackScreen()
        manager.activity.musicPlayer.playMusicSuspendTillEnd(
            R.raw.sfx_knocks_reversed,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
        manager.drawer.stopBlackScreen()
        manager.activity.musicPlayer.playMusic(
            musicThemeId,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = true
        )
        battleField.restartCreativeHeaven()
    }
}