package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.ThreeWaySharp
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.WallStreet
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheWanderer
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldTheWanderer(state: TheWanderer.State) : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_the_wanderer_name
    override val number: Int
        get() = 56

    override val attackTimeMvs: Int = 30

    override val defaultFace: String
        get() = "\uD83D\uDE1B"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE1E"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDE1C"

    override val centerSymbol: String
        get() = "\uD83D\uDD13"
    override val centerSymbolColorId: Int
        get() = R.color.almost_black

    override val hexagramSymbol: String
        get() = "䷷"
    override val outerSkinTrigram: Trigram
        get() = Fire
    override val innerHeartTrigram: Trigram
        get() = Mountain

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (state) {
            TheWanderer.State.SEC_2 -> when (moveNumber) {
                1 -> R.string.battlefield_the_wanderer_city_1
                2 -> R.string.battlefield_the_wanderer_city_2
                else -> R.string.empty_line
            }
            TheWanderer.State.KING_OF_CLUBS -> when (moveNumber) {
                1 -> R.string.battlefield_the_wanderer_koc_1
                2 -> R.string.battlefield_the_wanderer_koc_2
                3 -> R.string.battlefield_the_wanderer_koc_3
                4 -> R.string.battlefield_the_wanderer_koc_4
                else -> R.string.empty_line
            }
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_the_wanderer_d
        override fun getVictoryLine(): Int = when (state) {
            TheWanderer.State.SEC_2 -> R.string.battlefield_the_wanderer_v_city
            TheWanderer.State.KING_OF_CLUBS -> R.string.battlefield_the_wanderer_v_koc
        }
    }

    private val rows = mutableListOf<Int>()
    private val cols = mutableListOf<Int>()
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            rows.clear()
            cols.clear()
            repeat(battleField.width * battleField.height) {
                if (it % 2 == 1) {
                    val row = it / battleField.width
                    val col = it % battleField.width
                    battleField.addProjectile(WallStreet(Coordinates(row, col)))
                }
            }
        }

        when (battleField.moveNumber) {
            1 -> {
                when {
                    tickNumber % 3 == 0 -> { cols.add(battleField.width) }
                }
            }
            2 -> {
                when {
                    tickNumber % 3 == 0 -> { rows.add(-1) }
                    tickNumber % 10 == 9 -> { cols.add(battleField.width) }
                }
            }
            3 -> {
                when {
                    tickNumber % 3 == 0 -> { rows.add(-1) }
                    tickNumber % battleField.width == battleField.width - 1 -> { cols.add(battleField.width) }
                }
            }
            else -> {
                when {
                    tickNumber % 3 == 0 -> { rows.add(-1) }
                    tickNumber % 4 == 3 -> { cols.add(battleField.width) }
                }
            }
        }

        battleField.getAllObjects().filterIsInstance<ThreeWaySharp>().forEach {
            battleField.removeProjectile(it)
        }

        rows.forEachIndexed { index, row ->
            repeat(battleField.width) { col ->
                battleField.addProjectile(ThreeWaySharp(Coordinates(row, col), BattleFieldProjectile.Direction.DOWN))
            }
            rows[index]++
        }
        rows.removeAll { it >= battleField.height }

        cols.forEachIndexed { index, col ->
            repeat(battleField.height) { row ->
                battleField.addProjectile(ThreeWaySharp(Coordinates(row, col), BattleFieldProjectile.Direction.LEFT))
            }
            cols[index]--
        }
        cols.removeAll { it < 0 }
    }
}