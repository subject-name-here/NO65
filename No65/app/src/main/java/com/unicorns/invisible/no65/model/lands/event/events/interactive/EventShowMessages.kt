package com.unicorns.invisible.no65.model.lands.event.events.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event


class EventShowMessages(val messages: List<Int>): Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessages(messages, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    }
})