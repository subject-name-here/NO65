package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.MagnetBot
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldObstruction : BattleFieldEnemy() {
    override val number: Int = 39
    override val nameId: Int
        get() = R.string.battlefield_obstruction_name

    override val centerSymbolColorId: Int = R.color.red
    private var centerSymbolShows = true
    override val centerSymbol: String
        get() {
            return if (centerSymbolShows) {
                "⏻"
            } else {
                ""
            }
        }

    override val hexagramSymbol: String = "䷦"
    override val outerSkinTrigram = Water
    override val innerHeartTrigram = Mountain

    override val defaultFace: String = "\uD83E\uDD16"
    override val damageReceivedFace: String = "\uD83D\uDDAD"
    override val noDamageReceivedFace: String = "\uD83E\uDD16"

    override val attackTimeMvs: Int = 30

    override val animation: CharacterAnimation
        get() = CharacterAnimation.LITTLE_SWING

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_obstruction_1
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_obstruction_d
        override fun getVictoryLine(): Int = R.string.battlefield_obstruction_v
    }

    override suspend fun onBattleBegins(manager: BattleManager) {
        manager.controller.addListenerOnEnemyCentreCell {
            health = 0
        }
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        centerSymbolShows = !centerSymbolShows
        when (tickNumber % 4) {
            0 -> {
                spawnLine(battleField)
            }
        }
    }
    private fun spawnLine(battleField: BattleField65) {
        repeat(battleField.width) {
            battleField.addProjectile(MagnetBot(Coordinates(-1, it)))
        }
    }

    private var damagedLineSaid = false
    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        if (result.damage > 0) {
            if (!damagedLineSaid) {
                damagedLineSaid = true
                if (health <= 0) {
                    manager.drawer.hideEnemyHealthBarWithAnimation()
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(
                        R.string.battlefield_obstruction_on_death_1,
                        withDelay = false
                    )
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_obstruction_on_death_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_obstruction_on_death_3)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_obstruction_on_death_4)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_obstruction_on_death_5)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_obstruction_on_death_6)
                } else {
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(
                        R.string.battlefield_obstruction_on_damage_1,
                        withDelay = false
                    )
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_obstruction_on_damage_2)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_obstruction_on_damage_3)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_obstruction_on_damage_4)
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_obstruction_on_damage_5)
                    manager.drawer.hideEnemyHealthBarWithAnimation().join()
                    manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_obstruction_on_damage_6)
                }
            }
            health = maxHealth
        }
    }
}