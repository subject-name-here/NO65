package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Oppression
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.coroutines.delay

class EventOppressionAppears2 : EventPlaced({ manager ->
    val appearCoordinates = Coordinates(0, 10)
    manager.wrapCutscene {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_oppression_appears_2_opp_0_1,
                R.string.lands_oppression_appears_2_opp_0_2,
                R.string.lands_oppression_appears_2_opp_0_3,
            )
        )
        delay(1000L)
        val oppressionCell = gameState.currentMap.createCellOnTop(appearCoordinates, Oppression::class)
        delay(1000L)

        oppressionCell.emotion = Emotion.FEAR
        drawer.showCharacterMessages(
            oppressionCell,
            listOf(
                R.string.lands_oppression_appears_2_opp_1_2,
                R.string.lands_oppression_appears_2_opp_1_3,
                R.string.lands_oppression_appears_2_opp_1_4,
                R.string.lands_oppression_appears_2_opp_1_5,
                R.string.lands_oppression_appears_2_opp_1_6,
            )
        )

        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_oppression_appears_2_spr_2_1,
                R.string.lands_oppression_appears_2_spr_2_2,
                R.string.lands_oppression_appears_2_spr_2_3,
            )
        )

        oppressionCell.emotion = Emotion.HOSTILITY
        drawer.showCharacterMessages(oppressionCell, listOf(
            R.string.lands_oppression_appears_2_opp_3_1,
            R.string.lands_oppression_appears_2_opp_3_2,
            R.string.lands_oppression_appears_2_opp_3_3,
            R.string.lands_oppression_appears_2_opp_3_4,
            R.string.lands_oppression_appears_2_opp_3_5,
            R.string.lands_oppression_appears_2_opp_3_6,
        ))
        delay(1000L)
        gameState.currentMap.removeCellFromTop(oppressionCell)
        delay(1000L)

        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_oppression_appears_2_spr_4_1,
                R.string.lands_oppression_appears_2_spr_4_2,
            )
        )
    }
})