package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.coroutines.delay

class EventTheMarryingMaidenHome(
    tmm: TheMarryingMaiden,
    isVisit: Boolean,
    isClean: Boolean
) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        if (isVisit) {
            tmm.emotionState = TheMarryingMaiden.EmotionState.DISAPPOINTMENT
            drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_the_marrying_maiden_home_1,
                R.string.lands_the_marrying_maiden_home_2,
                R.string.lands_the_marrying_maiden_home_3,
            ))

            tmm.emotionState = TheMarryingMaiden.EmotionState.SMILING
            manager.drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_the_marrying_maiden_home_4,
            ))
        }

        if (isVisit && isClean) {
            drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_the_marrying_maiden_home_5,
                R.string.lands_the_marrying_maiden_home_6,
            ))
            delay(500L)
        }

        if (isClean) {
            tmm.emotionState = TheMarryingMaiden.EmotionState.LOVE
            manager.drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_the_marrying_maiden_home_7,
                R.string.lands_the_marrying_maiden_home_8,
            ))
            tmm.emotionState = TheMarryingMaiden.EmotionState.SMILING
        }
    }
})