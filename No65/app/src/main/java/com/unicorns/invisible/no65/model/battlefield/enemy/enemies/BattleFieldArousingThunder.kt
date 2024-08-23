package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Lightning
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.TeslaSphere
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.AT_EVADED_LIGHTNING
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.AT_LIGHTNING_STRIKE_REACHED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt
import com.unicorns.invisible.no65.util.takeRand
import kotlin.math.ceil


class BattleFieldArousingThunder(private val isFromExtras: Boolean = false): BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_BAD_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 51
    override val hexagramSymbol: String = "䷲"
    override val nameId: Int = R.string.battlefield_arousing_thunder_name

    override val centerSymbolColorId: Int = R.color.purple
    override val centerSymbol: String = "∅"

    override val outerSkinTrigram = Thunder
    override val innerHeartTrigram = Thunder

    private var state = State.IDLE
    override val defaultFace: String
        get() = when (state) {
            State.IDLE -> "\uD83E\uDD10"
            State.SURPRISED -> "\uD83D\uDE2E"
            State.DOWN -> "\uD83D\uDE13"
        }
    override val damageReceivedFace: String
        get() = when (state) {
            State.IDLE -> defaultFace
            State.SURPRISED -> defaultFace
            State.DOWN -> "\uD83D\uDE30"
        }
    override val noDamageReceivedFace: String
        get() = defaultFace

    override var attackTimeMvs: Int = 32

    override val animation: CharacterAnimation
        get() = CharacterAnimation.FLOAT
    override val musicThemeId: Int
        get() = R.raw.battle_arousing_thunder

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_arousing_thunder_1
                2 -> R.string.battlefield_arousing_thunder_2
                3 -> R.string.battlefield_arousing_thunder_3
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_arousing_thunder_d
        override fun getVictoryLine(): Int = R.string.battlefield_arousing_thunder_v
    }

    override fun onMoveStart(battleField: BattleField) {
        val lightningsPerLife = BattleFieldProtagonist.BASIC_HEALTH.toDouble() / Lightning.DAMAGE
        attackTimeMvs = when {
            battleField.moveNumber % 2 == 0 -> (ceil(lightningsPerLife)).toInt() * 2
            else -> 32
        }
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (battleField.moveNumber % 2 == 1) {
            when (tickNumber % 4) {
                0 -> battleField.removeProjectiles()
                1 -> generateSpheres(battleField)
                2, 3 -> {}
            }
        } else {
            when (tickNumber % 2) {
                0 -> {
                    battleField.removeProjectiles()
                }
                1 -> {
                    generateLightningStrike(battleField)
                }
            }
        }
    }

    private var previousHP = -1
    private var surprisedStateTriggered = false
    override fun onTick(manager: BattleManager, tickNumber: Int) {
        if (manager.battleField.moveNumber == 2) {
            when (tickNumber % 2) {
                0 -> {
                    previousHP = manager.battleField.protagonist.health
                }
                1 -> {
                    if (previousHP == manager.battleField.protagonist.health && !surprisedStateTriggered) {
                        if (!isFromExtras) {
                            GlobalState.putBoolean(manager.activity, AT_EVADED_LIGHTNING, true)
                        }
                        surprisedStateTriggered = true
                        state = State.SURPRISED
                        manager.drawer.updateEnemyFace(this)
                    }
                }
            }
        }
    }
    override fun onMoveRepeat(battleField: BattleField) {
        if (battleField.moveNumber == 2) {
            surprisedStateTriggered = false
            state = State.IDLE
        }
    }

    private fun generateSpheres(battleField: BattleField65) {
        val height = battleField.height
        val moveNumber = battleField.moveNumber
        var numOfSpheresOnSide = if (moveNumber == 1) height / 2 else height - 1

        when (moveNumber / 2) {
            0 -> {}
            1 -> {
                val row = battleField.protagonist.position.row
                battleField.addProjectile(TeslaSphere(Coordinates(row, 0)))
                battleField.addProjectile(TeslaSphere(Coordinates(row, battleField.width - 1)))
                numOfSpheresOnSide--
            }
            else -> {
                val row = battleField.protagonist.position.row
                (row - 1..row + 1).intersect(0 until battleField.height).forEach {
                    battleField.addProjectile(TeslaSphere(Coordinates(it, 0)))
                    battleField.addProjectile(TeslaSphere(Coordinates(it, battleField.width - 1)))
                    numOfSpheresOnSide--
                }
            }
        }
        when (moveNumber / 2) {
            in 0..1 -> {
                repeat(numOfSpheresOnSide) {
                    val rowLeft = randInt(battleField.height)
                    val rowRight = randInt(battleField.height)
                    battleField.addProjectile(TeslaSphere(Coordinates(rowLeft, 0)))
                    battleField.addProjectile(TeslaSphere(Coordinates(rowRight, battleField.width - 1)))
                }
            }
            else -> {
                val rowsLeft = (0 until battleField.height).takeRand(numOfSpheresOnSide)
                val rowsRight = (0 until battleField.height).takeRand(numOfSpheresOnSide)
                for (rowLeft in rowsLeft) {
                    battleField.addProjectile(TeslaSphere(Coordinates(rowLeft, 0)))
                }
                for (rowRight in rowsRight) {
                    battleField.addProjectile(TeslaSphere(Coordinates(rowRight, battleField.width - 1)))
                }
            }
        }

    }

    private fun generateLightningStrike(battleField: BattleField65) {
        val deltas = (-1..1)
        val position = battleField.protagonist.position
        for (deltaX in deltas) {
            for (deltaY in deltas) {
                battleField.addProjectile(Lightning(position + Coordinates(deltaX, deltaY)))
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
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_arousing_thunder_after_1_1)
                if (!isFromExtras) {
                    val reachedLS = GlobalState.getInt(manager.activity, AT_LIGHTNING_STRIKE_REACHED, 0)
                    GlobalState.putInt(manager.activity, AT_LIGHTNING_STRIKE_REACHED, reachedLS + 1)
                }
            }
            2 -> {
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_arousing_thunder_after_2_1)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_arousing_thunder_after_2_2)
                state = State.DOWN
                manager.drawer.updateEnemyFace(this)
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_arousing_thunder_after_2_3)
                state = State.IDLE
                manager.drawer.updateEnemyFace(this)
            }
        }
    }

    enum class State {
        IDLE,
        SURPRISED,
        DOWN
    }
}