package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.NuclearExplosion
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.NuclearRocket
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randCoordinatesWithExclusion
import com.unicorns.invisible.no65.util.randInt


class BattleFieldGreatPreponderance : BattleFieldEnemy() {
    override val number: Int = 28
    override val hexagramSymbol: String = "䷛"
    override val nameId: Int
        get() = R.string.battlefield_great_preponderance_name

    override val attackTimeMvs: Int = 30

    override val centerSymbolColorId: Int = R.color.true_yellow
    override val centerSymbol: String = "\uD83E\uDD47"

    override val outerSkinTrigram = Lake
    override val innerHeartTrigram = Wind

    override val defaultFace: String = "\uD83D\uDE20"
    override val damageReceivedFace: String = "\uD83D\uDE24"
    override val noDamageReceivedFace: String = "\uD83D\uDE20"

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_great_preponderance_1
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_great_preponderance_d
        override fun getVictoryLine(): Int = R.string.battlefield_great_preponderance_v
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0 || tickNumber % 2 == 1) {
            return
        }

        battleField.getAllObjects().filterIsInstance<NuclearExplosion>().forEach {
            battleField.removeProjectile(it)
        }

        when (battleField.moveNumber) {
            1 -> {
                setUpRockets(battleField, 1, battleField.width / 2)
            }
            2, 4, 6 ->  {
                setUpRockets(battleField, 1, battleField.height / 2 - 1)
            }
            3 -> {
                setUpRockets(battleField, 2, battleField.width / 2)
            }
            else -> {
                setUpRockets(battleField, 2, randInt(battleField.width / 2 + 1))
            }
        }
    }

    private fun setUpRockets(battleField: BattleField65, numOfRockets: Int, radius: Int) {
        val rocketsCoordinates = arrayListOf<Coordinates>()
        val protagonistCoordinates = battleField.protagonist.position
        repeat(numOfRockets) {
            val randomCoordinates = randCoordinatesWithExclusion(battleField.width, battleField.height, protagonistCoordinates)
            if (randomCoordinates !in rocketsCoordinates) {
                rocketsCoordinates.add(randomCoordinates)
            }
        }

        for (rocket in rocketsCoordinates) {
            battleField.addProjectile(NuclearRocket(rocket, radius))
        }
    }
}