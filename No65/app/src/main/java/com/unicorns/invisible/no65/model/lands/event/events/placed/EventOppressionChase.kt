package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Oppression
import com.unicorns.invisible.no65.model.lands.cell.floor.CellFloor
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.OPPRESSION_CHASE_FIRED
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.view.dpad.CircleDPadMk2
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventOppressionChase : EventPlaced({ manager ->
    val appearCoordinates = Coordinates(-1, 10)
    val hasBeenChasedAlready = GlobalState.getBoolean(manager.activity, OPPRESSION_CHASE_FIRED, false)

    suspend fun LandsManager.cutscene() {
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_oppression_chase_opp_0_1,
            R.string.lands_oppression_chase_opp_0_2,
        ))
        delay(500L)
        val oppressionCell = manager.gameState.currentMap.createCellOnTop(appearCoordinates, Oppression::class)
        delay(500L)

        oppressionCell.emotion = Emotion.ANGER
        drawer.showCharacterMessages(oppressionCell, listOf(
            R.string.lands_oppression_chase_opp_1_1,
            R.string.lands_oppression_chase_opp_1_2,
            R.string.lands_oppression_chase_opp_1_3,
        ))

        manager.gameState.currentMap.removeCellFromTop(oppressionCell)
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_oppression_chase_spr_2_1,
                R.string.lands_oppression_chase_spr_2_2,
            ),
            delayAfterMessage = 0L
        )
    }

    if (hasBeenChasedAlready) {
        manager.wrapCutsceneSkippable {
            cutscene()
        }
    } else {
        manager.wrapCutscene {
            cutscene()
        }
    }

    if (!hasBeenChasedAlready) {
        GlobalState.putBoolean(manager.activity, OPPRESSION_CHASE_FIRED, true)
    }

    manager.activity.musicPlayer.playMusic(
        R.raw.cutscene_chase,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = false
    )

    val rows = listOf(
        -1,
        2, 3, 4, 5
    )
    val startCol = 6
    val endCol = 48
    val dynamicDelay = CircleDPadMk2.DELAY_MILLISECONDS * 3 / 4

    launchCoroutine {
        for (col in (startCol..endCol)) {
            for (row in rows) {
                val coordinates = Coordinates(row, col)
                val cell = manager.gameState.currentMap.getTopCell(coordinates)
                if (cell is CellFloor) {
                    manager.gameState.currentMap.removeCellFromTop(cell)
                }
            }
            delay(dynamicDelay)
        }
    }
})