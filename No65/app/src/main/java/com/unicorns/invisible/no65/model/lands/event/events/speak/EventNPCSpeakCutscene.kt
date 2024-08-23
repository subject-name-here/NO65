package com.unicorns.invisible.no65.model.lands.event.events.speak

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.lands.cell.character.CellNonStaticCharacter
import com.unicorns.invisible.no65.model.lands.event.Event

class EventNPCSpeakCutscene(
    val characterCell: CellNonStaticCharacter,
    val isSkippable: Boolean = false,
    val messagesGetter: (LandsManager) -> List<Int>
): Event({ manager ->
    if (isSkippable) {
        manager.wrapCutsceneSkippable {
            drawer.showCharacterMessages(characterCell, messagesGetter(this))
        }
    } else {
        manager.wrapCutscene {
            drawer.showCharacterMessages(characterCell, messagesGetter(this))
        }
    }
})