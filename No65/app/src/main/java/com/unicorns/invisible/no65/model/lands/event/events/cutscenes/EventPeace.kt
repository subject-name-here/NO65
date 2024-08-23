package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Peace
import com.unicorns.invisible.no65.model.lands.event.Event


class EventPeace(
    private val characterCell: Peace,
): Event({ manager ->
    manager.wrapCutscene {
        drawer.showCharacterMessages(characterCell, listOf(
            R.string.lands_peace_event_1,
            R.string.lands_peace_event_2,
            R.string.lands_peace_event_3,
            R.string.lands_peace_event_4,
            R.string.lands_peace_event_5,
            R.string.lands_peace_event_6,
            R.string.lands_peace_event_7,
            R.string.lands_peace_event_8,
        ))
    }
})