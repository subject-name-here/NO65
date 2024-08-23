package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.ALIEN_SPACESHIP_TO_LEAVE
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.floor.CellFloorAlmostWhite
import com.unicorns.invisible.no65.model.lands.cell.interactive.CellMoney
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay

class EventSpaceshipToLeave : EventPlaced(lambda@ { manager ->
    val map = manager.gameState.mapGraph.getMap("map_sec3")
    val teleportCoordinates = Coordinates(10, 3)
    if (map.getTopCell(teleportCoordinates) is TeleportCell) {
        manager.wrapCutsceneSkippable {
            activity.musicPlayer.playMusic(
                R.raw.sfx_spaceship_leaving,
                behaviour = MusicPlayer.MusicBehaviour.PAUSE_ALL,
                isLooping = false
            )
            delay(2000L)
            map.clearCell(teleportCoordinates)
            delay(2000L)
            map.createCellOnTop(teleportCoordinates, CellFloorAlmostWhite::class)
            delay(2000L)
            map.createCellOnTop(teleportCoordinates, CellMoney::class)

            activity.musicPlayer.resumeAllMusic()
        }
    }
}) {
    override fun getConditionToFire(manager: LandsManager): Boolean {
        return firingCell is CellProtagonist && ALIEN_SPACESHIP_TO_LEAVE in manager.gameState.flagsMaster
    }
}