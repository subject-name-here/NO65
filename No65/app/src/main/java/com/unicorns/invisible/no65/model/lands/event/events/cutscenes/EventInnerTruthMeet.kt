package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.InnerTruth
import com.unicorns.invisible.no65.model.lands.event.Event


class EventInnerTruthMeet(
    private val characterCell: InnerTruth,
): Event({ manager ->
    manager.wrapCutscene {
        drawer.showCharacterMessages(characterCell, listOf(
            R.string.lands_inner_truth_meet_1,
            R.string.lands_inner_truth_meet_2,
            R.string.lands_inner_truth_meet_3,
        ))
    }
})