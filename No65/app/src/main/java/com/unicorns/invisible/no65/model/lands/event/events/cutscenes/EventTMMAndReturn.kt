package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Return
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.coroutines.delay


class EventTMMAndReturn(tmm: TheMarryingMaiden, returnC: Return) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(returnC, listOf(
            R.string.lands_tmm_and_return_ret_0_1,
        ))
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_tmm_and_return_tmm_1_1,
            R.string.lands_tmm_and_return_tmm_1_2,
        ))
        drawer.showCharacterMessages(returnC, listOf(
            R.string.lands_tmm_and_return_ret_2_1,
            R.string.lands_tmm_and_return_ret_2_2,
        ))
        drawer.showCharacterMessages(returnC, listOf(
            R.string.lands_tmm_and_return_ret_2_3,
        ), delayAfterMessage = 0L)
        tmm.emotionState = TheMarryingMaiden.EmotionState.DISAPPOINTMENT
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_tmm_and_return_tmm_3_1,
            R.string.lands_tmm_and_return_tmm_3_2,
        ))
        drawer.showCharacterMessages(returnC, listOf(
            R.string.lands_tmm_and_return_ret_4_1,
            R.string.lands_tmm_and_return_ret_4_2,
            R.string.lands_tmm_and_return_ret_4_3,
            R.string.lands_tmm_and_return_ret_4_4,
        ))
        tmm.emotionState = TheMarryingMaiden.EmotionState.LAUGHING
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_tmm_and_return_tmm_5_1,
            R.string.lands_tmm_and_return_tmm_5_2,
        ))
        tmm.emotionState = TheMarryingMaiden.EmotionState.SMILING
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_tmm_and_return_tmm_5_3,
            R.string.lands_tmm_and_return_tmm_5_4,
        ))
        drawer.showCharacterMessages(returnC, listOf(
            R.string.lands_tmm_and_return_ret_6_1,
        ))

        delay(2500L)

        drawer.showCharacterMessages(returnC, listOf(
            R.string.lands_tmm_and_return_ret_7_1,
        ), delayAfterMessage = 0L)
        tmm.emotionState = TheMarryingMaiden.EmotionState.DETERMINED
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_tmm_and_return_tmm_8_1,
        ))
        drawer.showCharacterMessages(returnC, listOf(
            R.string.lands_tmm_and_return_ret_9_1
        ))

        tmm.emotionState = TheMarryingMaiden.EmotionState.SMILING
    }
})