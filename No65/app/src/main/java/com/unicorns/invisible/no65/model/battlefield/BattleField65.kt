package com.unicorns.invisible.no65.model.battlefield

import com.unicorns.invisible.no65.model.battlefield.enemy.BattleFieldEnemy
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.model.battlefield.projectile.projectiles.BattleFieldJournalEntryProjectile
import com.unicorns.invisible.no65.util.Coordinates
import kotlin.concurrent.write


class BattleField65(
    width: Int,
    height: Int,
    override val protagonist: BattleFieldProtagonist,
    override val enemy: BattleFieldEnemy
) : BattleField(width, height, protagonist, enemy) {
    private var coordinatesToObjectFrozenCopy = HashMap<Coordinates, BattleFieldObject>()
    private val coordinatesToObjectJournal = ArrayList<Map<Coordinates, BattleFieldJournalEntryProjectile>>()
    private var enemyHealthJournal: Int = enemy.health
    private var protagonistJournal = BattleFieldProtagonist.JournalEntry(protagonist)
    var takeFromJournal = false
    private var journalCounter = 0

    fun preTickField() {
        if (takeFromJournal && coordinatesToObjectJournal.size <= journalCounter) {
            removeProjectiles()
            coordinatesToObject.putAll(coordinatesToObjectFrozenCopy)
            takeFromJournal = false
        }
    }

    override fun setFieldPlayable() {
        if (takeFromJournal) {
            copyFromRaw(coordinatesToObjectJournal[journalCounter])
            addFighter(protagonist)

            journalCounter++
        } else {
            super.setFieldPlayable()
        }
    }

    override fun addProjectile(projectile: BattleFieldProjectile) {
        if (takeFromJournal) {
            return
        }
        super.addProjectile(projectile)
    }

    private fun copyFromRaw(newMap: Map<Coordinates, BattleFieldJournalEntryProjectile>) {
        rwMapLock.write {
            coordinatesToObject.clear()
            coordinatesToObject.putAll(newMap)
        }
    }

    override fun onEnemyMoveStart() {
        if (!takeFromJournal) {
            super.onEnemyMoveStart()
            coordinatesToObjectJournal.clear()
            enemyHealthJournal = enemy.health
            protagonistJournal = BattleFieldProtagonist.JournalEntry(protagonist)
        }
    }
    override fun onEnemyMoveEnd() {
        takeFromJournal = false
        journalCounter = 0
    }

    fun saveField() {
        coordinatesToObjectJournal.add(
            getAllObjects()
                .filterIsInstance<BattleFieldProjectile>()
                .map { BattleFieldJournalEntryProjectile(it) }
                .associateBy { it.position }
        )
    }

    fun timeback() {
        restoreHp()
        takeFromJournal = true
        enemy.onMoveRepeat(this)
    }
    fun ankhRestart() {
        restoreHp()
        takeFromJournal = true
        coordinatesToObjectFrozenCopy = HashMap(coordinatesToObject)
        enemy.onMoveRepeat(this)
    }
    fun redoMove() {
        restoreHp()
        moveNumber--
    }
    private fun restoreHp() {
        enemy.health = enemyHealthJournal
        protagonist.restoreFromJournal(protagonistJournal)
    }


    fun restartCreativeHeaven() {
        moveNumber = 0

        takeFromJournal = false
        journalCounter = 0
        coordinatesToObjectJournal.clear()

        enemy.health = enemy.maxHealth
        protagonist.health = protagonist.maxHealth

        removeProjectiles()
    }

    fun removeProjectiles() {
        clearRaw()
        addFighter(protagonist)
    }
}