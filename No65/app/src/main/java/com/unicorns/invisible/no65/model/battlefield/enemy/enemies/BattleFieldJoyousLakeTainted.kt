package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.SixtyFiveHeart
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.choose
import com.unicorns.invisible.no65.util.randBoolean


class BattleFieldJoyousLakeTainted : BattleFieldEnemy() {
    override val isHealthInfinite: Boolean = true

    override val attackTimeMvs: Int = 40
    override val defenceTimeSec: Int = 3

    override val number: Int = 0
    override val nameId: Int
        get() = R.string.battlefield_joyous_lake_tainted_name

    override val goText: String = "0"

    override val centerSymbolColorId: Int = R.color.black
    override val centerSymbol: String = "\uD83D\uDC94"

    override val hexagramSymbol: String = "?"
    override val outerSkinTrigram = Lake
    override val innerHeartTrigram = Lake

    override val defaultFace: String = "\uD83D\uDC80"
    override val damageReceivedFace: String = "\uD83D\uDC80"
    override val noDamageReceivedFace: String = "\uD83D\uDC80"

    override val musicThemeId: Int = R.raw.battle_joyous_lake_tainted
    override val beatId: Int = R.raw.sfx_beepbeat

    override val legsSymbol: String = "~"
    override val animation: CharacterAnimation = CharacterAnimation.FLOAT

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = R.string.empty_line
        override fun getDefeatedLine(): Int = R.string.empty_line
        override fun getVictoryLine(): Int = R.string.empty_line
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber) {
            1 -> {
                if (tickNumber % 3 == 0) {
                    spawnLineOfBlackHearts(battleField, BattleFieldProjectile.Direction.DOWN)
                }
            }
            2 -> {
                if (tickNumber % 4 == 0) {
                    val direction = choose(BattleFieldProjectile.Direction.UP, BattleFieldProjectile.Direction.DOWN)
                    spawnLineOfBlackHearts(battleField, direction)
                }
                val row = battleField.protagonist.position.row
                spawnHeartFromSide(row, battleField)
            }
            3 -> {
                if (tickNumber % 4 == 0) {
                    val direction = choose(BattleFieldProjectile.Direction.UP, BattleFieldProjectile.Direction.DOWN)
                    spawnLineOfBlackHearts(battleField, direction)
                }
                val row = battleField.protagonist.position.row
                spawnHeartFromLeft(row, battleField)
                spawnHeartFromRight(row, battleField)
            }
            else -> {
                if (tickNumber % 7 == 0 || tickNumber % 7 == 3) {
                    val direction = choose(BattleFieldProjectile.Direction.UP, BattleFieldProjectile.Direction.DOWN)
                    spawnLineOfBlackHearts(battleField, direction)
                }
                val row = battleField.protagonist.position.row
                spawnHeartFromLeft(row, battleField)
                spawnHeartFromRight(row, battleField)
            }
        }
    }

    private fun spawnLineOfBlackHearts(battleField: BattleField65, direction: BattleFieldProjectile.Direction) {
        when (direction) {
            BattleFieldProjectile.Direction.DOWN -> {
                repeat(battleField.width) {
                    battleField.addProjectile(SixtyFiveHeart(Coordinates(-1, it), direction))
                }
            }
            BattleFieldProjectile.Direction.UP -> {
                repeat(battleField.width) {
                    battleField.addProjectile(SixtyFiveHeart(Coordinates(battleField.height, it), direction))
                }
            }
            else -> {}
        }
    }

    private fun spawnHeartFromSide(row: Int, battleField: BattleField65) {
        if (randBoolean()) {
            spawnHeartFromLeft(row, battleField)
        } else {
            spawnHeartFromRight(row, battleField)
        }
    }

    private fun spawnHeartFromLeft(row: Int, battleField: BattleField65) {
        battleField.addProjectile(
            SixtyFiveHeart(
                Coordinates(row, -1),
                BattleFieldProjectile.Direction.RIGHT
            )
        )
    }
    private fun spawnHeartFromRight(row: Int, battleField: BattleField65) {
        battleField.addProjectile(
            SixtyFiveHeart(
                Coordinates(row, battleField.width),
                BattleFieldProjectile.Direction.LEFT
            )
        )
    }
}