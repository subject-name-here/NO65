package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.THE_MARRYING_MAIDEN_LEFT_THE_PARTY
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.model.lands.event.events.EventTeleport
import com.unicorns.invisible.no65.util.Coordinates

class EventTempleAlleyTeleport : EventPlaced({ manager ->
    EventTeleport(0, Coordinates(0, 0), manager.gameState.protagonist).fireEventChain(manager)
}) {
    override fun getConditionToFire(manager: LandsManager): Boolean {
        return firingCell is CellProtagonist && THE_MARRYING_MAIDEN_LEFT_THE_PARTY in manager.gameState.flagsMaster
    }
}