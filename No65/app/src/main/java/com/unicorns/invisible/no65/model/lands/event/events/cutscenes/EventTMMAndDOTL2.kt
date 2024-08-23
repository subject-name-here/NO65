package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.DarkeningOfTheLight
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.Event


class EventTMMAndDOTL2(mm: TheMarryingMaiden, dotl: DarkeningOfTheLight) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(mm, listOf(
            R.string.lands_tmm_and_dotl_2_tmm_0_1,
        ))
        drawer.showCharacterMessages(dotl, listOf(
            R.string.lands_tmm_and_dotl_2_dotl_1_1,
            R.string.lands_tmm_and_dotl_2_dotl_1_2,
        ))
        drawer.showCharacterMessages(mm, listOf(
            R.string.lands_tmm_and_dotl_2_tmm_2_1,
        ))
        drawer.showCharacterMessages(dotl, listOf(
            R.string.lands_tmm_and_dotl_2_dotl_3_1,
        ))
    }
})