package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredCounters.Companion.AT_SHOWDOWN_NUMBER_OF_HITS
import com.unicorns.invisible.no65.model.lands.event.Event


class EventATShowdownHit : Event({ manager ->
    manager.wrapCutsceneSkippable {
        when (gameState.countersMaster.preInc(AT_SHOWDOWN_NUMBER_OF_HITS)) {
            1, 2 -> {
                drawer.showMessagesPhoneWithUnknownHead(listOf(
                    R.string.lands_arousing_thunder_core_hit_1_1,
                ))
            }
            3 -> {
                drawer.showMessagesPhoneWithUnknownHead(listOf(
                    R.string.lands_arousing_thunder_core_hit_2_1,
                    R.string.lands_arousing_thunder_core_hit_2_2,
                    R.string.lands_arousing_thunder_core_hit_2_3,
                    R.string.lands_arousing_thunder_core_hit_2_4,
                    R.string.lands_arousing_thunder_core_hit_2_5,
                    R.string.lands_arousing_thunder_core_hit_2_6,
                    R.string.lands_arousing_thunder_core_hit_2_7,
                    R.string.lands_arousing_thunder_core_hit_2_8,
                ))
            }
            4, 5 -> {
                drawer.showMessagesPhoneWithUnknownHead(listOf(
                    R.string.lands_arousing_thunder_core_hit_3_1,
                    R.string.lands_arousing_thunder_core_hit_3_2,
                ))
            }
            else -> {
                drawer.showMessagesPhoneWithUnknownHead(listOf(
                    R.string.lands_arousing_thunder_core_hit_else_1
                ))
            }
        }
    }
})