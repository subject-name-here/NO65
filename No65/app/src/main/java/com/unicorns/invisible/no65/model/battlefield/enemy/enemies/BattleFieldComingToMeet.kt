package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.HighFive
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.LowFive
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.LowerFive
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.choose
import com.unicorns.invisible.no65.util.randCoordinatesWithExclusion


class BattleFieldComingToMeet : BattleFieldEnemy() {
    override val maxHealth: Int = ENEMY_BAD_HEALTH_MULTIPLIER * BASE_DAMAGE_TO_ENEMY
    override var health: Int = maxHealth

    override val number: Int = 44
    override val hexagramSymbol: String = "䷫"
    override val nameId: Int
        get() = R.string.battlefield_coming_to_meet_name

    override val centerSymbolColorId: Int = R.color.true_yellow
    override val centerSymbol: String = "\uD83C\uDF7B"

    override val outerSkinTrigram = Heaven
    override val innerHeartTrigram = Wind

    private var state = State.NORMAL
    override val defaultFace: String
        get() = getFace()
    override val damageReceivedFace: String
        get() = getFace()
    override val noDamageReceivedFace: String
        get() = getFace()
    private fun getFace(): String {
        return when (state) {
            State.NORMAL -> "\uD83D\uDE26"
            State.ROBOT -> choose("\uD83E\uDD00", "\uD83E\uDD01", "\uD83E\uDD02")
        }
    }

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (state) {
                State.NORMAL -> {
                    when (moveNumber) {
                        1 -> R.string.battlefield_coming_to_meet_1
                        2 -> R.string.battlefield_coming_to_meet_2
                        3 -> R.string.battlefield_coming_to_meet_3
                        else -> R.string.empty_line
                    }
                }
                State.ROBOT -> R.string.battlefield_coming_to_meet_robot_1
            }

        override fun getDefeatedLine(): Int = R.string.battlefield_coming_to_meet_d
        override fun getVictoryLine(): Int = when (state) {
            State.NORMAL -> R.string.battlefield_coming_to_meet_v
            State.ROBOT -> R.string.battlefield_coming_to_meet_v_robot
        }
    }

    private val currentCoordinates = arrayListOf<Coordinates>()
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            currentCoordinates.clear()
        }
        if (state == State.ROBOT) {
            onTickRobot(battleField)
            return
        }

        when (tickNumber % 2) {
            0 -> {
                generateHighFives(battleField)
                currentCoordinates.clear()
                generateLowerFive(battleField)
            }
            1 -> {
                generateLowFives(battleField)
            }
        }
    }

    private fun onTickRobot(battleField: BattleField65) {
        generateHighFives(battleField)
        generateLowFives(battleField)
        currentCoordinates.clear()
        generateLowerFive(battleField)
    }

    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        if (state == State.NORMAL && health < maxHealth) {
            state = State.ROBOT
            health = maxHealth
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_coming_to_meet_robot_activation)
        }
    }

    private fun generateHighFives(battleField: BattleField65) {
        repeat(battleField.height) { r ->
            repeat(battleField.width) { c ->
                battleField.addProjectile(HighFive(Coordinates(r, c)))
            }
        }
    }
    private fun generateLowerFive(battleField: BattleField65) {
        val coordinates = randCoordinatesWithExclusion(
            battleField.width,
            battleField.height,
            battleField.protagonist.position
        )
        currentCoordinates.add(coordinates)
        battleField.addProjectile(LowerFive(coordinates))
    }
    private fun generateLowFives(battleField: BattleField65) {
        for (coordinates in currentCoordinates) {
            repeat(battleField.height) { r ->
                battleField.addProjectile(LowFive(Coordinates(r, coordinates.col)))
            }
            repeat(battleField.width) { c ->
                battleField.addProjectile(LowFive(Coordinates(coordinates.row, c)))
            }
        }
    }

    enum class State {
        NORMAL,
        ROBOT
    }
}