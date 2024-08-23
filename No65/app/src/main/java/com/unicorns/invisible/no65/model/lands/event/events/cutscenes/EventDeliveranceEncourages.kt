package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Deliverance
import com.unicorns.invisible.no65.model.lands.event.Event


class EventDeliveranceEncourages(deliverance: Deliverance) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(deliverance, listOf(
            R.string.lands_deliverance_encourage_1,
            R.string.lands_deliverance_encourage_2,
            R.string.lands_deliverance_encourage_3,
            R.string.lands_deliverance_encourage_4,
            R.string.lands_deliverance_encourage_5,
            R.string.lands_deliverance_encourage_6,
        ))
    }
})