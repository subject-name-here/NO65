package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.REVOLUTION_COURSE_VISITED
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Revolution
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.coroutines.delay

class EventRevolutionAndGenocideLock : EventPlaced(lambda@ { manager ->
    val trajectoryWayBack = listOf(
        Coordinates(12, 0),
        Coordinates(11, 0),
        Coordinates(10, 0),
        Coordinates(10, -1),
        Coordinates(10, -2),
        Coordinates(10, -3),
        Coordinates(10, -4),
    )

    manager.wrapCutscene {
        val appearCoordinates = trajectoryWayBack.last()
        val revolution = gameState.currentMap.createCellOnTop(appearCoordinates, Revolution::class)

        val messages = listOf(
            R.string.lands_revolution_genocide_lock_preview_1,
            R.string.lands_revolution_genocide_lock_preview_2,
        )
        drawer.showUnknownCharacterMessages(revolution, messages)
        delay(500L)
        CellUtils.moveOnTrajectory(trajectoryWayBack.reversed(), revolution, gameState.currentMap, 200L)
        delay(500L)

        drawer.showCharacterMessages(revolution, listOf(
            R.string.lands_revolution_genocide_lock_closed_1,
            R.string.lands_revolution_genocide_lock_closed_2,
            R.string.lands_revolution_genocide_lock_closed_3,
            R.string.lands_revolution_genocide_lock_closed_4,
            R.string.lands_revolution_genocide_lock_closed_5,
            R.string.lands_revolution_genocide_lock_closed_6,
            R.string.lands_revolution_genocide_lock_closed_7,
            R.string.lands_revolution_genocide_lock_closed_8,
        ))
        if (REVOLUTION_COURSE_VISITED !in gameState.flagsMaster) {
            drawer.showCharacterMessages(revolution, listOf(
                R.string.lands_revolution_genocide_lock_course_not_visited_1,
                R.string.lands_revolution_genocide_lock_course_not_visited_2,
                R.string.lands_revolution_genocide_lock_course_not_visited_3,
            ))
        } else {
            val tmm = gameState.companions.filterIsInstance<TheMarryingMaiden>().firstOrNull()
            if (tmm != null) {
                drawer.showCharacterMessages(revolution, listOf(
                    R.string.lands_revolution_genocide_lock_tmm_freed_rev_0_1,
                    R.string.lands_revolution_genocide_lock_tmm_freed_rev_0_2,
                    R.string.lands_revolution_genocide_lock_tmm_freed_rev_0_3,
                    R.string.lands_revolution_genocide_lock_tmm_freed_rev_0_4,
                ))
                drawer.showCharacterMessages(tmm, listOf(
                    R.string.lands_revolution_genocide_lock_tmm_freed_tmm_1_1
                ))
                drawer.showCharacterMessages(revolution, listOf(
                    R.string.lands_revolution_genocide_lock_tmm_freed_rev_2_1
                ))
            } else {
                drawer.showCharacterMessages(revolution, listOf(
                    R.string.lands_revolution_genocide_lock_tmm_ignored_1,
                    R.string.lands_revolution_genocide_lock_tmm_ignored_2,
                    R.string.lands_revolution_genocide_lock_tmm_ignored_3,
                    R.string.lands_revolution_genocide_lock_tmm_ignored_4,
                    R.string.lands_revolution_genocide_lock_tmm_ignored_5,
                ))
            }
        }

        delay(500L)
        CellUtils.moveOnTrajectory(trajectoryWayBack, revolution, gameState.currentMap, 200L)
        gameState.currentMap.removeCellFromTop(revolution)
        delay(500L)
    }
})