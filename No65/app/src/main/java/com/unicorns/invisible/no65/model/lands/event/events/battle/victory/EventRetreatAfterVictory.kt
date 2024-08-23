package com.unicorns.invisible.no65.model.lands.event.events.battle.victory

import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.RETREAT_DEAD
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.CellUtils


class EventRetreatAfterVictory: Event({ manager ->
    CellUtils.litUpCurrentMapFloors(manager)
    manager.gameState.flagsMaster.add(RETREAT_DEAD)
    manager.activity.musicPlayer.stopAllMusic()
})