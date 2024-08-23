package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.LetterProjectile
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt
import kotlin.math.min


class BattleFieldTheWell : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_the_well_name
    override val number: Int = 48

    override val defaultFace: String
        get() = "\uD83D\uDC37"
    override val damageReceivedFace: String
        get() = "\uD83D\uDC37"
    override val noDamageReceivedFace: String
        get() = "\uD83D\uDC37"

    override val centerSymbol: String
        get() = "\uD83D\uDEC8"
    override val centerSymbolColorId: Int
        get() = R.color.blue

    override val hexagramSymbol: String
        get() = "䷯"
    override val outerSkinTrigram: Trigram
        get() = Water
    override val innerHeartTrigram: Trigram
        get() = Wind

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = R.string.battlefield_the_well_0
        override fun getDefeatedLine(): Int = R.string.battlefield_the_well_d
        override fun getVictoryLine(): Int = R.string.battlefield_the_well_v
    }

    private var currentMRows = mutableListOf<Int>()
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            currentMRows.clear()
        }

        battleField.getAllObjects()
            .filterIsInstance<LetterProjectile>()
            .forEach { battleField.removeProjectile(it) }

        currentMRows.forEach { currentMRow ->
            (0 until battleField.width).forEach { col ->
                battleField.addProjectile(
                    LetterProjectile(
                        Coordinates(currentMRow, col),
                        BattleFieldProjectile.Direction.NO_MOVEMENT,
                        "O",
                        color = R.color.green
                    )
                )
            }
        }
        currentMRows.clear()
        repeat(min(battleField.moveNumber + 1, battleField.height / 3)) { mNumber ->
            val currentMRow = when {
                tickNumber == 0 -> ((0 until battleField.height) - battleField.protagonist.position.row).random()
                mNumber == 0 && battleField.moveNumber != 1 -> battleField.protagonist.position.row
                else -> randInt(battleField.height)
            }

            val projectile = LetterProjectile(
                Coordinates(currentMRow, battleField.width),
                BattleFieldProjectile.Direction.LEFT,
                "M",
                color = R.color.dark_red
            ).apply {
                damage = BattleFieldProjectile.DEFAULT_PROJECTILE_DAMAGE * 4
                priority = 2
            }

            battleField.addProjectile(projectile)
            currentMRows.add(currentMRow)
        }
    }
}