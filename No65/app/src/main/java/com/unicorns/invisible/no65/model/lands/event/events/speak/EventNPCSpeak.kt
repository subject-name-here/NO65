package com.unicorns.invisible.no65.model.lands.event.events.speak

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.lands.cell.character.CellNonStaticCharacter
import com.unicorns.invisible.no65.model.lands.event.Event

class EventNPCSpeak(
    val characterCell: CellNonStaticCharacter,
    val messageGetter: (LandsManager) -> Int
): Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(characterCell, listOf(messageGetter(this)))
    }
})