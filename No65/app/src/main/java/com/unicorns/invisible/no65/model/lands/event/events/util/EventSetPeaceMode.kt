package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.event.Event


class EventSetPeaceMode : Event(lambda@ { manager ->
    if (manager.gameState !is GameState65) return@lambda
    manager.gameState.battleMode = BattleMode.PEACE
})