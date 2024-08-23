package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.coroutines.delay

class EventMeetAT : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_meet_arousing_thunder_1,
            ),
            delayAfterMessage = 0L
        )
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_meet_arousing_thunder_2,
                R.string.lands_meet_arousing_thunder_3,
            )
        )
        delay(5000L)
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_meet_arousing_thunder_4,
                R.string.lands_meet_arousing_thunder_5,
                R.string.lands_meet_arousing_thunder_6,
                R.string.lands_meet_arousing_thunder_7,
                R.string.lands_meet_arousing_thunder_8,
                R.string.lands_meet_arousing_thunder_9,
                R.string.lands_meet_arousing_thunder_10,
                R.string.lands_meet_arousing_thunder_11,
                R.string.lands_meet_arousing_thunder_12,
                R.string.lands_meet_arousing_thunder_13,
            )
        )
    }
})