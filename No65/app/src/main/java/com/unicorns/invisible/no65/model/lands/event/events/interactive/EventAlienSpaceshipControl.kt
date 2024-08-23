package com.unicorns.invisible.no65.model.lands.event.events.interactive

import com.unicorns.invisible.no65.model.lands.cell.interactive.AlienSpaceshipControls
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates


class EventAlienSpaceshipControl(thisControl: AlienSpaceshipControls): Event({ manager ->
    thisControl.state = thisControl.state.nextOnPressState()
    val newControlCoordinates = thisControl.coordinates + Coordinates(0, 1)
    val newControl = manager.gameState.currentMap.getTopCell(newControlCoordinates)
    if (newControl is AlienSpaceshipControls) {
        newControl.state = newControl.state.nextOnPressState()
    }
})