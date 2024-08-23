package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Pentagram
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean
import com.unicorns.invisible.no65.util.randInt


class BattleFieldOpposition(private val battleState: State) : BattleFieldEnemy() {
    override val maxHealth: Int = when (battleState) {
        State.JAIL_MODESTY_DEAD -> BOSS_HEALTH_MULTIPLIER * BASE_DAMAGE_TO_ENEMY
        State.JAIL_MODESTY_ALIVE -> BOSS_HEALTH_MULTIPLIER * BASE_DAMAGE_TO_ENEMY
        State.STR_50 -> ENEMY_MAYBE_HEALTH_MULTIPLIER * BASE_DAMAGE_TO_ENEMY
    }
    override var health: Int = when (battleState) {
        State.JAIL_MODESTY_DEAD -> maxHealth
        State.JAIL_MODESTY_ALIVE -> maxHealth / 6
        State.STR_50 -> maxHealth
    }

    override val number: Int = 38
    override val hexagramSymbol: String = "䷥"
    override val nameId: Int
        get() = R.string.battlefield_opposition_name

    override val centerSymbolColorId: Int = R.color.dark_red
    override val centerSymbol: String = "\uD83D\uDC54"

    override val outerSkinTrigram = Fire
    override val innerHeartTrigram = Lake

    override val defaultFace: String = "\uD83D\uDE08"
    override val damageReceivedFace: String = "\uD83D\uDC7F"
    override val noDamageReceivedFace: String = "\uD83D\uDE08"

    override val legsSymbol: String
        get() = "ӯ"

    override val rowViewsShuffleSeed = 666

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int {
            return when (battleState) {
                State.JAIL_MODESTY_DEAD -> when (moveNumber) {
                    1 -> R.string.battlefield_opposition_strong_1
                    2 -> R.string.battlefield_opposition_strong_2
                    3 -> R.string.battlefield_opposition_strong_3
                    4 -> R.string.battlefield_opposition_strong_4
                    else -> R.string.empty_line
                }
                State.JAIL_MODESTY_ALIVE -> when (moveNumber) {
                    1 -> R.string.battlefield_opposition_weak_1
                    2 -> R.string.battlefield_opposition_weak_2
                    else -> R.string.empty_line
                }
                State.STR_50 -> when (moveNumber) {
                    1 -> R.string.battlefield_opposition_1
                    2 -> R.string.battlefield_opposition_2
                    3 -> R.string.battlefield_opposition_3
                    4 -> R.string.battlefield_opposition_4
                    else -> R.string.empty_line
                }
            }
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_opposition_d
        override fun getVictoryLine(): Int = R.string.battlefield_opposition_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber) {
            1 -> {
                if (tickNumber % 5 == 0) {
                    spawnRowOfPentagrams(battleField)
                }
            }
            3, 5, 7 -> {
                if (tickNumber % 4 == 0) {
                    spawnRowOfPentagrams(battleField)
                }
            }
            else -> {
                spawnRandomPentagram(battleField)
            }
        }
    }

    private fun spawnRowOfPentagrams(battleField: BattleField65) {
        repeat(battleField.width) {
            battleField.addProjectile(Pentagram(Coordinates(-1, it), BattleFieldProjectile.Direction.DOWN))
        }
    }

    private fun spawnRandomPentagram(battleField: BattleField65) {
        if (randBoolean()) {
            battleField.addProjectile(
                Pentagram(
                    Coordinates(
                        -1,
                        randInt(battleField.width)
                    ),
                    BattleFieldProjectile.Direction.DOWN
                )
            )
        } else {
            battleField.addProjectile(
                Pentagram(
                    Coordinates(
                        randInt(battleField.height),
                        battleField.width
                    ),
                    BattleFieldProjectile.Direction.LEFT
                )
            )
        }
    }

    enum class State {
        JAIL_MODESTY_DEAD,
        JAIL_MODESTY_ALIVE,
        STR_50,
    }
}