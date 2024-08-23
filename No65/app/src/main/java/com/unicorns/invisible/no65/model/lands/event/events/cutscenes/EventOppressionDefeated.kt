package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.event.Event

class EventOppressionDefeated : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_oppression_defeated_sp_1,
                R.string.lands_oppression_defeated_sp_2,
                R.string.lands_oppression_defeated_sp_3,
                R.string.lands_oppression_defeated_sp_4,
            )
        )

        (gameState.protagonist as CellProtagonist).youState = CellProtagonist.YouState.CREEP_AGAIN
    }
})