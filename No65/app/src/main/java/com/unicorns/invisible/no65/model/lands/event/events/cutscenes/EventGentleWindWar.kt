package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.GentleWind
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.GENTLE_WIND_WAR_DECLARED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay

class EventGentleWindWar(private val gw: GentleWind) : Event (lambda@ { manager ->
    if (GlobalState.getBoolean(manager.activity, GENTLE_WIND_WAR_DECLARED)) {
        manager.wrapCutsceneSkippable {
            drawer.showCharacterMessages(gw, listOf(
                R.string.lands_gentle_wind_war_revisited_1,
                R.string.lands_gentle_wind_war_revisited_2,
            ))
        }
        return@lambda
    }

    GlobalState.putBoolean(manager.activity, GENTLE_WIND_WAR_DECLARED, true)
    manager.activity.musicPlayer.playMusic(
        R.raw.cutscene_gentle_wind_war,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = false
    )
    manager.wrapCutscene {
        drawer.showCharacterMessages(gw, listOf(
            R.string.lands_gentle_wind_war_1,
        ))
        drawer.showCharacterMessages(gw, listOf(
            R.string.lands_gentle_wind_war_2,
        ), delayAfterMessage = 0L)
        drawer.showCharacterMessages(gw, listOf(
            R.string.lands_gentle_wind_war_3,
            R.string.lands_gentle_wind_war_4,
            R.string.lands_gentle_wind_war_5,
        ))
        activity.musicPlayer.playMusic(
            R.raw.sfx_applause,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )

        delay(500L)
        gameState.currentMap.moveOnDelta(gw, Coordinates(0, -1))
        delay(500L)
        gameState.currentMap.moveOnDelta(gw, Coordinates(0, -1))
        delay(500L)
        drawer.showCharacterMessages(gw, listOf(
            R.string.lands_gentle_wind_war_6,
            R.string.lands_gentle_wind_war_7,
            R.string.lands_gentle_wind_war_8,
            R.string.lands_gentle_wind_war_9,
            R.string.lands_gentle_wind_war_10,
            R.string.lands_gentle_wind_war_11,
        ))
        drawer.showCharacterMessages(gw, listOf(
            R.string.lands_gentle_wind_war_12,
            R.string.lands_gentle_wind_war_13,
        ), delayAfterMessage = 0L)
        drawer.showCharacterMessages(gw, listOf(
            R.string.lands_gentle_wind_war_14,
            R.string.lands_gentle_wind_war_15,
            R.string.lands_gentle_wind_war_16,
            R.string.lands_gentle_wind_war_17,
        ))
        drawer.showCharacterMessages(gw, listOf(
            R.string.lands_gentle_wind_war_18,
            R.string.lands_gentle_wind_war_18,
            R.string.lands_gentle_wind_war_18,
            R.string.lands_gentle_wind_war_18,
            R.string.lands_gentle_wind_war_18,
        ), delayAfterMessage = 0L)
        drawer.showCharacterMessages(gw, listOf(
            R.string.lands_gentle_wind_war_19,
            R.string.lands_gentle_wind_war_20,
            R.string.lands_gentle_wind_war_21,
            R.string.lands_gentle_wind_war_22,
        ))
    }
})