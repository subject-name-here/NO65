package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.GreatPowerJSpawner
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.LetterProjectile
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean


class BattleFieldGreatPower : BattleFieldEnemy() {
    override val number: Int = 34
    override val hexagramSymbol: String = "䷡"
    override val nameId: Int
        get() = R.string.battlefield_great_power_name

    override val attackTimeMvs: Int
        get() = 34

    override val centerSymbolColorId: Int = R.color.blue
    override val centerSymbol: String = "\uD83D\uDED4"

    override val outerSkinTrigram = Thunder
    override val innerHeartTrigram = Heaven

    override val defaultFace: String = "\uD83E\uDD21"
    override val damageReceivedFace: String = "\uD83D\uDE29"
    override val noDamageReceivedFace: String = "\uD83E\uDD23"

    override val handsSymbol: String = "Ӟ"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_great_power_1
                2 -> R.string.battlefield_great_power_2
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_great_power_d
        override fun getVictoryLine(): Int = R.string.battlefield_great_power_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber) {
            1 -> {
                if (tickNumber % battleField.width == 0) {
                    spawnWave(battleField, BattleFieldProjectile.Direction.RIGHT, -1)
                }
                if (tickNumber % 3 == 2) {
                    spawnRandomJSpawnerFromSide(battleField)
                }
            }
            3 -> {
                if (tickNumber % 4 == 0) {
                    spawnWave(battleField, BattleFieldProjectile.Direction.LEFT, battleField.width)
                }
                if (tickNumber % 2 == 1) {
                    spawnRandomJSpawnerFromSide(battleField)
                }
            }
            else -> {
                if (tickNumber % 3 == 0) {
                    spawnWave(battleField, BattleFieldProjectile.Direction.RIGHT, -1)
                }
                spawnRandomJSpawnerFromSide(battleField)
            }
        }
    }

    private fun spawnWave(battleField: BattleField65, direction: BattleFieldProjectile.Direction, col: Int) {
        repeat(battleField.height) {
            battleField.addProjectile(
                LetterProjectile(
                Coordinates(it, col),
                direction,
                "J",
                R.color.blue
            ).apply { damage = 10 })
        }
    }

    private fun spawnRandomJSpawnerFromSide(battleField: BattleField65) {
        val (direction, col) = if (randBoolean()) {
            BattleFieldProjectile.Direction.LEFT to battleField.width
        } else {
            BattleFieldProjectile.Direction.RIGHT to -1
        }
        val row = battleField.protagonist.position.row
        battleField.addProjectile(GreatPowerJSpawner(Coordinates(row, col), direction))
    }
}