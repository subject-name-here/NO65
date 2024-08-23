package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Lightning
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Stone
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.TeslaSphereVertical
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBooleanPercent
import com.unicorns.invisible.no65.util.randInt


class BattleFieldTreading(val alreadyFought: Boolean) : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_treading_name
    override val number: Int = 10

    override val defaultFace: String
        get() = "\uD83D\uDE25"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE16"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDE0C"

    override val centerSymbol: String
        get() = "\uD83D\uDC22"
    override val centerSymbolColorId: Int
        get() = R.color.green

    override val hexagramSymbol: String
        get() = "䷉"
    override val outerSkinTrigram: Trigram
        get() = Heaven
    override val innerHeartTrigram: Trigram
        get() = Lake

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_treading_1
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_treading_d
        override fun getVictoryLine(): Int = R.string.battlefield_treading_v
    }

    private var attackNumber = 0
    private val movesRange = (1..4)
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            repeat(battleField.width) {
                battleField.addProjectile(TeslaSphereVertical(Coordinates(0, it)))
            }
            attackNumber = if (battleField.moveNumber in movesRange) battleField.moveNumber else movesRange.random()
        }

        battleField.getAllObjects().filterIsInstance<Lightning>().forEach {
            battleField.removeProjectile(it)
        }

        val direction = BattleFieldProjectile.Direction.LEFT
        val percent = if (alreadyFought) 42 else 51
        (1 until battleField.height).forEach {
            if (randBooleanPercent(percent)) {
                battleField.addProjectile(Stone(Coordinates(it, battleField.width), direction))
            }
        }

        when (attackNumber) {
            1 -> {
                if (tickNumber % 4 == 2) {
                    val col = randInt(battleField.width)
                    battleField
                        .getAllObjects()
                        .filterIsInstance<TeslaSphereVertical>()
                        .filter { it.position.col == col }
                        .forEach { it.charging = true }
                }
            }
            2 -> {
                if (tickNumber % 3 == 2) {
                    val col = randInt(battleField.width)
                    battleField
                        .getAllObjects()
                        .filterIsInstance<TeslaSphereVertical>()
                        .filter { it.position.col == col }
                        .forEach { it.charging = true }
                }
            }
            3 -> {
                val mod = if (alreadyFought) 3 else 2
                if (tickNumber % mod == 1) {
                    val col = randInt(battleField.width)
                    battleField
                        .getAllObjects()
                        .filterIsInstance<TeslaSphereVertical>()
                        .filter { it.position.col == col }
                        .forEach { it.charging = true }
                }
            }
            4 -> {
                if (tickNumber % 3 == 1) {
                    battleField
                        .getAllObjects()
                        .filterIsInstance<TeslaSphereVertical>()
                        .filter { it.position.col == battleField.protagonist.position.col }
                        .forEach { it.charging = true }
                }
            }
            else -> {}
        }
    }
}