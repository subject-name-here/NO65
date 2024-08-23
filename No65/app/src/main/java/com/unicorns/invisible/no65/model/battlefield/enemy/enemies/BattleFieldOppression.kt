package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Bat
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BatFlipped
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.choose
import com.unicorns.invisible.no65.util.randInt


class BattleFieldOppression(private val speechEventTriggered: Boolean) : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_BAD_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 30

    override val number: Int = 47
    override val nameId: Int
        get() = R.string.battlefield_oppression_name

    override val centerSymbolColorId: Int = R.color.dark_grey
    override val centerSymbol: String = "\uD83D\uDD17"

    override val hexagramSymbol: String = "䷮"
    override val outerSkinTrigram = Lake
    override val innerHeartTrigram = Water

    override val defaultFace: String = "\uD83E\uDDDB"
    override val damageReceivedFace: String = "\uD83D\uDE23"
    override val noDamageReceivedFace: String = "\uD83E\uDDDB"

    override val animation: CharacterAnimation
        get() = CharacterAnimation.LITTLE_SWING

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = if (!speechEventTriggered) {
            if (moveNumber == 1) {
                R.string.battlefield_oppression_no_speech_1
            } else {
                R.string.empty_line
            }
        } else when (moveNumber) {
            1 -> R.string.battlefield_oppression_1
            2 -> R.string.battlefield_oppression_2
            3 -> R.string.battlefield_oppression_3
            else -> R.string.empty_line
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_oppression_d
        override fun getVictoryLine(): Int = R.string.battlefield_oppression_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 4 == 0) {
            spawnLine(battleField)
        } else {
            val (col, dir) = when (battleField.moveNumber) {
                1 -> Pair(battleField.width / 2, BattleFieldProjectile.Direction.LEFT)
                2, 5 -> {
                    val bats = battleField
                        .getAllObjects()
                        .filterIsInstance<Bat>()
                        .filter { it.position.row == 0 }
                    if (bats.isEmpty()) {
                        getRandomColDir(battleField)
                    } else {
                        Pair(bats[0].position.col, bats[0].direction)
                    }
                }
                else -> getRandomColDir(battleField)
            }
            battleField.addProjectile(Bat(Coordinates(-1, col), dir))
        }
    }

    private fun spawnLine(battleField: BattleField65) {
        repeat(battleField.width) { col ->
            battleField.addProjectile(BatFlipped(Coordinates(-1, col)))
        }
    }

    private fun getRandomColDir(battleField: BattleField65): Pair<Int, BattleFieldProjectile.Direction> {
        return Pair(
            randInt(1, battleField.width - 1),
            choose(BattleFieldProjectile.Direction.LEFT, BattleFieldProjectile.Direction.RIGHT)
        )
    }

    private var waterballsLineSaid = false
    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        if (element == Water) {
            if (!waterballsLineSaid) {
                waterballsLineSaid = true
                manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_oppression_waterballs)
            }
        }
    }
}