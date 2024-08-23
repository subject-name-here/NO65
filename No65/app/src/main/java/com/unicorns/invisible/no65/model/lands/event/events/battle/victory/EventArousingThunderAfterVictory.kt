package com.unicorns.invisible.no65.model.lands.event.events.battle.victory

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event


class EventArousingThunderAfterVictory: Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_arousing_thunder_after_victory_1,
            R.string.lands_arousing_thunder_after_victory_2,
            R.string.lands_arousing_thunder_after_victory_3,
            R.string.lands_arousing_thunder_after_victory_4,
            R.string.lands_arousing_thunder_after_victory_5,
            R.string.lands_arousing_thunder_after_victory_6,
        ))
    }
})