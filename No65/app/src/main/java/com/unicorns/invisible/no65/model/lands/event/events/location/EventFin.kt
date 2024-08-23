package com.unicorns.invisible.no65.model.lands.event.events.location

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.ENTERED_FINALE
import com.unicorns.invisible.no65.model.lands.cell.character.npc.AfterCompletion
import com.unicorns.invisible.no65.model.lands.cell.floor.CellFloorWhite
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventFin : Event(lambda@ { manager ->
    manager.gameState.flagsMaster.add(ENTERED_FINALE)
    (manager.gameState as GameState65).rewindAvailable = false

    val ac = manager.gameState.currentMap.getTopCells()
        .filterIsInstance<AfterCompletion>()
        .firstOrNull() ?: return@lambda

    manager.activity.musicPlayer.playMusic(
        R.raw.location_fin,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = false
    )

    val passageCoordinates = Coordinates(0, -10)
    manager.wrapCutscene {
        drawer.showUnknownCharacterMessages(ac, listOf(
            R.string.lands_after_completion_on_eow_entrance_1,
            R.string.lands_after_completion_on_eow_entrance_2,
            R.string.lands_after_completion_on_eow_entrance_3,
        ))
    }

    launchCoroutine {
        delay(60000)
        if (manager.stopped) return@launchCoroutine

        manager.gameState.currentMap.clearCell(passageCoordinates)
        manager.gameState.currentMap.createCellOnTop(passageCoordinates, CellFloorWhite::class)

        manager.wrapCutscene {
            drawer.showUnknownCharacterMessages(ac, listOf(
                R.string.lands_after_completion_on_eow_passage_appeared_1,
                R.string.lands_after_completion_on_eow_passage_appeared_2,
                R.string.lands_after_completion_on_eow_passage_appeared_3,
                R.string.lands_after_completion_on_eow_passage_appeared_4,
            ))
        }
    }
})