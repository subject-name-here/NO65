package com.unicorns.invisible.no65.model.lands.event.events.battle

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.STARTED_GAME
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.DIED_AT_LEAST_ONCE
import com.unicorns.invisible.no65.saveload.GlobalState


class EventBattleDefeat: Event({ manager ->
    if (STARTED_GAME in manager.gameState.flagsMaster) {
        GlobalState.putBoolean(manager.activity, DIED_AT_LEAST_ONCE, true)
    }
    manager.activity.returnToMenu()
})