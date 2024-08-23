package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.GENTLE_WIND_ENCOUNTERED_SEC3
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.OPPOSITION_CONTRACT_IN_PLACE
import com.unicorns.invisible.no65.model.lands.cell.character.npc.GentleWind
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Peace
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventSec3 : Event(lambda@ { manager ->
    val peace = manager.gameState.currentMap.getTopCells().filterIsInstance<Peace>().firstOrNull()
    if (peace != null) {
        if (OPPOSITION_CONTRACT_IN_PLACE in manager.gameState.flagsMaster) {
            manager.gameState.currentMap.removeCellFromTop(peace)
        }
    }

    manager.wrapCutscene {
        drawer.showMessagesPhoneWithUnknownHead(listOf(R.string.lands_sec3_event_1))

        delay(500L)
        val closestCell = CellUtils.findCurrentMapClosestFloor(this, gameState.protagonist)!!
        val gentleWind = gameState.currentMap.createCellOnTop(closestCell, GentleWind::class)
        activity.musicPlayer.playMusicSuspendTillEnd(
            R.raw.sfx_fanfares,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
        delay(500L)
        activity.musicPlayer.playMusicSuspendTillStart(
            R.raw.cutscene_gentle_wind_sec3,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )

        drawer.showCharacterMessages(gentleWind, listOf(
            R.string.lands_sec3_event_2,
            R.string.lands_sec3_event_3,
            R.string.lands_sec3_event_4,
            R.string.lands_sec3_event_5,
            R.string.lands_sec3_event_6,
            R.string.lands_sec3_event_7,
            R.string.lands_sec3_event_8,
            R.string.lands_sec3_event_9,
            R.string.lands_sec3_event_10,
            R.string.lands_sec3_event_11,
            R.string.lands_sec3_event_12,
            R.string.lands_sec3_event_13,
            R.string.lands_sec3_event_14,
            R.string.lands_sec3_event_15,
            R.string.lands_sec3_event_16,
            R.string.lands_sec3_event_17,
            R.string.lands_sec3_event_18,
            R.string.lands_sec3_event_19,
        ))

        activity.musicPlayer.playMusicSuspendTillEnd(
            R.raw.sfx_fanfares,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )
        gameState.currentMap.removeCellFromTop(gentleWind)

        delay(500L)
    }

    manager.gameState.flagsMaster.add(GENTLE_WIND_ENCOUNTERED_SEC3)
    manager.activity.musicPlayer.playMusic(
        manager.getCurrentMapMusicThemeId(),
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = true
    )
})