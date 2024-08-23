package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.event.Event

class EventRemoveCell(cell: CellNonEmpty) : Event({ manager ->
    manager.gameState.currentMap.removeCellFromTop(cell)
})