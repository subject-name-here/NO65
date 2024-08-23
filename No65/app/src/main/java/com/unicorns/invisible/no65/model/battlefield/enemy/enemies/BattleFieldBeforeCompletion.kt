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
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.TRIGRAMS_HELP_AVAILABLE
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.coroutines.delay


class BattleFieldBeforeCompletion : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_MAYBE_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val tickTime: Long = SLOW_PROJECTILE_SPEED_MILLISECONDS
    override var attackTimeMvs: Int = super.attackTimeMvs
    override var defenceTimeSec: Int = super.defenceTimeSec

    override val goNumbersToDelays: List<Pair<Int, Long>> = listOf(
        3 to 450L,
        2 to 450L,
        1 to 450L,
    )

    override val number: Int = 64
    override val hexagramSymbol: String = "䷿"
    override val nameId: Int = R.string.battlefield_before_completion_name

    override val centerSymbolColorId: Int = R.color.white
    override val centerSymbol: String = "\uD83D\uDD1C"

    override val outerSkinTrigram = Fire
    override val innerHeartTrigram = Water

    override val defaultFace: String = "\uD83D\uDC76"
    override val damageReceivedFace: String = "\uD83D\uDE2D"
    override val noDamageReceivedFace: String = "\uD83D\uDC76"

    override val musicThemeId: Int = R.raw.battle_bc
    override val animation: CharacterAnimation = CharacterAnimation.LITTLE_SWING

    override fun onMoveStart(battleField: BattleField) {
        when (battleField.moveNumber) {
            1, 3, 4 -> {
                attackTimeMvs = 7
                defenceTimeSec = 0
            }
            2 -> {
                attackTimeMvs = 3
                defenceTimeSec = 0
            }
            5, 6, 7, 8, 9, 10 -> {
                attackTimeMvs = 0
                defenceTimeSec = 10
            }
            11 -> {
                attackTimeMvs = 9
                defenceTimeSec = 0
            }
            else -> {
                health = 0
            }
        }
    }

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
                1 -> R.string.battlefield_before_completion_1
                2 -> R.string.battlefield_before_completion_2
                3 -> R.string.battlefield_before_completion_3
                4 -> R.string.battlefield_before_completion_4
                5 -> R.string.battlefield_before_completion_5
                6 -> R.string.battlefield_before_completion_6
                7 -> R.string.battlefield_before_completion_7
                8 -> R.string.battlefield_before_completion_8
                9 -> R.string.battlefield_before_completion_9
                10 -> R.string.battlefield_before_completion_10
                11 -> R.string.battlefield_before_completion_11
                else -> R.string.empty_line
            }

        override fun getDefeatedLine(): Int = R.string.battlefield_before_completion_d
        override fun getVictoryLine(): Int = R.string.battlefield_before_completion_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        val moveNumber = battleField.moveNumber
        if (tickNumber > 0) {
            return
        }

        when (moveNumber) {
            1 -> {
                addProjectileFromTop(battleField, battleField.width / 2)
                addProjectileFromBottom(battleField, battleField.width / 2)
            }
            2 -> {
                addProjectileFromLeft(battleField, battleField.height / 2)
                addProjectileFromRight(battleField, battleField.height / 2)
            }
            3 -> {
                addProjectileFromTop(battleField, battleField.width / 2 - 1)
                addProjectileFromTop(battleField, battleField.width / 2)
                addProjectileFromTop(battleField, battleField.width / 2 + 1)
                addProjectileFromBottom(battleField, battleField.width / 2 - 1)
                addProjectileFromBottom(battleField, battleField.width / 2)
                addProjectileFromBottom(battleField, battleField.width / 2 + 1)
                addProjectileFromLeft(battleField, battleField.height / 2)
                addProjectileFromRight(battleField, battleField.height / 2)
            }
            4 -> {
                repeat(battleField.height) {
                    addProjectileFromTop(battleField, it)
                    addProjectileFromBottom(battleField, it)
                }
            }
            11 -> {
                repeat(battleField.width) {
                    addProjectileFromTop(battleField, it)
                    addProjectileFromBottom(battleField, it)
                }
                repeat(battleField.height) {
                    addProjectileFromLeft(battleField, it)
                    addProjectileFromRight(battleField, it)
                }
            }
            else -> {}
        }
    }

    private fun addProjectileFromTop(battleField: BattleField65, col: Int) {
        battleField.addProjectile(
            SpaceDistortion(
                Coordinates(-1, col),
                BattleFieldProjectile.Direction.DOWN
            )
        )
    }

    private fun addProjectileFromBottom(battleField: BattleField65, col: Int) {
        battleField.addProjectile(
            SpaceDistortion(
                Coordinates(battleField.height, col),
                BattleFieldProjectile.Direction.UP
            )
        )
    }

    private fun addProjectileFromLeft(battleField: BattleField65, row: Int) {
        battleField.addProjectile(
            SpaceDistortion(
                Coordinates(row, -1),
                BattleFieldProjectile.Direction.RIGHT
            )
        )
    }

    private fun addProjectileFromRight(battleField: BattleField65, row: Int) {
        battleField.addProjectile(
            SpaceDistortion(
                Coordinates(row, battleField.width),
                BattleFieldProjectile.Direction.LEFT
            )
        )
    }

    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        when (battleField.moveNumber) {
            1, 2, 3 -> {
                if (battleField.protagonist.health != battleField.protagonist.maxHealth) {
                    resetStage(battleField)
                }
            }
            4 -> {
                if (battleField.protagonist.health != battleField.protagonist.maxHealth) {
                    resetStage(battleField)
                } else {
                    manager.drawer.hideAll()
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_4_1)
                }
            }
            5 -> {
                if (element != Earth) {
                    resetStage(battleField)
                }
            }
            6 -> {
                if (element != Heaven) {
                    resetStage(battleField)
                } else {
                    manager.drawer.hideAll()
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_6_1)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_6_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_6_3)
                    delay(1500L)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_6_4)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_6_5)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_6_6)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_6_7)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_6_8)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_6_9)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_6_10)
                }
            }
            7 -> {
                if (element != Water) {
                    resetStage(battleField)
                }
            }
            8 -> {
                if (element != Thunder) {
                    resetStage(battleField)
                }
            }
            9 -> {
                if (element != Fire) {
                    resetStage(battleField)
                }
            }
            10 -> {
                if (element != Wind) {
                    resetStage(battleField)
                } else {
                    manager.drawer.hideAll()
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_10_1)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_10_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_10_3)
                }
            }
            11 -> {
                manager.drawer.hideAll()
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_11_1)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_11_2)
                GlobalState.putBoolean(manager.activity, TRIGRAMS_HELP_AVAILABLE, true)
                delay(1500L)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_11_3)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_11_4)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_11_5)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_before_completion_after_11_6)
                delay(1000L)
            }
        }
    }

    private fun resetStage(battleField: BattleField65) {
        battleField.redoMove()
    }
}