package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.SpaceDistortion
import com.unicorns.invisible.no65.model.elements.trigram.*
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class BattleFieldSmallPreponderance : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_small_preponderance_name
    override val number: Int
        get() = 62

    override var attackTimeMvs: Int = 5
    override var defenceTimeSec: Int = 5

    override val defaultFace: String
        get() = "\uD83D\uDE11"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE2C"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDE11"

    override val centerSymbol: String
        get() = "❷"
    override val centerSymbolColorId: Int
        get() = R.color.grey

    override val musicThemeId: Int = 0
    override var animation: CharacterAnimation = CharacterAnimation.LITTLE_SWING

    override val hexagramSymbol: String
        get() = "䷽"
    override val outerSkinTrigram: Trigram
        get() = Thunder
    override val innerHeartTrigram: Trigram
        get() = Mountain

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            2 -> R.string.battlefield_small_preponderance_2
            3 -> R.string.battlefield_small_preponderance_3
            4 -> R.string.battlefield_small_preponderance_4
            5 -> R.string.battlefield_small_preponderance_5
            6 -> R.string.battlefield_small_preponderance_6
            7 -> R.string.battlefield_small_preponderance_7
            8 -> R.string.battlefield_small_preponderance_8
            else -> R.string.empty_line
        }

        override fun getDefeatedLine(): Int = R.string.battlefield_small_preponderance_d
        override fun getVictoryLine(): Int = R.string.battlefield_small_preponderance_v
    }


    override suspend fun onBattleBegins(manager: BattleManager) {
        manager.activity.musicPlayer.playMusic(
            R.raw.battle_enemy,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = true
        )

        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_begin_1, withDelay = false)
        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_begin_2)
        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_begin_3)
        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_begin_4)
        manager.drawer.enemyLeaveTheScene()
        delay(1000L)
        manager.activity.musicPlayer.playMusicSuspendTillEnd(
            R.raw.sfx_scratch,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )
        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_begin_5)
        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_begin_6)
        manager.activity.musicPlayer.playMusic(
            R.raw.battle_small_preponderance,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = true
        )
        delay(1000L)
        manager.drawer.enemyReturnToTheScene()
        manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_begin_7)
    }

    override fun onMoveStart(battleField: BattleField) {
        when (battleField.moveNumber) {
            1, 2 -> {
                attackTimeMvs = 28
                defenceTimeSec = 0
            }
            3, 4, 5, 6 -> {
                attackTimeMvs = 0
                defenceTimeSec = 10
            }
            else -> {
                attackTimeMvs = 0
                defenceTimeSec = 15
            }
        }
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber) {
            1 -> {
                val pr = battleField.protagonist.position.row
                battleField.addProjectile(SpaceDistortion(Coordinates(pr, -1), BattleFieldProjectile.Direction.RIGHT))
                battleField.addProjectile(SpaceDistortion(Coordinates(pr, battleField.width), BattleFieldProjectile.Direction.LEFT))
            }
            2 -> {
                if (tickNumber % 3 == 0) {
                    repeat(battleField.width) {
                        battleField.addProjectile(SpaceDistortion(Coordinates(battleField.height, it), BattleFieldProjectile.Direction.UP))
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
        when (battleField.moveNumber) {
            1 -> {
                if (battleField.protagonist.health != battleField.protagonist.maxHealth) {
                    resetStage(battleField)
                }
            }
            2 -> {
                if (battleField.protagonist.health != battleField.protagonist.maxHealth) {
                    resetStage(battleField)
                } else {
                    manager.drawer.hideAll()
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_2_1)
                    manager.drawer.enemyLeaveTheScene()
                    delay(1000L)
                    manager.activity.musicPlayer.playMusicSuspendTillEnd(
                        R.raw.sfx_scratch,
                        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                        isLooping = false
                    )
                    manager.activity.musicPlayer.playMusic(
                        R.raw.battle_small_preponderance2,
                        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                        isLooping = true
                    )
                    delay(1000L)
                    manager.drawer.enemyReturnToTheScene()
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_2_2)
                }
            }
            3 -> {
                if (element != Water) {
                    resetStage(battleField)
                } else {
                    manager.drawer.hideAll()
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_3_1)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_3_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_3_3)
                }
            }
            4 -> {
                if (element != Earth) {
                    resetStage(battleField)
                } else {
                    manager.drawer.hideAll()
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_4_1)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_4_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_4_3)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_4_4)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_4_5)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_4_6)
                }
            }
            5 -> {
                if (element != Mountain) {
                    resetStage(battleField)
                } else {
                    manager.drawer.hideAll()
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_5_1)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_5_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_5_3)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_5_4)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_5_5)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_5_6)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_5_7)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_5_8)
                }
            }
            6 -> {
                if (element != Thunder) {
                    resetStage(battleField)
                } else {
                    manager.drawer.hideAll()
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_6_1)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_6_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_6_3)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_6_4)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_6_5)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_6_6)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_6_7)
                    manager.drawer.enemyLeaveTheScene()
                    delay(1000L)
                    manager.activity.musicPlayer.playMusicSuspendTillEnd(
                        R.raw.sfx_scratch,
                        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                        isLooping = false
                    )
                    manager.activity.musicPlayer.playMusic(
                        R.raw.battle_small_preponderance3,
                        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                        isLooping = true
                    )
                    delay(1000L)
                    manager.drawer.enemyReturnToTheScene()
                    animation = CharacterAnimation.NONE
                    manager.drawer.animateEnemy(animation)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_6_8)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_small_preponderance_after_6_9)
                }
            }
        }
    }

    private fun resetStage(battleField: BattleField65) {
        battleField.redoMove()
    }
}