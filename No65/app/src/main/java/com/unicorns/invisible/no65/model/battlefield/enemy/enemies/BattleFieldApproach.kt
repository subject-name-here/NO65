package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Spider
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.Web
import com.unicorns.invisible.no65.model.elements.trigram.Earth
import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.util.Coordinates


class BattleFieldApproach : BattleFieldEnemy() {
    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_BAD_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 19
    override val hexagramSymbol: String = "䷒"
    override val nameId: Int = R.string.battlefield_approach_name

    override val centerSymbolColorId: Int = R.color.almost_black
    override val centerSymbol: String = "\uD83D\uDD78"

    override val outerSkinTrigram = Earth
    override val innerHeartTrigram = Lake

    override val defaultFace: String = "\uD83D\uDE20"
    override val damageReceivedFace: String = "\uD83D\uDE24"
    override val noDamageReceivedFace: String = "\uD83D\uDE0F"

    override val animation: CharacterAnimation = CharacterAnimation.LITTLE_SWING

    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int =
            when (moveNumber) {
                1 -> R.string.battlefield_approach_1
                2 -> R.string.battlefield_approach_2
                3 -> R.string.battlefield_approach_3
                else -> R.string.empty_line
            }
        override fun getDefeatedLine(): Int = R.string.battlefield_approach_d
        override fun getVictoryLine(): Int = R.string.battlefield_approach_v
    }


    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        when (battleField.moveNumber % 2) {
            0 -> {
                ceilingAttack(battleField, tickNumber)
            }
            1 -> {
                spidersAttack(battleField)
            }
        }
    }

    private var col = 0
    private var isAscending = true
    private fun spidersAttack(battleField: BattleField65) {
        battleField.addProjectile(Spider(Coordinates(-1, col)))
        battleField.addProjectile(Spider(Coordinates(-1, (col + battleField.width / 2) % battleField.width)))
        if (isAscending) {
            col++
        } else {
            col--
        }
        if (col == -1) {
            col = 0
            isAscending = true
        } else if (col == battleField.width) {
            col = battleField.width - 1
            isAscending = false
        }
    }

    private val spidersCols = mutableListOf<Int>()
    private val spiders = mutableListOf<Spider>()
    private fun ceilingAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber % 5 == 0) {
            spidersCols.clear()
            spiders.clear()
            battleField.removeProjectiles()
        }
        when (tickNumber % 5) {
            0, 1, 2 -> {
                val protagonistCol = battleField.protagonist.position.col
                val col = if (protagonistCol in spidersCols) {
                    ((0 until battleField.width) - spidersCols.toSet()).random()
                } else {
                    protagonistCol
                }
                val spider = Spider(Coordinates(-1, col))
                spider.changeDirection(BattleFieldProjectile.Direction.NO_MOVEMENT)
                battleField.addProjectile(spider)
                spidersCols.add(col)
                spiders.add(spider)
            }
            3 -> {
                spiders.forEach {
                    it.position.row = 1
                    battleField.addProjectile(Web(Coordinates(0, it.position.col)))
                }
            }
            4 -> {
                spiders.forEach { spider ->
                    spider.position.row = battleField.height - 1
                    (1 until battleField.height - 1).forEach {
                        battleField.addProjectile(Web(Coordinates(it, spider.position.col)))
                    }
                }
            }
        }
    }
}