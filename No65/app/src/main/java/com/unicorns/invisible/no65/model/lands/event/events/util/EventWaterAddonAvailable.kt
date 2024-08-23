package com.unicorns.invisible.no65.model.lands.event.events.util

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredEvents
import com.unicorns.invisible.no65.model.lands.cell.interactive.CellMessage
import com.unicorns.invisible.no65.model.lands.cell.interactive.Note
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.WATER_ADDON_AVAILABLE
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates


class EventWaterAddonAvailable : Event(lambda@ { manager ->
    val events = manager.gameState.eventMaster
    val key = RegisteredEvents.OnUseEventKey("map_tpl_off", Coordinates(4, -2))
    val cell = manager.gameState.currentMap.getTopCell(key.cell)
    if (cell !is Note || cell.state != CellMessage.State.CLICKED) {
        events.onUseEventsFlags[key] = false
        return@lambda
    }

    val isFlagSet = GlobalState.getBoolean(manager.activity, WATER_ADDON_AVAILABLE)
    if (!isFlagSet) {
        GlobalState.putBoolean(manager.activity, WATER_ADDON_AVAILABLE, true)
        manager.wrapCutscene {
            drawer.showMessages(
                listOf(
                    R.string.lands_waterever_available_1,
                    R.string.lands_waterever_available_2,
                ),
                color = R.color.black,
                tapSoundId = R.raw.sfx_tap
            )
        }
    }
})