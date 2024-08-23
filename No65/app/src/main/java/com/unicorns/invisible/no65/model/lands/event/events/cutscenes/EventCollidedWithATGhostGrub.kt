package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.COLLIDED_WITH_GRUB_CUTSCENE1
import com.unicorns.invisible.no65.model.lands.event.Event


class EventCollidedWithATGhostGrub : Event (lambda@ { manager ->
    if (COLLIDED_WITH_GRUB_CUTSCENE1 !in manager.gameState.flagsMaster) {
        manager.gameState.flagsMaster.add(COLLIDED_WITH_GRUB_CUTSCENE1)
        manager.wrapCutsceneSkippable {
            drawer.showMessagesPhoneWithUnknownHead(listOf(
                R.string.lands_arousing_thunder_precore_hit_0_1,
            ))
        }
    }
})