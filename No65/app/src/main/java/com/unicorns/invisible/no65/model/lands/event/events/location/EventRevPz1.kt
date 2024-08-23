package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.REVOLUTION_COURSE_VISITED
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCellBroken
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.view.lands.StringResourceForFormatWrapper
import java.util.Calendar

class EventRevPz1 : Event({ manager ->
    val prevMap = manager.gameState.mapGraph.getMap("map_str_45")
    val teleportCoordinates = Coordinates(-3, 0)
    val teleportCell = prevMap.getTopCell(teleportCoordinates)
    if (teleportCell is TeleportCell) {
        prevMap.clearCell(teleportCoordinates)
        prevMap.createCellOnTop(teleportCoordinates, TeleportCellBroken::class)
    }
    manager.gameState.flagsMaster.add(REVOLUTION_COURSE_VISITED)

    val timeOfDayId = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 5..11 -> R.string.lands_time_of_day_morning
        in 12..16 -> R.string.lands_time_of_day_afternoon
        in 17..23 -> R.string.lands_time_of_day_evening
        else -> R.string.lands_time_of_day_night
    }

    manager.wrapCutsceneSkippable {
        with(drawer) {
            showUnknownTalkingHead()
            showMessageFormatted(
                R.string.lands_rev_puzzle_1_1,
                color = R.color.black,
                tapSoundId = R.raw.sfx_phone,
                formatArgs = arrayOf(StringResourceForFormatWrapper(timeOfDayId))
            )
            showMessages(
                listOf(
                    R.string.lands_rev_puzzle_1_2,
                    R.string.lands_rev_puzzle_1_3,
                    R.string.lands_rev_puzzle_1_4,
                    R.string.lands_rev_puzzle_1_5,
                    R.string.lands_rev_puzzle_1_6,
                ),
                color = R.color.black,
                tapSoundId = R.raw.sfx_phone
            )
            hideTalkingHead()
        }
    }
})