package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.LEFT_RHQ_HALL
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.launchCoroutine
import kotlinx.coroutines.delay


class Event5Minutes : Event({ manager ->
    launchCoroutine {
        delay(1000L * 60 * 5)
        if (!manager.stopped) {
            if (LEFT_RHQ_HALL !in manager.gameState.flagsMaster) {
                manager.wrapCutsceneSkippable {
                    drawer.showMessagesPhoneWithUnknownHead(listOf(
                        R.string.lands_event_5_minutes_1,
                        R.string.lands_event_5_minutes_2,
                        R.string.lands_event_5_minutes_3,
                        R.string.lands_event_5_minutes_4,
                    ))
                }
            }
        }
    }
})