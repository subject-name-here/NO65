package com.unicorns.invisible.no65.model.lands.event.events.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event


class EventShowPOIMessageMultiline(private val messageMultilineId: Int): Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessage(R.string.lands_message_point_of_interest_intro, color = R.color.black, tapSoundId = R.raw.sfx_tap)
        drawer.showMultilineMessage(messageMultilineId, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    }
})