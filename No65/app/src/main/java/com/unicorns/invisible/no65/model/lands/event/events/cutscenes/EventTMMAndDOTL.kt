package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.DarkeningOfTheLight
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.Event


class EventTMMAndDOTL(mm: TheMarryingMaiden, dotl: DarkeningOfTheLight) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(mm, listOf(
            R.string.lands_tmm_and_dotl_1_tmm_0_1,
            R.string.lands_tmm_and_dotl_1_tmm_0_2,
        ))
        drawer.showCharacterMessages(dotl, listOf(
            R.string.lands_tmm_and_dotl_1_dotl_1_1,
            R.string.lands_tmm_and_dotl_1_dotl_1_2,
        ))
        drawer.showCharacterMessages(mm, listOf(
            R.string.lands_tmm_and_dotl_1_tmm_2_1,
            R.string.lands_tmm_and_dotl_1_tmm_2_2,
        ))
        drawer.showCharacterMessages(dotl, listOf(
            R.string.lands_tmm_and_dotl_1_dotl_3_1,
            R.string.lands_tmm_and_dotl_1_dotl_3_2,
            R.string.lands_tmm_and_dotl_1_dotl_3_3,
            R.string.lands_tmm_and_dotl_1_dotl_3_4,
            R.string.lands_tmm_and_dotl_1_dotl_3_5,
        ))
        drawer.showCharacterMessages(mm, listOf(
            R.string.lands_tmm_and_dotl_1_tmm_4_1,
        ))
    }
})