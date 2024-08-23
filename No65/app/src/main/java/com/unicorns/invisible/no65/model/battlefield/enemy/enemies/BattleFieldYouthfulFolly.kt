package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.WaveRider
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean


class BattleFieldYouthfulFolly : BattleFieldEnemy() {
    override val number: Int = 4
    override val nameId: Int
        get() = R.string.battlefield_youthful_folly_name

    override val centerSymbolColorId: Int = R.color.blue
    override val centerSymbol: String = "\uD83C\uDFC4"

    override val hexagramSymbol: String = "䷃"
    override val outerSkinTrigram = Mountain
    override val innerHeartTrigram = Water

    override var defaultFace: String = "\uD83D\uDE13"
    override val damageReceivedFace: String = "\uD83D\uDE16"
    override val noDamageReceivedFace: String = "\uD83D\uDE05"

    override fun onMoveStart(battleField: BattleField) {
        when (battleField.moveNumber) {
            2 -> {
                defaultFace = "\uD83D\uDE22"
            }
            4 -> {
                defaultFace = "\uD83D\uDE2D"
            }
            5 -> {
                defaultFace = "\uD83D\uDE00"
            }
        }
    }
    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_youthful_folly_1
                2 -> R.string.battlefield_youthful_folly_2
                3 -> R.string.battlefield_youthful_folly_3
                4 -> R.string.battlefield_youthful_folly_4
                5 -> R.string.battlefield_youthful_folly_5
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_youthful_folly_dv
        override fun getVictoryLine(): Int = R.string.battlefield_youthful_folly_dv
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        val direction = if (randBoolean()) {
            BattleFieldProjectile.Direction.LEFT
        } else {
            BattleFieldProjectile.Direction.RIGHT
        }
        if ((tickNumber % battleField.width) < battleField.width - 3) {
            generateSimpleWaves(battleField, direction)
        }
    }

    private fun generateSimpleWaves(battleField: BattleField65, direction: BattleFieldProjectile.Direction) {
        if (
            direction != BattleFieldProjectile.Direction.LEFT &&
            direction != BattleFieldProjectile.Direction.RIGHT
        ) {
            return
        }

        val col = when (direction) {
            BattleFieldProjectile.Direction.LEFT -> battleField.width
            BattleFieldProjectile.Direction.RIGHT -> -1
            else -> battleField.width + projectilePassesCellsPerMove * 2 // far out of bounds
        }
        repeat(battleField.height) {
            battleField.addProjectile(WaveRider(Coordinates(it, col), direction))
        }
    }
}