package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.Event


class EventTheMarryingMaidenBackstab(tmm: TheMarryingMaiden) : Event({ manager ->
    manager.wrapCutscene {
        tmm.emotionState = TheMarryingMaiden.EmotionState.DISAPPOINTMENT
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_bout_after_let_go_1,
            R.string.lands_the_marrying_maiden_bout_after_let_go_2,
        ))
        tmm.emotionState = TheMarryingMaiden.EmotionState.SADNESS
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_bout_after_let_go_3,
            R.string.lands_the_marrying_maiden_bout_after_let_go_4,
            R.string.lands_the_marrying_maiden_bout_after_let_go_5,
        ))
        tmm.emotionState = TheMarryingMaiden.EmotionState.DETERMINED
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_bout_after_let_go_6,
        ))
    }
})