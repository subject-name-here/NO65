package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.SEC1_STARTED
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.THE_MARRYING_MAIDEN_LEFT_THE_PARTY
import com.unicorns.invisible.no65.model.lands.cell.character.npc.MouthCorners
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventSec1 : Event({ manager ->
    val tmm = manager.gameState.companions.filterIsInstance<TheMarryingMaiden>().firstOrNull()
    val mc = manager.gameState.currentMap.getTopCells().filterIsInstance<MouthCorners>().firstOrNull()!!
    if (tmm != null) {
        manager.wrapCutscene {
            activity.musicPlayer.playMusicSuspendTillStart(
                R.raw.cutscene_broken_heart,
                behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                isLooping = false
            )

            drawer.showCharacterMessages(mc, listOf(
                R.string.lands_sec1_event_mc_0_1,
            ))
            delay(500L)
            tmm.emotionState = TheMarryingMaiden.EmotionState.LAUGHING
            delay(500L)
            drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_sec1_event_tmm_1_1,
                R.string.lands_sec1_event_tmm_1_2,
            ))
            drawer.showCharacterMessages(mc, listOf(
                R.string.lands_sec1_event_mc_2_1,
                R.string.lands_sec1_event_mc_2_2,
                R.string.lands_sec1_event_mc_2_3,
                R.string.lands_sec1_event_mc_2_4,
            ))

            val mcWalkToTrajectory = listOf(
                Coordinates(1, -1),
                Coordinates(0, -1)
            )
            CellUtils.moveOnTrajectory(
                mcWalkToTrajectory,
                mc,
                gameState.currentMap,
                delayBetweenMoves = LandsManager.TICK_TIME_MILLISECONDS * 3
            )
            mc.state = MouthCorners.State.KISSING_CLOSED_EYES

            activity.musicPlayer.playMusic(
                R.raw.sfx_kiss,
                behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                isLooping = false
            )

            delay(500L)
            tmm.emotionState = TheMarryingMaiden.EmotionState.DISAPPOINTMENT
            delay(250L)
            drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_sec1_event_tmm_3_1,
                R.string.lands_sec1_event_tmm_3_2,
            ))
            delay(100L)
            tmm.emotionState = TheMarryingMaiden.EmotionState.SADNESS
            drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_sec1_event_tmm_4_1,
                R.string.lands_sec1_event_tmm_4_2,
            ))
            mc.state = MouthCorners.State.KISSING_OPEN_EYES
            drawer.showCharacterMessages(mc, listOf(
                R.string.lands_sec1_event_mc_5_1,
                R.string.lands_sec1_event_mc_5_2,
            ))
            mc.state = MouthCorners.State.KISSING_CLOSED_EYES
            delay(500L)
            drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_sec1_event_tmm_6_1,
                R.string.lands_sec1_event_tmm_6_2,
            ))
            tmm.emotionState = TheMarryingMaiden.EmotionState.CRYING
            tmm.heartState = TheMarryingMaiden.HeartState.BROKEN
            drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_sec1_event_tmm_6_3,
            ), delayAfterMessage = 0L)

            val mmEscapeTrajectory = listOf(
                Coordinates(1, 1),
                Coordinates(1, 3),
                Coordinates(1, 5),
            )
            CellUtils.moveOnTrajectory(
                mmEscapeTrajectory,
                tmm,
                gameState.currentMap,
                delayBetweenMoves = LandsManager.TICK_TIME_MILLISECONDS * 3
            )
            tmm.companionState = TheMarryingMaiden.CompanionState.AFTER_COMPANIONSHIP
            gameState.currentMap.removeCellFromTop(tmm)
            gameState.flagsMaster.add(THE_MARRYING_MAIDEN_LEFT_THE_PARTY)

            delay(500L)

            mc.state = MouthCorners.State.NORMAL
            drawer.showCharacterMessages(mc, listOf(
                R.string.lands_sec1_event_mc_7_1,
                R.string.lands_sec1_event_mc_7_2,
                R.string.lands_sec1_event_mc_7_3,
            ))

            val mcWalkAwayTrajectory = listOf(
                Coordinates(0, -1),
                Coordinates(1, -1),
                Coordinates(2, -1),
                Coordinates(2, -2),
                Coordinates(2, -3),
                Coordinates(2, -4),
                Coordinates(3, -4),
            )
            CellUtils.moveOnTrajectory(
                mcWalkAwayTrajectory,
                mc,
                gameState.currentMap,
                delayBetweenMoves = LandsManager.TICK_TIME_MILLISECONDS * 10
            )
            delay(500L)
        }
    }

    manager.gameState.currentMap.removeCellFromTop(mc)
    manager.gameState.flagsMaster.add(SEC1_STARTED)
    manager.activity.musicPlayer.playMusic(
        manager.getCurrentMapMusicThemeId(),
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = true
    )
})