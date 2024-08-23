package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Viking
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean


class BattleFieldDevelopment : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_development_name
    override val number: Int
        get() = 53

    override val defaultFace: String
        get() = "\uD83D\uDE42"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE27"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDE03"

    override val centerSymbol: String
        get() = "\uD83D\uDCD6"
    override val centerSymbolColorId: Int
        get() = R.color.blue

    override val hexagramSymbol: String
        get() = "䷴"
    override val outerSkinTrigram: Trigram
        get() = Wind
    override val innerHeartTrigram: Trigram
        get() = Mountain

    override val lineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_development_1
            else -> R.string.empty_line
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_development_d
        override fun getVictoryLine(): Int = R.string.battlefield_development_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        val currentWaves = battleField
            .getAllObjects()
            .filterIsInstance<Viking>()
            .filter { it.position.row == 0 }
            .map { it.position.col }

        if (currentWaves.size < 2) {
            spawnVikings(battleField)
        }

        currentWaves.forEach { col ->
            changeDirectionsRandom(battleField, col)
        }
    }

    private fun spawnVikings(battleField: BattleField65) {
        repeat(battleField.height) { row ->
            battleField.addProjectile(Viking(Coordinates(row, battleField.width)))
        }
    }

    private fun changeDirectionsRandom(battleField: BattleField65, col: Int) {
        if (randBoolean()) {
            changeDirection(battleField, col, BattleFieldProjectile.Direction.LEFT)
        } else {
            changeDirection(battleField, col, BattleFieldProjectile.Direction.NO_MOVEMENT)
        }
    }

    private fun changeDirection(battleField: BattleField65, col: Int, direction: BattleFieldProjectile.Direction) {
        battleField
            .getAllObjects()
            .filterIsInstance<Viking>()
            .filter { it.position.col == col }
            .forEach { it.changeDirection(direction) }
    }
}