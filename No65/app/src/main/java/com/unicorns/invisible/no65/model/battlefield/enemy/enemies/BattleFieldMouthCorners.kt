package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Kiss
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.KissKissKiss
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.WallStreet
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.takeRand
import kotlin.math.min


class BattleFieldMouthCorners(val hasBrokeHeart: Boolean) : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_mouth_corners_name
    override val number: Int
        get() = 27

    override val defaultFace: String
        get() = "\uD83D\uDE18"
    override val damageReceivedFace: String
        get() = "\uD83D\uDE25"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDE18"

    override val centerSymbol: String
        get() = "\uD83D\uDC8C"
    override val centerSymbolColorId: Int
        get() = R.color.dark_red

    override val beatId: Int
        get() = R.raw.sfx_kiss_short

    override val hexagramSymbol: String
        get() = "䷚"
    override val outerSkinTrigram: Trigram
        get() = Mountain
    override val innerHeartTrigram: Trigram
        get() = Thunder

    override val animation: CharacterAnimation
        get() = CharacterAnimation.LITTLE_SWING

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            if (hasBrokeHeart) {
                when (moveNumber) {
                    1 -> R.string.battlefield_mouth_corners_broken_heart_1
                    2 -> R.string.battlefield_mouth_corners_broken_heart_2
                    else -> R.string.empty_line
                }
            } else
                when (moveNumber) {
                    1 -> R.string.battlefield_mouth_corners_1
                    else -> R.string.empty_line
                }
        override fun getDefeatedLine(): Int = if (hasBrokeHeart) {
            R.string.battlefield_mouth_corners_d_broken_heart
        } else {
            R.string.battlefield_mouth_corners_d
        }
        override fun getVictoryLine(): Int = R.string.battlefield_mouth_corners_v
    }

    private val currentRows = mutableListOf<Int>()
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber) {
            0 -> {
                currentRows.clear()
                spawnWalls(battleField)
                return
            }
            1 -> return
        }
        battleField.getAllObjects()
            .filterIsInstance<KissKissKiss>()
            .forEach { battleField.removeProjectile(it) }

        currentRows.forEach { row ->
            repeat(battleField.width) { col ->
                battleField.addProjectile(KissKissKiss(Coordinates(row, col)))
            }
        }

        val availableRows = (0..battleField.height / 2).map { it * 2 } - currentRows.toSet()
        currentRows.clear()
        val numOfNewRows = min(battleField.moveNumber, availableRows.size - 1)
        currentRows.addAll(availableRows.takeRand(numOfNewRows))
        currentRows.forEach { row ->
            repeat(battleField.width) { col ->
                battleField.addProjectile(Kiss(Coordinates(row, col)))
            }
        }
    }

    private fun spawnWalls(battleField: BattleField65) {
        repeat(battleField.height / 2) {
            val row = it * 2 + 1
            repeat(battleField.width) { col ->
                battleField.addProjectile(WallStreet(Coordinates(row, col)))
            }
        }
    }
}