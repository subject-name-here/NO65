package com.unicorns.invisible.no65.model.lands.event.events.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event


class EventShowMessageMultiline(private val messageMultilineId: Int): Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMultilineMessage(messageMultilineId, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    }
})