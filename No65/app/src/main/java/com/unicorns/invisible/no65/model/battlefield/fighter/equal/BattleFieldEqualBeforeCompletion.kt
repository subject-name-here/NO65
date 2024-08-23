package com.unicorns.invisible.no65.model.battlefield.fighter.equal

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldLineGenerator
import com.unicorns.invisible.no65.model.battlefield.enemy.CharacterAnimation
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacterEqual
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.AbysmalWaterDropAuto
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BCSpaceDistorter
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BCSpaceDistorterSpawner
import com.unicorns.invisible.no65.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


class BattleFieldEqualBeforeCompletion : BattleFieldCharacterEqual() {
    override fun getString(): String = "ӹ"
    override fun getStringColor(): Int = R.color.black

    override val maxHealth: Int = BASE_DAMAGE_TO_ENEMY * ENEMY_MAYBE_HEALTH_MULTIPLIER
    override var health: Int = maxHealth

    override val number: Int = 64
    override val nameId: Int = R.string.battlefield_before_completion_name

    override val defaultFace: String = "\uD83D\uDC76"
    override val damageReceivedFace: String = "\uD83D\uDE2D"
    override val noDamageReceivedFace: String = "\uD83D\uDC76"

    override val centerSymbolColorId: Int = R.color.white
    override val centerSymbol: String = "\uD83D\uDD1C"

    override val hexagramSymbol: String = "䷿"

    override val animation: CharacterAnimation
        get() = CharacterAnimation.LITTLE_SWING

    override val attackTimeMvs: Int
        get() = 32

    override fun getLineGenerator(enemy: BattleFieldCharacterEqual): BattleFieldLineGenerator {
        return when (enemy) {
            is BattleFieldEqualAbysmalWater -> abysmalWaterLineGenerator
            else -> BattleFieldLineGenerator.EMPTY
        }
    }

    private val abysmalWaterLineGenerator = BattleFieldLineGenerator.EMPTY

    override fun onTick(tickNumber: Int, battleField: BattleField) {
        if (tickNumber % 2 == 1) {
            val allCols = 0 until battleField.width
            val occupiedColsTop = getOccupiedColsInRow(battleField, 0)
            val occupiedColsBottom = getOccupiedColsInRow(battleField, battleField.height - 1)
            val occupiedCols = occupiedColsTop.intersect(occupiedColsBottom.toSet())
            val freeCols = allCols - occupiedCols.toSet()
            val spawnCols = when (freeCols.size) {
                0 -> emptyList()
                1 -> freeCols
                else -> freeCols.takeRand(choose(1, 2))
            }
            spawnCols.forEach { col ->
                battleField.addProjectile(BCSpaceDistorterSpawner(Coordinates(0, col)))
                battleField.addProjectile(BCSpaceDistorterSpawner(Coordinates(battleField.height - 1, col)))
            }
        }
    }
    private fun getOccupiedColsInRow(battleField: BattleField, row: Int): List<Int> {
        return battleField.getAllObjects()
            .filter { it.position.row == row }
            .map { it.position.col }
            .distinct()
    }

    override fun onTapAttack(coordinates: Coordinates, battleField: BattleField) {
        val occupant = battleField.getMap()[coordinates]
        if (occupant is BCSpaceDistorter) {
            occupant.increaseCharge()
            return
        }

        val rowCondition = coordinates.row == 0 || coordinates.row == battleField.height - 1
        if (battleField.enemy.position != coordinates && rowCondition) {
            battleField.addProjectile(BCSpaceDistorter(coordinates))
        }
    }

    private var defensiveJob: Job? = null
    override fun defend(battleField: BattleField) {
        defensiveJob = when (battleField.enemy) {
            is BattleFieldEqualAbysmalWater -> abysmalWaterDefending(battleField)
            else -> Job()
        }
    }
    private fun abysmalWaterDefending(battleField: BattleField) = launchCoroutine {
        while (isActive) {
            awaitDropDown(battleField, this)
            while (isActive && isDropDownOnField(battleField)) {
                moveToFreeCell(battleField)
                while (isActive && !hasDropDownFired(battleField)) {
                    delay(150L)
                }
                moveToFreeCell(battleField)
                delay(tickTime * 2 / 3)
                moveToFreeCell(battleField)
            }
        }
    }
    private suspend fun awaitDropDown(battleField: BattleField, scope: CoroutineScope) {
        while (scope.isActive && !isDropDownOnField(battleField)) {
            delay(50L)
        }
    }
    private fun isDropDownOnField(battleField: BattleField): Boolean {
        return battleField.getAllObjects()
            .filterIsInstance<AbysmalWaterDropAuto>()
            .any { it.direction == BattleFieldProjectile.Direction.DOWN }
    }
    private fun hasDropDownFired(battleField: BattleField): Boolean {
        val numOfObjects = battleField.getAllObjects().size
        val sidesDirectionCounter = battleField.getAllObjects()
            .filterIsInstance<AbysmalWaterDropAuto>()
            .count { it.direction in BattleFieldEqualAbysmalWater.SIDES_LIST }
        return numOfObjects >= battleField.height * 3 - 1 && sidesDirectionCounter > 1
    }

    override fun stopDefending() {
        defensiveJob?.cancel()
        defensiveJob = null
    }

    private fun moveToFreeCell(battleField: BattleField) {
        val projectiles = battleField.getAllObjects().filterIsInstance<BattleFieldProjectile>()
        val maxDist = battleField.height
        val cellsUnderFire = projectiles.flatMap { proj ->
            (0 until maxDist)
                .map {
                    dist -> proj.position + proj.direction.getDelta() * dist
                }
                .filter {
                    it.row in (0 until battleField.height) && it.col in (0 until battleField.width)
                }
        }.distinct()
        val allCoordinates = mutableListOf<Coordinates>()
        (0 until battleField.width).forEach { col ->
            (1 until battleField.height).forEach { row ->
                allCoordinates.add(Coordinates(row, col))
            }
        }
        val freeCells = allCoordinates - cellsUnderFire.toSet()
        if (freeCells.isNotEmpty()) {
            val currentDefender = if (battleField.areAttacksSwapped()) battleField.enemy else battleField.protagonist
            if (currentDefender.position !in freeCells || randBooleanPercent(15)) {
                battleField.changeEnemyCoordinates(freeCells.random())
            }
        }
    }
}