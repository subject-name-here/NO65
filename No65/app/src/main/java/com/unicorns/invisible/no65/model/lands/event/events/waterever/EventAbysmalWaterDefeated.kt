package com.unicorns.invisible.no65.model.lands.event.events.waterever

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.CellNonEmpty
import com.unicorns.invisible.no65.model.lands.cell.character.npc.AbysmalWater
import com.unicorns.invisible.no65.model.lands.cell.character.npc.BeforeCompletion
import com.unicorns.invisible.no65.model.lands.cell.floor.CellFloor
import com.unicorns.invisible.no65.model.lands.cell.interactive.AbysmalWaterRemains
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.SOUND_TEST_WATEREVER_COMPLETE
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import kotlinx.coroutines.delay


class EventAbysmalWaterDefeated : Event({ manager ->
    launchCoroutineOnMain {
        val newManager = LandsManager(manager.activity, manager.gameState)

        val map = newManager.gameState.currentMap
        map.getTopCells()
            .filterIsInstance<AbysmalWater>()
            .forEach {
                map.removeCellFromTop(it)
                map.createCellOnTop(it.coordinates, AbysmalWaterRemains::class)
            }

        newManager.init()
        newManager.processMap()

        val bc = map.getTopCells().filterIsInstance<BeforeCompletion>().firstOrNull() ?: return@launchCoroutineOnMain
        newManager.wrapCutscene {
            delay(1000L)

            drawer.showCharacterMessages(bc, listOf(
                R.string.lands_waterever_ending_1_1
            ))
            drawer.showMessage(
                R.string.lands_waterever_ending_announce_2_1,
                color = R.color.black,
                tapSoundId = R.raw.sfx_tap
            )
            drawer.showMessage(
                R.string.lands_waterever_ending_announce_2_2,
                color = R.color.black,
                tapSoundId = R.raw.sfx_tap,
                delayAfterMessage = 0L
            )
            drawer.showCharacterMessages(bc, listOf(
                R.string.lands_waterever_ending_3_1,
                R.string.lands_waterever_ending_3_2,
                R.string.lands_waterever_ending_3_3,
                R.string.lands_waterever_ending_3_4,
                R.string.lands_waterever_ending_3_5,
                R.string.lands_waterever_ending_3_6,
            ))

            delay(1000L)

            val protagonistCoordinates = manager.gameState.protagonist.coordinates
            (-1..1).forEach { dx ->
                (-1..1).forEach { dy ->
                    val cell = map.getTopCell(protagonistCoordinates + Coordinates(dx, dy))
                    if (cell is CellFloor) {
                        cell.litUp = false
                    } else if (cell is CellNonEmpty) {
                        val cellBelow = cell.cellBelow
                        if (cellBelow is CellFloor) {
                            cellBelow.litUp = false
                        }
                    }
                }
            }

            drawer.showCharacterMessages(bc, listOf(
                R.string.lands_waterever_ending_4_1,
                R.string.lands_waterever_ending_4_2,
                R.string.lands_waterever_ending_4_3,
            ))
            delay(1000L)
            drawer.fadeInBlack(time = 5000L)
        }
        GlobalState.putBoolean(newManager.activity, SOUND_TEST_WATEREVER_COMPLETE, true)
        newManager.activity.returnToMenu(playMusic = true)
    }
})