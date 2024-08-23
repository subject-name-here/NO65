package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ArousingThunder
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.coroutines.delay

class EventATShowdownFinale1(thunderCell: ArousingThunder) : Event({ manager ->
    manager.wrapCutscene {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_arousing_thunder_showdown1_sp_0_1,
                R.string.lands_arousing_thunder_showdown1_sp_0_2,
            )
        )
        delay(1000L)

        drawer.showCharacterMessages(thunderCell,
            listOf(
                R.string.lands_arousing_thunder_showdown1_at_1_1,
                R.string.lands_arousing_thunder_showdown1_at_1_2,
                R.string.lands_arousing_thunder_showdown1_at_1_3,
            ),
        )

        delay(5000L)

        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_arousing_thunder_showdown1_sp_2_1,
                R.string.lands_arousing_thunder_showdown1_sp_2_2,
            ),
        )
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_arousing_thunder_showdown1_sp_2_3,
            ),
            delayAfterMessage = 0L
        )

        drawer.showCharacterMessages(thunderCell,
            listOf(
                R.string.lands_arousing_thunder_showdown1_at_3_1
            )
        )

        delay(1000L)
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_arousing_thunder_showdown1_sp_4_1
            )
        )
    }
})