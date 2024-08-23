package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.KSM_TRAPPORT_FIRED
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.npc.KeepingStillMountain
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates

class EventKSMTrapport(coordinates: Coordinates) : Event({ manager ->
    val hasKSM = manager.gameState.currentMap.getTopCells()
        .filterIsInstance<KeepingStillMountain>()
        .isNotEmpty()
    val firingCell = manager.gameState.currentMap.getTopCell(coordinates)
    if (firingCell is CellProtagonist && hasKSM) {
        val protagonist = manager.gameState.protagonist
        manager.gameState.currentMap.moveTo(protagonist, Coordinates.ZERO)

        if (KSM_TRAPPORT_FIRED !in manager.gameState.flagsMaster) {
            manager.gameState.flagsMaster.add(KSM_TRAPPORT_FIRED)
            manager.wrapCutsceneSkippable {
                drawer.showMessages(
                    listOf(
                        R.string.lands_jail_ksm_trapport_1,
                        R.string.lands_jail_ksm_trapport_2,
                    ),
                    color = R.color.black,
                    tapSoundId = R.raw.sfx_tap
                )
            }
        }
    }
})