package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.RegisteredCounters.Companion.MONEYS_STOLEN_BY_THE_WANDERER
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.THE_WANDERER_STOLE_MONEYS
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheMarryingMaiden
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheWanderer
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.view.Lands65Drawer
import com.unicorns.invisible.no65.view.speech.SpeechProperties


class EventTheWandererStealsMoneys(theWanderer: TheWanderer) : Event(lambda@ { manager ->
    if (manager.gameState !is GameState65) return@lambda

    manager.wrapCutscene {
        drawer.showCharacterMessages(theWanderer, listOf(
            R.string.lands_the_wanderer_steals_moneys_1,
            R.string.lands_the_wanderer_steals_moneys_2,
            R.string.lands_the_wanderer_steals_moneys_3,
            R.string.lands_the_wanderer_steals_moneys_4,
        ), delayAfterMessage = SpeechProperties.DELAY_AFTER_MESSAGE / 4)
        drawer.showCharacterMessages(theWanderer, listOf(
            R.string.lands_the_wanderer_steals_moneys_5,
            R.string.lands_the_wanderer_steals_moneys_6,
        ), delayAfterMessage = 0L)

        with(gameState as GameState65) {
            countersMaster[MONEYS_STOLEN_BY_THE_WANDERER] = protagonist.moneys
            protagonist.moneys = 0
            (drawer as Lands65Drawer).drawStats(protagonist)
        }

        val trajectory = listOf(
            Coordinates(11, 11),
            Coordinates(12, 11),
            Coordinates(13, 11),
            Coordinates(14, 11),
            Coordinates(14, 10),
            Coordinates(14, 9),
            Coordinates(14, 8),
            Coordinates(14, 7),
            Coordinates(14, 6),
            Coordinates(14, 5),
            Coordinates(14, 4),
            Coordinates(14, 3),
            Coordinates(14, 2),
        )

        CellUtils.moveOnTrajectory(
            trajectory,
            theWanderer,
            gameState.currentMap,
            delayBetweenMoves = LandsManager.TICK_TIME_MILLISECONDS * 3
        )

        manager.gameState.currentMap.removeCellFromTop(theWanderer)

        val tmm = gameState.companions.filterIsInstance<TheMarryingMaiden>().firstOrNull()
        if (tmm != null) {
            tmm.emotionState = TheMarryingMaiden.EmotionState.LAUGHING
            drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_the_wanderer_steals_moneys_tmm_1,
            ))
            tmm.emotionState = TheMarryingMaiden.EmotionState.DETERMINED
            drawer.showCharacterMessages(tmm, listOf(
                R.string.lands_the_wanderer_steals_moneys_tmm_2,
            ))
            tmm.emotionState = TheMarryingMaiden.EmotionState.SMILING
        }

        gameState.flagsMaster.add(THE_WANDERER_STOLE_MONEYS)
    }
})