package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ArousingThunder
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.coroutines.delay


class EventATGhostAttacked(characterCell: ArousingThunder) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_arousing_thunder_ghost_attacked_sp_0_1,
            R.string.lands_arousing_thunder_ghost_attacked_sp_0_2,
            R.string.lands_arousing_thunder_ghost_attacked_sp_0_3,
            R.string.lands_arousing_thunder_ghost_attacked_sp_0_4,
            R.string.lands_arousing_thunder_ghost_attacked_sp_0_5,
        ))

        delay(1000L)

        drawer.showCharacterMessages(characterCell, listOf(
            R.string.lands_arousing_thunder_ghost_attacked_at_1,
            R.string.lands_arousing_thunder_ghost_attacked_at_2,
            R.string.lands_arousing_thunder_ghost_attacked_at_3,
            R.string.lands_arousing_thunder_ghost_attacked_at_4,
        ))

        delay(2000L)

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_arousing_thunder_ghost_attacked_sp_1_1,
            R.string.lands_arousing_thunder_ghost_attacked_sp_1_2,
            R.string.lands_arousing_thunder_ghost_attacked_sp_1_3,
        ))
    }
})