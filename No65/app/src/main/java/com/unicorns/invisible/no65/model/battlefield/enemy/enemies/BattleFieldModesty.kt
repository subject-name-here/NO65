package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Balloon
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BalloonBroken
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BalloonEvil
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.*
import kotlinx.coroutines.delay


class BattleFieldModesty : BattleFieldEnemy() {
    override val maxHealth: Int
        get() = BASE_DAMAGE_TO_ENEMY * ENEMY_BAD_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 15
    override val nameId: Int
        get() = R.string.battlefield_modesty_name

    override val centerSymbolColorId: Int = R.color.blue
    override val centerSymbol: String = "\uD83D\uDE4F"

    override val hexagramSymbol: String = "䷎"
    override val outerSkinTrigram = Earth
    override val innerHeartTrigram = Mountain

    private var state = State.CALM
    override val defaultFace: String
        get() = getFace()
    override val damageReceivedFace: String
        get() = getFace()
    override val noDamageReceivedFace: String
        get() = getFace()
    private val angryFacesList = listOf("⚆", "⚇", "⚈", "⚉")
    private fun getFace(): String {
        return when (state) {
            State.CALM -> "\uD83D\uDE07"
            State.ANGRY -> angryFacesList.random()
            State.BROKEN -> (angryFacesList + "" + "\uD83D\uDE07").random()
        }
    }

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int {
            return if (moveNumber == 1) {
                R.string.battlefield_modesty_1
            } else {
                when (state) {
                    State.CALM -> R.string.empty_line
                    State.ANGRY -> R.string.empty_line
                    State.BROKEN -> {
                        when (maxTurnsDamaged - turnsDamaged - 1) {
                            3 -> R.string.battlefield_modesty_broken_3_moves
                            2 -> R.string.battlefield_modesty_broken_2_moves
                            1 -> R.string.battlefield_modesty_broken_1_moves
                            0 -> R.string.battlefield_modesty_broken_0_moves
                            else -> R.string.battlefield_modesty_broken_n_moves
                        }
                    }
                }
            }
        }
        override fun getDefeatedLine(): Int = when (state) {
            State.CALM -> R.string.battlefield_modesty_d
            State.ANGRY -> R.string.battlefield_modesty_d_angry
            State.BROKEN -> R.string.battlefield_modesty_d_broken
        }
        override fun getVictoryLine(): Int = when (state) {
            State.CALM -> R.string.battlefield_modesty_v
            State.ANGRY -> R.string.battlefield_modesty_v_angry
            State.BROKEN -> if (health > 0) R.string.battlefield_modesty_v_broken else R.string.battlefield_modesty_v_broken_boom
        }
    }

    override fun onTick(manager: BattleManager, tickNumber: Int) {
        launchCoroutine {
            val numOfBlinks = 3
            repeat(numOfBlinks) {
                manager.drawer.updateEnemyFace(this@BattleFieldModesty)
                delay(tickTime / numOfBlinks)
            }
        }
    }
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (state) {
            State.CALM -> calmAttack(battleField, tickNumber)
            State.ANGRY -> angryAttack(battleField, tickNumber)
            State.BROKEN -> when (randInt(3)) {
                0 -> calmAttack(battleField, tickNumber)
                1 -> angryAttack(battleField, tickNumber)
                2 -> brokenAttack(battleField, tickNumber)
            }
        }
    }

    private fun calmAttack(battleField: BattleField65, tickNumber: Int) {
        val cols = if (tickNumber % 4 == 0) {
            (0 until battleField.width)
        } else {
            (0 until battleField.width).takeRand(1)
        }
        cols.forEach { col ->
            battleField.addProjectile(Balloon(Coordinates(battleField.height, col)))
        }
    }
    private fun angryAttack(battleField: BattleField65, tickNumber: Int) {
        val cols = if (tickNumber % 4 == 0) {
            (0 until battleField.width)
        } else {
            (0 until battleField.width).takeRand(1)
        }
        cols.forEach { col ->
            battleField.addProjectile(BalloonEvil(Coordinates(battleField.height, col)))
        }
    }

    private fun brokenAttack(battleField: BattleField65, tickNumber: Int) {
        val cols = if (tickNumber % 4 == 0) {
            (0 until battleField.width)
        } else {
            (0 until battleField.width).takeRand(choose(1, 2))
        }
        cols.forEach { col ->
            when (randInt(3)) {
                0 -> battleField.addProjectile(Balloon(Coordinates(battleField.height, col)))
                1 -> {
                    if (tickNumber % 4 == 0) {
                        battleField.addProjectile(BalloonBroken(Coordinates(battleField.height, col)))
                    } else {
                        battleField.addProjectile(BalloonEvil(Coordinates(battleField.height, col)))
                    }
                }
                2 -> battleField.addProjectile(BalloonBroken(Coordinates(battleField.height, col)))
            }
        }
    }

    private var turnsDamaged = 0
    private val maxTurnsDamaged = ENEMY_NO_HEALTH_MULTIPLIER + 1
    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        if (element == Heaven) {
            return
        }
        if (state == State.BROKEN) {
            turnsDamaged++
        }
        if (turnsDamaged >= maxTurnsDamaged) {
            if (!battleField.protagonist.hasAnkh) {
                manager.activity.musicPlayer.stopAllMusic()
            }
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_modesty_explosion)
            health = 0
            battleField.protagonist.health = 0
            return
        }

        if (state == State.CALM && health <= BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER) {
            state = State.ANGRY
            health = maxHealth
            delay(500L)
            manager.drawer.updateEnemyFace(this)
            delay(500L)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_modesty_turning_angry)
        }
        if (state == State.ANGRY && health <= BASE_DAMAGE_TO_ENEMY) {
            state = State.BROKEN
            health = BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_modesty_turning_broken)
        }
    }

    enum class State {
        CALM,
        ANGRY,
        BROKEN
    }
}