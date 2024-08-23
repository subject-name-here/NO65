package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.coroutines.delay


class EventRevSoc : Event(lambda@ { manager ->
    val tmm = manager.gameState.companions.filterIsInstance<TheMarryingMaiden>().firstOrNull() ?: return@lambda

    manager.wrapCutscene {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_rev_sockets_rev_0_1,
                R.string.lands_rev_sockets_rev_0_2,
                R.string.lands_rev_sockets_rev_0_3,
            )
        )
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_rev_sockets_rev_0_4,
            ),
            delayAfterMessage = 0L
        )

        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_rev_sockets_tmm_1_1,
            R.string.lands_rev_sockets_tmm_1_2,
        ))

        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_rev_sockets_rev_2_1,
                R.string.lands_rev_sockets_rev_2_2,
                R.string.lands_rev_sockets_rev_2_3,
                R.string.lands_rev_sockets_rev_2_4,
                R.string.lands_rev_sockets_rev_2_5,
                R.string.lands_rev_sockets_rev_2_6,
            )
        )

        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_rev_sockets_tmm_3_1,
            R.string.lands_rev_sockets_tmm_3_2,
            R.string.lands_rev_sockets_tmm_3_3,
        ))

        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_rev_sockets_rev_4_1,
            )
        )

        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_rev_sockets_tmm_5_1,
            R.string.lands_rev_sockets_tmm_5_2,
        ))

        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_rev_sockets_rev_6_1,
                R.string.lands_rev_sockets_rev_6_2,
            )
        )

        delay(500L)

        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_rev_sockets_tmm_7_1,
            R.string.lands_rev_sockets_tmm_7_2,
            R.string.lands_rev_sockets_tmm_7_3,
        ))
    }
})