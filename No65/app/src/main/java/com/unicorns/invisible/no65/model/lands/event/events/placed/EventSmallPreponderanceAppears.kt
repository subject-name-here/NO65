package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.MoveMode
import com.unicorns.invisible.no65.model.lands.RegisteredCounters.Companion.SP_TELEPORT_COUNT
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.npc.SmallPreponderance
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.util.CellUtils
import kotlinx.coroutines.delay

class EventSmallPreponderanceAppears : EventPlaced(lambda@ { manager ->
    val gameState = manager.gameState
    if (gameState !is GameState65) return@lambda

    manager.wrapCutscene {
        val messages = listOf(
            R.string.lands_small_preponderance_appears_1,
            R.string.lands_small_preponderance_appears_2,
            R.string.lands_small_preponderance_appears_3,
        )
        val messagesAfterAppearance = listOf(
            R.string.lands_small_preponderance_appears_4,
            R.string.lands_small_preponderance_appears_5,
            R.string.lands_small_preponderance_appears_6,
            R.string.lands_small_preponderance_appears_7,
            R.string.lands_small_preponderance_appears_8,
            R.string.lands_small_preponderance_appears_9,
            R.string.lands_small_preponderance_appears_10,
            R.string.lands_small_preponderance_appears_11,
            R.string.lands_small_preponderance_appears_12,
        )

        drawer.showMessagesPhoneWithUnknownHead(messages)

        delay(500L)

        val protagonist = gameState.protagonist
        val coordinatesToAppear = CellUtils.findCurrentMapClosestFloor(this, protagonist)!!
        val cell = gameState.currentMap.createCellOnTop(coordinatesToAppear, SmallPreponderance::class)

        delay(1000L)
        drawer.showCharacterMessages(cell, messagesAfterAppearance)
        gameState.moveMode = MoveMode.WALK

        gameState.protagonist.youState = CellProtagonist.YouState.CREEP
    }
}) {
    override fun getConditionToFire(manager: LandsManager): Boolean {
        return firingCell is CellProtagonist && manager.gameState.countersMaster.preInc(SP_TELEPORT_COUNT) == 3
    }
}