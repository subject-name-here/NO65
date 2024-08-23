package com.unicorns.invisible.no65.model.lands.event.events

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.RegisteredCounters.Companion.SAVE_COUNTER
import com.unicorns.invisible.no65.model.lands.cell.CellEmpty
import com.unicorns.invisible.no65.model.lands.cell.character.npc.AfterCompletion
import com.unicorns.invisible.no65.model.lands.cell.floor.CellFloorWhite
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.SaveManager
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.launchCoroutine
import kotlinx.coroutines.delay


class EventSave(private val silent: Boolean = false): Event(lambda@ { manager ->
    if (manager.gameState !is GameState65) return@lambda

    if (!silent) {
        val cellsInSight = CellUtils.getCellsInSight(manager)
        val emptyCells = cellsInSight.filterIsInstance<CellEmpty>()
        if (emptyCells.isNotEmpty()) {
            val emptyCellCoordinates = emptyCells.random().coordinates
            manager.gameState.currentMap.createCellOnTop(emptyCellCoordinates, CellFloorWhite::class)
            val ac = manager.gameState.currentMap.createCellOnTop(emptyCellCoordinates, AfterCompletion::class)
            launchCoroutine {
                delay(300L)
                manager.gameState.currentMap.clearCell(emptyCellCoordinates)
            }

            manager.wrapCutsceneSkippable {
                drawer.showUnknownCharacterMessages(ac, listOf(R.string.saving))
            }
        } else {
            manager.wrapCutsceneSkippable {
                drawer.showMessagesPhoneWithUnknownHead(listOf(R.string.saving))
            }
        }
    }
    SaveManager.saveState(manager.gameState, manager.activity)
    manager.gameState.countersMaster.preInc(SAVE_COUNTER)
})