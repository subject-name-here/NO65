package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.GreatPossession
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.coroutines.delay


class EventGreatPossessionWelcome(gp: GreatPossession) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(gp, listOf(
            R.string.lands_great_possession_welcome_1,
            R.string.lands_great_possession_welcome_2,
            R.string.lands_great_possession_welcome_3,
            R.string.lands_great_possession_welcome_4,
            R.string.lands_great_possession_welcome_5,
            R.string.lands_great_possession_welcome_6,
            R.string.lands_great_possession_welcome_7,
            R.string.lands_great_possession_welcome_8,
        ))

        delay(500L)
        gp.state = GreatPossession.State.CITY_KNOWS_BANNERMAN
        delay(500L)

        drawer.showCharacterMessages(gp, listOf(
            R.string.lands_great_possession_welcome_9,
            R.string.lands_great_possession_welcome_10,
            R.string.lands_great_possession_welcome_11,
        ))
    }
})