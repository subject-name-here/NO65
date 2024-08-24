package com.unicorns.invisible.no65.model.battlefield

import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldCharacter
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldFighter
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.random.Random


abstract class BattleField(
    val width: Int,
    val height: Int,
    open val protagonist: BattleFieldFighter,
    open val enemy: BattleFieldCharacter
) {
    protected val coordinatesToObject = HashMap<Coordinates, BattleFieldObject>()
    protected val rwMapLock: ReentrantReadWriteLock = ReentrantReadWriteLock()

    // Numeration of moves from 1 is intentional for no particular reason.
    var moveNumber = 0

    open fun onEnemyMoveStart() {
        clearRaw()
        moveNumber++
    }
    open fun onEnemyMoveEnd() {}

    val rowsOrder: MutableList<Int>
        get() {
            val seed = enemy.rowViewsShuffleSeed
            return if (seed == null) {
                (0 until height)
            } else {
                (0 until height).shuffled(Random(seed))
            }.toMutableList()
        }

    open fun setFieldPlayable() {
        val oldObjectsList = getAllObjects()
        clearRaw()
        for (battleFieldObject in oldObjectsList) {
            when (battleFieldObject) {
                is BattleFieldFighter -> addFighter(battleFieldObject)
                is BattleFieldProjectile -> {
                    val row = battleFieldObject.position.row
                    val col = battleFieldObject.position.col
                    if (row in (0 until height) && col in (0 until width)) {
                        addProjectile(battleFieldObject)
                    }
                }
            }
        }
    }

    open fun addFighter(fighter: BattleFieldFighter) {
        val occupantObject = rwMapLock.read { coordinatesToObject[fighter.position] }
        if (occupantObject is BattleFieldProjectile) {
            fighter.takeDamage(occupantObject.damage)
            removeRaw(occupantObject)
        }
        addObjectRaw(fighter)
    }

    open fun addProjectile(projectile: BattleFieldProjectile) {
        val occupantObject = rwMapLock.read { coordinatesToObject[projectile.position] }
        if (occupantObject is BattleFieldFighter) {
            occupantObject.takeDamage(projectile.damage)
        } else {
            if (
                occupantObject == null ||
                occupantObject is BattleFieldProjectile &&
                projectile.priority >= occupantObject.priority
            ) {
                addObjectRaw(projectile)
            }
        }
    }
    fun removeProjectile(projectile: BattleFieldProjectile) {
        removeRaw(projectile)
    }
    fun removeProjectileAt(position: Coordinates) {
        val projectile = getMap()[position] ?: return
        if (projectile is BattleFieldProjectile) {
            removeRaw(projectile)
        }
    }

    private fun addObjectRaw(battleFieldObject: BattleFieldObject) {
        rwMapLock.write {
            coordinatesToObject[battleFieldObject.position] = battleFieldObject
        }
    }
    private fun removeRaw(battleFieldObject: BattleFieldObject) {
        rwMapLock.write {
            if (battleFieldObject in coordinatesToObject.values) {
                coordinatesToObject.remove(battleFieldObject.position)
            }
        }
    }
    protected fun clearRaw() {
        rwMapLock.write {
            coordinatesToObject.clear()
        }
    }

    fun changeProtagonistCoordinates(coordinates: Coordinates) {
        removeRaw(protagonist)
        protagonist.position = coordinates
        addFighter(protagonist)
    }

    fun getCenterCoordinates() = Coordinates(height, width) / 2
    fun sendProtagonistToCenter() = changeProtagonistCoordinates(getCenterCoordinates())

    fun getAllObjects() = rwMapLock.read { coordinatesToObject.values.toList() }
    fun getMap(): Map<Coordinates, BattleFieldObject> = rwMapLock.read { coordinatesToObject.toMap() }
    fun clear() = clearRaw()
}