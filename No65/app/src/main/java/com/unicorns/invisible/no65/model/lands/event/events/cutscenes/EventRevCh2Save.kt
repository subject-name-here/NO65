package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.control.door.Door
import com.unicorns.invisible.no65.model.lands.cell.control.door.DoorMode
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.EventSave


class EventRevCh2Save : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessage(R.string.saving, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    }
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_revolution_choice_2_after_saving_1,
            R.string.lands_revolution_choice_2_after_saving_2,
            R.string.lands_revolution_choice_2_after_saving_3,
            R.string.lands_revolution_choice_2_after_saving_4,
            R.string.lands_revolution_choice_2_after_saving_5,
            R.string.lands_revolution_choice_2_after_saving_6,
            R.string.lands_revolution_choice_2_after_saving_7,
        ))
    }
    manager.gameState.currentMap.getTopCells().filterIsInstance<Door>().forEach { it.mode = DoorMode.ALL }
    EventSave(silent = true).fireEventChain(manager)
})