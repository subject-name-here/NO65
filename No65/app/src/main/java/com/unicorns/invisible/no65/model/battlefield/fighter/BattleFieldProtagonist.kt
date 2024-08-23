package com.unicorns.invisible.no65.model.battlefield.fighter

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.knowledge.Knowledge
import com.unicorns.invisible.no65.util.Coordinates
import kotlin.math.min


class BattleFieldProtagonist(
    val knowledge: Knowledge,
    private val isInverted: Boolean = false
) : BattleFieldFighter() {
    override var maxHealth: Int = BASIC_HEALTH
    override var health: Int = maxHealth

    override fun getString() = "@"
    override fun getStringColor(): Int = if (isInverted) R.color.white else R.color.black
    override fun onTick(tickNumber: Int, battleField: BattleField) {}

    var multiplier: Int = 1
    var hasWindRequiem: Boolean = false
    var hasAnkh: Boolean = false

    override fun takeDamage(dp: Int) {
        super.takeDamage(dp)
        if (dp > 0) {
            dropMultiplier()
        }
    }
    fun heal() {
        health = min(health + HEAL_POINTS, maxHealth)
    }
    fun healRequiem() {
        health = maxHealth
    }

    fun incrementMultiplier() {
        multiplier = min(multiplier * MULTIPLIER_INCREMENT, MAX_MULTIPLIER)
    }
    fun incrementMultiplierRequiem() {
        when {
            hasWindRequiem -> {
                return
            }
            multiplier * MULTIPLIER_INCREMENT > MAX_MULTIPLIER -> {
                hasWindRequiem = true
            }
            else -> {
                incrementMultiplier()
            }
        }
    }

    fun dropMultiplier() {
        multiplier = 1
        hasWindRequiem = false
    }

    class JournalEntry(protagonist: BattleFieldProtagonist) {
        val health: Int = protagonist.health
        val coordinates: Coordinates = protagonist.position
        val multiplier: Int = protagonist.multiplier
        val hasWindRequiem: Boolean = protagonist.hasWindRequiem
        val hasAnkh: Boolean = protagonist.hasAnkh
    }
    fun restoreFromJournal(entry: JournalEntry) {
        health = entry.health
        position = entry.coordinates
        multiplier = entry.multiplier
        hasAnkh = entry.hasAnkh
        hasWindRequiem = entry.hasWindRequiem
    }

    companion object {
        const val BASIC_HEALTH = 65

        const val MULTIPLIER_INCREMENT = 3
        const val MAX_MULTIPLIER = 3

        const val HEAL_POINTS = 13
    }
}