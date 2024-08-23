package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.coroutines.delay


class EventWayBack : Event(lambda@ { manager ->
    delay(5000L)
    if (manager.stopped) {
        return@lambda
    }

    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_fist_way_back_1,
            R.string.lands_fist_way_back_2,
            R.string.lands_fist_way_back_3,
            R.string.lands_fist_way_back_4,
        ))
    }
})