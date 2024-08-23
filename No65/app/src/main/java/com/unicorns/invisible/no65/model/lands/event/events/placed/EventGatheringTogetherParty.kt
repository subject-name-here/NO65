package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.GATHERING_TOGETHER_PARTY_EXPLAINED
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.npc.GatheringTogether
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.util.CellUtils
import kotlinx.coroutines.delay


class EventGatheringTogetherParty : EventPlaced(lambda@ { manager ->
    val gt = manager.gameState.currentMap.getTopCells()
        .filterIsInstance<GatheringTogether>()
        .firstOrNull() ?: return@lambda

    val closestFloor = CellUtils.findCurrentMapClosestFloor(manager, gt)!!
    if (GATHERING_TOGETHER_PARTY_EXPLAINED !in manager.gameState.flagsMaster) {
        manager.wrapCutscene {
            drawer.showCharacterMessages(gt, listOf(
                R.string.lands_gathering_together_on_leave_1,
                R.string.lands_gathering_together_on_leave_2,
                R.string.lands_gathering_together_on_leave_3,
                R.string.lands_gathering_together_on_leave_4,
            ))

            delay(500L)

            gameState.currentMap.moveTo(gameState.protagonist, closestFloor)

            delay(500L)
            drawer.showCharacterMessages(gt, listOf(
                R.string.lands_gathering_together_on_leave_5,
            ))

            gameState.flagsMaster.add(GATHERING_TOGETHER_PARTY_EXPLAINED)
        }
    } else {
        manager.gameState.currentMap.moveTo(manager.gameState.protagonist, closestFloor)
    }
    reloadEvent(manager)
}) {
    override fun getConditionToFire(manager: LandsManager): Boolean {
        if (firingCell !is CellProtagonist) {
            return false
        }

        return manager.gameState.currentMap.getTopCells()
            .filterIsInstance<GatheringTogether>()
            .isNotEmpty()
    }
}