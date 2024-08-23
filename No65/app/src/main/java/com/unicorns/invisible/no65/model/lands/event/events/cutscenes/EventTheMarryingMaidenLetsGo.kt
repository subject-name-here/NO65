package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.Event


class EventTheMarryingMaidenLetsGo(tmm: TheMarryingMaiden) : Event({ manager ->
    manager.wrapCutscene {
        activity.musicPlayer.pauseAllMusic()

        tmm.emotionState = TheMarryingMaiden.EmotionState.DETERMINED
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_lets_go_1,
            R.string.lands_the_marrying_maiden_lets_go_2,
        ))
        tmm.emotionState = TheMarryingMaiden.EmotionState.SADNESS
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_lets_go_3,
        ))
        tmm.emotionState = TheMarryingMaiden.EmotionState.CRYING
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_lets_go_4,
            R.string.lands_the_marrying_maiden_lets_go_5,
        ))
        tmm.emotionState = TheMarryingMaiden.EmotionState.DISAPPOINTMENT
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_lets_go_6,
            R.string.lands_the_marrying_maiden_lets_go_7,
        ))
        tmm.emotionState = TheMarryingMaiden.EmotionState.CRYING
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_lets_go_8,
        ))

        activity.musicPlayer.resumeAllMusic()
    }
})