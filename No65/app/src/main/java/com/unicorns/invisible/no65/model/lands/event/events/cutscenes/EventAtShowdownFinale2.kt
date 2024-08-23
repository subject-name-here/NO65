package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ArousingThunder
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.AT_BATTLE_EVENT_REACHED
import com.unicorns.invisible.no65.saveload.GlobalState
import kotlinx.coroutines.delay


class EventAtShowdownFinale2(thunderCell: ArousingThunder) : Event({ manager ->
    manager.wrapCutscene {
        drawer.showCharacterMessages(thunderCell,
            listOf(
                R.string.lands_arousing_thunder_showdown2_at_0_1,
            ),
        )
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_arousing_thunder_showdown2_sp_1_1,
            )
        )
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_arousing_thunder_showdown2_sp_1_2,
            ),
            delayAfterMessage = 0L
        )

        drawer.showCharacterMessages(thunderCell,
            listOf(
                R.string.lands_arousing_thunder_showdown2_at_2_1,
                R.string.lands_arousing_thunder_showdown2_at_2_2,
            )
        )

        delay(2000L)

        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_arousing_thunder_showdown2_sp_3_1,
            ),
        )

        delay(2000L)

        drawer.showCharacterMessages(thunderCell,
            listOf(
                R.string.lands_arousing_thunder_showdown2_at_4_1,
            ),
        )

        delay(2000L)

        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_arousing_thunder_showdown2_sp_5_1,
                R.string.lands_arousing_thunder_showdown2_sp_5_2,
                R.string.lands_arousing_thunder_showdown2_sp_5_3,
            ),
        )

        delay(2000L)

        drawer.showCharacterMessages(thunderCell,
            listOf(
                R.string.lands_arousing_thunder_showdown2_at_6_1,
                R.string.lands_arousing_thunder_showdown2_at_6_2,
                R.string.lands_arousing_thunder_showdown2_at_6_3,
                R.string.lands_arousing_thunder_showdown2_at_6_4,
            )
        )

        GlobalState.putBoolean(activity, AT_BATTLE_EVENT_REACHED, true)
    }
})