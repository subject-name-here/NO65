package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Deliverance
import com.unicorns.invisible.no65.model.lands.cell.character.npc.GreatPossession
import com.unicorns.invisible.no65.model.lands.event.Event

class EventDeliveranceHimIntro(
    deliverance: Deliverance,
    greatPossession: GreatPossession
) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(deliverance, listOf(
            R.string.lands_deliverance_him_intro_d_1_1,
            R.string.lands_deliverance_him_intro_d_1_2,
        ))
        drawer.showCharacterMessages(deliverance, listOf(
            R.string.lands_deliverance_him_intro_d_1_3,
        ), delayAfterMessage = 0L)
        drawer.showCharacterMessages(greatPossession, listOf(
            R.string.lands_deliverance_him_intro_gp_2_1,
            R.string.lands_deliverance_him_intro_gp_2_2,
            R.string.lands_deliverance_him_intro_gp_2_3,
        ))
        drawer.showCharacterMessages(deliverance, listOf(
            R.string.lands_deliverance_him_intro_d_3_1,
            R.string.lands_deliverance_him_intro_d_3_2,
        ))
    }
})