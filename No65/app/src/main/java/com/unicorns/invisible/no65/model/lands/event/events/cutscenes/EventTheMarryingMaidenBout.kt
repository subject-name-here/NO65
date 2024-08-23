package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_MM_CUTSCENE_PLAYED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay

class EventTheMarryingMaidenBout(tmm: TheMarryingMaiden) : Event({ manager ->
    val cutsceneAlreadyPlayed = GlobalState.getBoolean(manager.activity, THE_MM_CUTSCENE_PLAYED)
    if (cutsceneAlreadyPlayed) {
        tmm.emotionState = TheMarryingMaiden.EmotionState.DETERMINED
        manager.wrapCutscene {
            activity.musicPlayer.stopAllMusic()
            drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_the_marrying_maiden_bout_revisited_1,
                R.string.lands_the_marrying_maiden_bout_revisited_2,
            ))
        }
    } else {
        manager.activity.musicPlayer.playMusicSuspendTillStart(
            R.raw.cutscene_the_mm_bout,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )
        manager.wrapCutscene {
            drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_the_marrying_maiden_bout_1,
                R.string.lands_the_marrying_maiden_bout_2,
                R.string.lands_the_marrying_maiden_bout_3,
                R.string.lands_the_marrying_maiden_bout_4,
                R.string.lands_the_marrying_maiden_bout_5,
                R.string.lands_the_marrying_maiden_bout_6,
            ))

            delay(5000L)
            tmm.emotionState = TheMarryingMaiden.EmotionState.SADNESS
            drawer.showTalkingHead(tmm)
            drawer.showMessages(listOf(
                R.string.lands_the_marrying_maiden_bout_7,
                R.string.lands_the_marrying_maiden_bout_8,
                R.string.lands_the_marrying_maiden_bout_9,
            ), color = tmm.speechColor, tapSoundId = tmm.speechSound)
            tmm.emotionState = TheMarryingMaiden.EmotionState.DISAPPOINTMENT
            drawer.updateTalkingHeadFace(tmm)
            drawer.showMessages(listOf(
                R.string.lands_the_marrying_maiden_bout_10,
                R.string.lands_the_marrying_maiden_bout_11,
            ), color = tmm.speechColor, tapSoundId = tmm.speechSound)
            tmm.emotionState = TheMarryingMaiden.EmotionState.DETERMINED
            drawer.updateTalkingHeadFace(tmm)
            drawer.showMessages(listOf(
                R.string.lands_the_marrying_maiden_bout_12,
                R.string.lands_the_marrying_maiden_bout_13,
                R.string.lands_the_marrying_maiden_bout_14,
                R.string.lands_the_marrying_maiden_bout_15,
                R.string.lands_the_marrying_maiden_bout_16,
                R.string.lands_the_marrying_maiden_bout_17,
                R.string.lands_the_marrying_maiden_bout_18,
                R.string.lands_the_marrying_maiden_bout_19,
            ), color = tmm.speechColor, tapSoundId = tmm.speechSound)
            drawer.hideTalkingHead()
        }

        GlobalState.putBoolean(manager.activity, THE_MM_CUTSCENE_PLAYED, true)
    }
})