package com.unicorns.invisible.no65.model.battlefield.enemy.enemies

import com.unicorns.invisible.no65.BattleManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.AttackResult
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.BattleField65
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.ConflictKunai
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.ConflictNife
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.ConflictNifeSpawner
import com.unicorns.invisible.no65.model.elements.trigram.Heaven
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Water
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.randBooleanPercent
import com.unicorns.invisible.no65.util.randInt
import com.unicorns.invisible.no65.util.takeRand


class BattleFieldConflict : BattleFieldEnemy() {
    override val number: Int = 6
    override val hexagramSymbol: String = "䷅"
    override val nameId: Int
        get() = R.string.battlefield_conflict_name

    override val centerSymbolColorId: Int = R.color.red
    override val centerSymbol: String = "\uD83D\uDC4A"

    override var attackTimeMvs: Int = super.attackTimeMvs

    override val outerSkinTrigram = Heaven
    override val innerHeartTrigram = Water

    override var defaultFace: String = "\uD83E\uDD2C"
    override val damageReceivedFace: String = "\uD83E\uDD2C"
    override val noDamageReceivedFace: String = "\uD83E\uDD2C"

    private var attacked = false
    private var attackedByWind = false
    private var soothingLineGenerated = false
    private var attackLineGenerated = false
    override val lineGenerator: BattleFieldLineGenerator = object : BattleFieldLineGenerator {
        override fun getLine(moveNumber: Int): Int {
            if (!attacked) {
                return if (soothingLineGenerated) {
                    R.string.empty_line
                } else {
                    soothingLineGenerated = true
                    R.string.battlefield_conflict_soothing
                }
            }

            return if (!attackLineGenerated && !attackedByWind) {
                attackLineGenerated = true
                R.string.battlefield_conflict_attacked
            } else {
                val isFeelingFunky = randBooleanPercent(1)
                if (isFeelingFunky && doesStrongerAttack) {
                    R.string.battlefield_conflict_funky
                } else {
                    R.string.empty_line
                }
            }
        }
        override fun getDefeatedLine(): Int = R.string.battlefield_conflict_d
        override fun getVictoryLine(): Int = R.string.battlefield_conflict_v
    }

    private var firstAttackMove = -1
    private var doesStrongerAttack = false
    override fun onMoveStart(battleField: BattleField) {
        attackTimeMvs = when {
            !attacked -> {
                6
            }
            firstAttackMove == -1 -> {
                firstAttackMove = battleField.moveNumber
                when {
                    attackedByWind -> {
                        doesStrongerAttack = true
                        60
                    }
                    health > maxHealth / 2 -> {
                        super.attackTimeMvs
                    }
                    else -> {
                        30
                    }
                }
            }
            else -> {
                val moveXor = (battleField.moveNumber - firstAttackMove) % 2
                if (moveXor == 0) {
                    doesStrongerAttack = true
                    52
                } else {
                    doesStrongerAttack = false
                    39
                }
            }
        }
    }

    override fun onTick(battleField: BattleField65, tickNumber: Int) {
        if (!attacked) {
            return
        }

        if (health > maxHealth / 2 && firstAttackMove == battleField.moveNumber && !attackedByWind) {
            moveKunai(battleField)
            battleField.setFieldPlayable()
            when (tickNumber % 12) {
                0, 3, 5, 8, 11 -> launchVerticalWave(battleField)
            }
        } else {
            complexAttack(battleField, tickNumber)
        }
    }
    private fun launchVerticalWave(battleField: BattleField65) {
        repeat(battleField.height) {
            battleField.addProjectile(ConflictKunai(Coordinates(it, 0)))
        }
    }
    private var ticksToFireNifeSpawner = listOf<Int>()
    private fun complexAttack(battleField: BattleField65, tickNumber: Int) {
        if (tickNumber == 0) {
            val nifesToTake = if (doesStrongerAttack) {
                attackTimeMvs * 3 / 4
            } else {
                attackTimeMvs / 2
            }
            ticksToFireNifeSpawner = (0 until attackTimeMvs).takeRand(nifesToTake)
        }

        val objects = battleField.getAllObjects()
        objects.filterIsInstance<ConflictNife>().forEach {
            battleField.removeProjectile(it)
        }
        moveKunai(battleField)
        battleField.setFieldPlayable()
        objects.filterIsInstance<ConflictNifeSpawner>().forEach {
            battleField.removeProjectile(it)
            repeat(battleField.height) { row ->
                battleField.addProjectile(ConflictNife(Coordinates(row, it.position.col)))
            }
        }
        battleField.setFieldPlayable()

        if (tickNumber in ticksToFireNifeSpawner) {
            val pCol = battleField.protagonist.position.col
            val col = if (randBooleanPercent(75)) {
                pCol
            } else {
                randInt(battleField.width)
            }
            battleField.addProjectile(ConflictNifeSpawner(Coordinates(0, col)))
        }

        (1 until battleField.height).takeRand(4).forEach {
            battleField.addProjectile(ConflictKunai(Coordinates(it, 0)))
        }
    }
    private fun moveKunai(battleField: BattleField65) {
        battleField.getAllObjects().filterIsInstance<ConflictKunai>().forEach {
            it.position += it.direction.getDelta()
        }
    }

    override suspend fun afterElements(
        result: AttackResult,
        battleField: BattleField65,
        manager: BattleManager,
        element: Trigram?
    ) {
        if (result.type.isAttack()) {
            attacked = true
            return
        }
        if (!attacked && element == Wind) {
            attacked = true
            attackedByWind = true
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_conflict_attacked_wind_1)
            manager.drawer.showTextInEnemySpeechBubbleSuspend(R.string.battlefield_conflict_attacked_wind_2)
        }
    }
}