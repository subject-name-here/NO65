package com.unicorns.invisible.no65.model.lands.event

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist


abstract class EventPlaced(protected open val thisEventLambdaPlaced: suspend EventPlaced.(LandsManager) -> Unit): Event() {
    protected var firingCell: CellNonEmpty? = null
    fun attachFiringCell(cell: CellNonEmpty) {
        this.firingCell = cell
    }

    protected open fun getConditionToFire(manager: LandsManager): Boolean {
        return firingCell is CellProtagonist
    }

    val id: String = this.javaClass.simpleName
    fun reloadEvent(manager: LandsManager) {
        manager.gameState.eventMaster.onStepEventsFlags[id] = false
    }

    final override val thisEventLambda: suspend Event.(LandsManager) -> Unit = lambda@ { manager ->
        if (getConditionToFire(manager)) {
            thisEventLambdaPlaced(manager)
        } else {
            reloadEvent(manager)
        }
    }

    companion object {
        val Null
            get() = object : EventPlaced({}) {}
    }
}