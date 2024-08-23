package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.Event
import kotlinx.coroutines.delay


class EventTheMarryingMaidenSeriousTalk(theMM: TheMarryingMaiden) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        theMM.emotionState = TheMarryingMaiden.EmotionState.DISAPPOINTMENT
        drawer.showCharacterMessages(theMM, listOf(
            R.string.lands_the_marrying_maiden_talk_1,
            R.string.lands_the_marrying_maiden_talk_2,
            R.string.lands_the_marrying_maiden_talk_3,
        ))
        theMM.emotionState = TheMarryingMaiden.EmotionState.LAUGHING
        drawer.showCharacterMessages(theMM, listOf(
            R.string.lands_the_marrying_maiden_talk_4,
        ))
        theMM.emotionState = TheMarryingMaiden.EmotionState.GLAD
        drawer.showCharacterMessages(theMM, listOf(
            R.string.lands_the_marrying_maiden_talk_5,
        ))
        delay(500L)
        theMM.emotionState = TheMarryingMaiden.EmotionState.DISAPPOINTMENT
        drawer.showCharacterMessages(theMM, listOf(
            R.string.lands_the_marrying_maiden_talk_6,
            R.string.lands_the_marrying_maiden_talk_7,
            R.string.lands_the_marrying_maiden_talk_8,
            R.string.lands_the_marrying_maiden_talk_9,
        ))
        theMM.emotionState = TheMarryingMaiden.EmotionState.CRYING
        drawer.showCharacterMessages(theMM, listOf(
            R.string.lands_the_marrying_maiden_talk_10,
            R.string.lands_the_marrying_maiden_talk_11,
            R.string.lands_the_marrying_maiden_talk_12,
        ))
        theMM.emotionState = TheMarryingMaiden.EmotionState.SADNESS
        drawer.showCharacterMessages(theMM, listOf(
            R.string.lands_the_marrying_maiden_talk_13,
            R.string.lands_the_marrying_maiden_talk_14,
        ))
        delay(1000L)
        theMM.emotionState = TheMarryingMaiden.EmotionState.SMILING
        drawer.showCharacterMessages(theMM, listOf(
            R.string.lands_the_marrying_maiden_talk_15,
            R.string.lands_the_marrying_maiden_talk_16,
            R.string.lands_the_marrying_maiden_talk_17,
            R.string.lands_the_marrying_maiden_talk_18,
            R.string.lands_the_marrying_maiden_talk_19,
            R.string.lands_the_marrying_maiden_talk_20,
        ))
    }
})