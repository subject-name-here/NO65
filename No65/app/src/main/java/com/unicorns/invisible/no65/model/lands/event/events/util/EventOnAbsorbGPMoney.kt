package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.GREAT_POSSESSION_MONEY_ABSORBED
import com.unicorns.invisible.no65.model.lands.event.Event

class EventOnAbsorbGPMoney : Event({ manager ->
    manager.gameState.flagsMaster.add(GREAT_POSSESSION_MONEY_ABSORBED)
})