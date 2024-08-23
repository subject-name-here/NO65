package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Censorship
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Joke
import com.unicorns.invisible.no65.model.elements.trigram.Fire
import com.unicorns.invisible.no65.model.elements.trigram.Thunder
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBoolean
import com.unicorns.invisible.no65.util.randInt


class BattleFieldAbundance : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_NO_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 55
    override val nameId: Int = R.string.battlefield_abundance_name

    override val centerSymbolColorId: Int = R.color.red
    override val centerSymbol: String = "\uD83C\uDCBF"

    override val hexagramSymbol: String = "䷶"
    override val outerSkinTrigram = Thunder
    override val innerHeartTrigram = Fire

    override val defaultFace: String = "\uD83D\uDE14"
    override val damageReceivedFace: String = "\uD83D\uDE22"
    override val noDamageReceivedFace: String = "\uD83D\uDE0F"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_abundance_1
                2 -> R.string.battlefield_abundance_2
                3 -> R.string.battlefield_abundance_3
                4 -> R.string.battlefield_abundance_4
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_abundance_d
        override fun getVictoryLine(): Int = R.string.battlefield_abundance_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        spawnJoke(battleField)
        spawnJoke(battleField)
        spawnCensorship(battleField)
    }

    private fun spawnJoke(battleField: BattleField65) {
        val row = randInt(battleField.height)
        if (randBoolean()) {
            battleField.addProjectile(Joke(Coordinates(row, -1), BattleFieldProjectile.Direction.RIGHT))
        } else {
            battleField.addProjectile(Joke(Coordinates(row, battleField.width), BattleFieldProjectile.Direction.LEFT))
        }
    }

    private fun spawnCensorship(battleField: BattleField65) {
        val col = randInt(battleField.width)
        if (randBoolean()) {
            battleField.addProjectile(Censorship(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
        } else {
            battleField.addProjectile(Censorship(Coordinates(battleField.height, col), BattleFieldProjectile.Direction.UP))
        }
    }
}