package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.CreativeHeaven
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.EventSave


class EventCreativeHeaven(
    private val characterCell: CreativeHeaven,
): Event({ manager ->
    manager.wrapCutscene {
        manager.drawer.showCharacterMessages(characterCell, listOf(
            R.string.lands_creative_heaven_event_1,
            R.string.lands_creative_heaven_event_2,
            R.string.lands_creative_heaven_event_3,
            R.string.lands_creative_heaven_event_4,
        ))
    }
    EventSave(silent = true).fireEventChain(manager)
})