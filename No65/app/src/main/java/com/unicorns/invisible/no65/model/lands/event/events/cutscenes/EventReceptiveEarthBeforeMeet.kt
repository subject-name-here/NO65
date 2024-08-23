package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredCounters.Companion.TIMES_RE_TRIGGER_FIRED
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ReceptiveEarth
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.choose
import com.unicorns.invisible.no65.util.randBooleanPercent

class EventReceptiveEarthBeforeMeet(
    private val cellCharacter: ReceptiveEarth,
) : Event(lambda@ { manager ->
    val message = when (manager.gameState.countersMaster.preInc(TIMES_RE_TRIGGER_FIRED)) {
        1 -> R.string.lands_receptive_earth_before_meet_1
        2 -> R.string.lands_receptive_earth_before_meet_2
        3 -> R.string.lands_receptive_earth_before_meet_3
        4 -> R.string.lands_receptive_earth_before_meet_4
        5 -> R.string.lands_receptive_earth_before_meet_5
        6 -> R.string.lands_receptive_earth_before_meet_6
        7 -> R.string.lands_receptive_earth_before_meet_7
        else -> {
            if (randBooleanPercent(75)) return@lambda
            choose(
                R.string.lands_receptive_earth_before_meet_random_1,
                R.string.lands_receptive_earth_before_meet_random_2,
                R.string.lands_receptive_earth_before_meet_random_3,
                R.string.lands_receptive_earth_before_meet_random_4,
                R.string.lands_receptive_earth_before_meet_random_5,
                R.string.lands_receptive_earth_before_meet_random_6,
            )
        }
    }

    manager.wrapCutsceneSkippable {
        drawer.showUnknownCharacterMessages(cellCharacter, listOf(message))
    }
})