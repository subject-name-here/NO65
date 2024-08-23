package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Paw
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Mountain
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randInt
import com.unicorns.invisible.no65.util.takeRand


class BattleFieldGreatTaming : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * BOSS_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val attackTimeMvs: Int = 26

    override val number: Int = 26
    override val hexagramSymbol: String = "䷙"
    override val nameId: Int
        get() = R.string.battlefield_great_taming_name

    override val centerSymbolColorId: Int = R.color.brown
    override val centerSymbol: String = "\uD83D\uDC3E"

    override val outerSkinTrigram = Mountain
    override val innerHeartTrigram = Heaven

    override var defaultFace: String = "\uD83E\uDD81"
    override val damageReceivedFace: String = "\uD83D\uDC79"
    override val noDamageReceivedFace: String = "\uD83E\uDD81"

    override val animation: CharacterAnimation
        get() = CharacterAnimation.LITTLE_SWING

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int = when (moveNumber) {
            1 -> R.string.battlefield_great_taming_1
            2 -> R.string.battlefield_great_taming_2
            3 -> R.string.battlefield_great_taming_3
            4 -> R.string.battlefield_great_taming_4
            else -> R.string.battlefield_great_taming_0
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_great_taming_d
        override fun getVictoryLine(): Int = R.string.battlefield_great_taming_v
    }

    private var cols = mutableListOf<Int>()
    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 2 == 0) {
            cols.clear()
            generateOnePaw(battleField)
        } else {
            generateLinePaws(battleField)
        }
    }

    private fun generateOnePaw(battleField: BattleField65) {
        battleField.removeProjectiles()
        cols.add(battleField.protagonist.position.col)
        when {
            battleField.moveNumber == 2 -> cols.add(randInt(battleField.width))
            battleField.moveNumber == 3 -> cols.addAll((0 until battleField.width).takeRand(2))
            battleField.moveNumber > 3 -> cols.addAll((0 until battleField.width).takeRand(3))
        }
        cols = cols.distinct().toMutableList()
        for (col in cols) {
            battleField.addProjectile(Paw(Coordinates(-1, col), BattleFieldProjectile.Direction.DOWN))
        }
    }

    private fun generateLinePaws(battleField: BattleField65) {
        for (col in cols) {
            repeat(battleField.height) {
                battleField.addProjectile(Paw(Coordinates(it, col), BattleFieldProjectile.Direction.NO_MOVEMENT))
            }
        }
    }
}