package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.EventPlaced

class EventJailKSMOffices2OnTeleport : EventPlaced({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessage(
            R.string.lands_jail_ksm_offices_2_on_teleport_1,
            color = R.color.black,
            tapSoundId = R.raw.sfx_tap
        )
    }
})