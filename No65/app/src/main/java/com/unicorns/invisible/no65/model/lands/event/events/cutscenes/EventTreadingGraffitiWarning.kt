package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.TREADING_WARNING_ACTIVATED
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Treading
import com.unicorns.invisible.no65.model.lands.event.Event


class EventTreadingGraffitiWarning : Event({ manager ->
    manager.gameState.flagsMaster.add(TREADING_WARNING_ACTIVATED)
    val treading = manager.gameState.currentMap.getTopCells().filterIsInstance<Treading>().firstOrNull()!!
    manager.wrapCutscene {
        drawer.showCharacterMessages(treading, listOf(
            R.string.lands_treading_warning_1,
            R.string.lands_treading_warning_2,
            R.string.lands_treading_warning_3,
            R.string.lands_treading_warning_4,
            R.string.lands_treading_warning_5,
            R.string.lands_treading_warning_6,
        ))
    }
})