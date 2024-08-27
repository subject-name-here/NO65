package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


class EventTheMarryingMaiden(val coordinates: Coordinates) : Event({ manager ->
    manager.activity.musicPlayer.playMusicSuspendTillStart(
        R.raw.cutscene_the_marrying_maiden,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = false
    )
    manager.wrapCutscene {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_the_marrying_maiden_appears_rev_0_1
        ))

        delay(500L)
        CellUtils.litUpCurrentMapFloors(this)
        val tmm = gameState.currentMap.createCellOnTop(coordinates, TheMarryingMaiden::class)
        delay(500L)

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_the_marrying_maiden_appears_rev_0_2
        ))

        delay(1000L)

        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_appears_tmm_1_1,
            R.string.lands_the_marrying_maiden_appears_tmm_1_2,
            R.string.lands_the_marrying_maiden_appears_tmm_1_3,
            R.string.lands_the_marrying_maiden_appears_tmm_1_4,
        ))

        delay(500L)

        tmm.emotionState = TheMarryingMaiden.EmotionState.SMILING
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_appears_tmm_1_5,
            R.string.lands_the_marrying_maiden_appears_tmm_1_6,
        ))

        delay(100L)
        tmm.emotionState = TheMarryingMaiden.EmotionState.LOVE
        delay(100L)

        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_appears_tmm_1_7
        ))
        val job = launchCoroutine {
            while (isActive) {
                tmm.rotation += 4f
                delay(LandsManager.TICK_TIME_MILLISECONDS)
            }
        }

        delay(500L)
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_the_marrying_maiden_appears_rev_2_1,
        ), delayAfterMessage = 0L)

        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_appears_tmm_3_1,
        ))

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_the_marrying_maiden_appears_rev_4_1,
            R.string.lands_the_marrying_maiden_appears_rev_4_2,
        ))
        job.cancel()

        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_appears_tmm_5_1,
        ))

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_the_marrying_maiden_appears_rev_6_1
        ))

        tmm.rotation = 0f
        tmm.emotionState = TheMarryingMaiden.EmotionState.SMILING
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_appears_tmm_7_1,
            R.string.lands_the_marrying_maiden_appears_tmm_7_2,
        ))

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_the_marrying_maiden_appears_rev_8_1
        ))

        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_appears_tmm_9_1,
            R.string.lands_the_marrying_maiden_appears_tmm_9_2,
        ))

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_the_marrying_maiden_appears_rev_10_1,
            R.string.lands_the_marrying_maiden_appears_rev_10_2,
        ))

        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_appears_tmm_11_1,
            R.string.lands_the_marrying_maiden_appears_tmm_11_2,
            R.string.lands_the_marrying_maiden_appears_tmm_11_3,
            R.string.lands_the_marrying_maiden_appears_tmm_11_4,
        ))

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_the_marrying_maiden_appears_rev_12_1,
            R.string.lands_the_marrying_maiden_appears_rev_12_2,
            R.string.lands_the_marrying_maiden_appears_rev_12_3,
            R.string.lands_the_marrying_maiden_appears_rev_12_4,
            R.string.lands_the_marrying_maiden_appears_rev_12_5,
        ))

        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_appears_tmm_13_1,
        ))

        delay(1500L)

        tmm.emotionState = TheMarryingMaiden.EmotionState.GLAD
        drawer.showCharacterMessages(tmm, listOf(
            R.string.lands_the_marrying_maiden_appears_tmm_13_2,
        ))

        drawer.showMessage(
            R.string.lands_the_marrying_maiden_appears_joins_the_party,
            color = R.color.black,
            tapSoundId = R.raw.sfx_tap
        )
        tmm.emotionState = TheMarryingMaiden.EmotionState.SMILING
        tmm.autoFollow = true
        tmm.companionState = TheMarryingMaiden.CompanionState.DURING_COMPANIONSHIP
    }

    manager.activity.musicPlayer.playMusic(
        manager.getCurrentMapMusicThemeId(),
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = true
    )
})