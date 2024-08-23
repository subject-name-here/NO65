package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Opposition
import com.unicorns.invisible.no65.model.lands.event.Event


class EventOppositionMacGuffinDestroyed : Event({ manager ->
    val opposition = manager.gameState.currentMap.getTopCells().filterIsInstance<Opposition>().firstOrNull()
    if (opposition != null) {
        manager.wrapCutsceneSkippable {
            drawer.showCharacterMessages(opposition, listOf(
                R.string.lands_opposition_city_property_destroyed_1
            ))
        }
    }
})