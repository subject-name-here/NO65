package com.unicorns.invisible.no65.model.battlefield.projectile.projectiles

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.BattleField
import com.unicorns.invisible.no65.model.battlefield.projectile.BattleFieldProjectile
import com.unicorns.invisible.no65.util.Coordinates
import kotlin.math.max

class BCSpaceDistorter(
    position: Coordinates
) : BattleFieldProjectile(
    position, Direction.NO_MOVEMENT
) {
    override fun getString(): String = "ӻ"
    override fun getStringColor(): Int = if (charge == 0) R.color.light_grey else R.color.dark_grey
    override val priority: Int = 10

    private var charge = 1
    private var chargeLossTickNumber = -1
    override fun onTick(tickNumber: Int, battleField: BattleField) {
        super.onTick(tickNumber, battleField)

        when {
            charge == 0 && tickNumber >= chargeLossTickNumber + 2 -> {
                battleField.removeProjectile(this)
            }
            charge > 0 -> {
                if (position.row == 0) {
                    checkOppositeDistorter(tickNumber, battleField)
                }
            }
        }
    }

    private fun checkOppositeDistorter(tickNumber: Int, battleField: BattleField) {
        val oppositeCoordinates = Coordinates(battleField.height - 1, position.col)
        val occupant = battleField.getMap()[oppositeCoordinates]
        if (occupant is BCSpaceDistorter) {
            val totalCharge = ((charge + occupant.charge) * TOTAL_CHARGE_COEFFICIENT).toInt()
            occupant.charge = 0
            occupant.chargeLossTickNumber = tickNumber
            charge = 0
            chargeLossTickNumber = tickNumber
            (1 until battleField.height - 1).forEach { row ->
                val projectileCoordinates = Coordinates(row, position.col)
                val directions = Direction.values().toMutableList() - Direction.NO_MOVEMENT
                val proj = SpaceDistortion(projectileCoordinates, directions.random())
                proj.damage = totalCharge
                battleField.addProjectile(proj)
            }
        }
    }

    fun increaseCharge() {
        charge = max(charge + 1, MAX_CHARGE)
        chargeLossTickNumber = -1
    }

    companion object {
        const val MAX_CHARGE = 2
        const val TOTAL_CHARGE_COEFFICIENT = 0.5
    }
}