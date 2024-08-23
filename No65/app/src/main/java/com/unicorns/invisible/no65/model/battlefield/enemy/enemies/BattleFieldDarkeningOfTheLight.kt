package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Bar
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.CrabPerson
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.choose
import com.unicorns.invisible.no65.util.randCoordinatesWithExclusion
import kotlin.math.ceil


class BattleFieldDarkeningOfTheLight : BattleFieldEnemy() {
    override val nameId: Int
        get() = R.string.battlefield_darkening_of_the_light_name
    override val number: Int
        get() = 36

    override val attackTimeMvs: Int
        get() = 27

    override val defaultFace: String
        get() = "\uD83E\uDD20"
    override val damageReceivedFace: String
        get() = "\uD83E\uDD20"
    override val noDamageReceivedFace: String
        get() = "\uD83E\uDD20"

    override val centerSymbol: String
        get() = "\uD83E\uDD46"
    override val centerSymbolColorId: Int
        get() = R.color.brown

    override val hexagramSymbol: String
        get() = "䷣"
    override val outerSkinTrigram: Trigram
        get() = Earth
    override val innerHeartTrigram: Trigram
        get() = Fire

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_darkening_of_the_light_1
            else -> choose(R.string.battlefield_darkening_of_the_light_0_1, R.string.battlefield_darkening_of_the_light_0_2)
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_darkening_of_the_light_d
        override fun getVictoryLine(): Int = R.string.battlefield_darkening_of_the_light_v
    }

    private val centers = mutableSetOf<Coordinates>()
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (tickNumber % 3) {
            0 -> {
                battleField.removeProjectiles()
                centers.clear()
            }
            1 -> {
                val numberOfBars = ceil(battleField.width / 3f).toInt() * ceil(battleField.height / 3f).toInt() - 1
                repeat(numberOfBars) {
                    val centerCoordinates = randCoordinatesWithExclusion(
                        battleField.width,
                        battleField.height,
                        battleField.protagonist.position
                    )
                    centers.add(centerCoordinates)
                }
                spawnBars(battleField)
            }
            2 -> {
                battleField.removeProjectiles()
                spawnCrabPersons(battleField)
            }
        }
    }

    private fun spawnBars(battleField: BattleField65) {
        for (center in centers) {
            battleField.addProjectile(Bar(center))
        }
    }

    private fun spawnCrabPersons(battleField: BattleField65) {
        for (center in centers) {
            (-1..1).forEach { dx ->
                (-1..1).forEach { dy ->
                    battleField.addProjectile(CrabPerson(center + Coordinates(dx, dy)))
                }
            }
        }
    }
}