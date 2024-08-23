package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Oppression
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.coroutines.delay

class EventOppressionAppears : EventPlaced({ manager ->
    val appearCoordinates = Coordinates(14, 0)
    manager.wrapCutscene {
        with(drawer) {
            showUnknownTalkingHead()
            showMessages(
                listOf(R.string.lands_oppression_appears_opp_0_1),
                color = R.color.black,
                tapSoundId = R.raw.sfx_phone,
                delayAfterMessage = 0L
            )

            showMessages(
                listOf(
                    R.string.lands_oppression_appears_opp_0_2,
                    R.string.lands_oppression_appears_opp_0_3,
                    R.string.lands_oppression_appears_opp_0_4,
                ),
                color = R.color.black,
                tapSoundId = R.raw.sfx_phone
            )

            showMessages(
                listOf(
                    R.string.lands_oppression_appears_opp_0_5,
                    R.string.lands_oppression_appears_opp_0_6,
                ),
                color = R.color.black,
                tapSoundId = R.raw.sfx_phone,
                delayAfterMessage = 0L
            )
            hideTalkingHead()
        }
        delay(1000L)

        val oppressionCell = gameState.currentMap.createCellOnTop(appearCoordinates, Oppression::class)

        delay(500L)
        gameState.currentMap.clearCell(appearCoordinates + Coordinates(1, 0))
        delay(500L)
        gameState.currentMap.clearCell(appearCoordinates + Coordinates(2, 0))
        delay(1000L)

        drawer.showCharacterMessages(oppressionCell, listOf(
            R.string.lands_oppression_appears_opp_1_1,
            R.string.lands_oppression_appears_opp_1_2,
            R.string.lands_oppression_appears_opp_1_3,
            R.string.lands_oppression_appears_opp_1_4,
            R.string.lands_oppression_appears_opp_1_5,
        ))

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_oppression_appears_spr_2_1,
            R.string.lands_oppression_appears_spr_4_1,
        ))
        drawer.showCharacterMessages(oppressionCell, listOf(
            R.string.lands_oppression_appears_opp_5_1,
            R.string.lands_oppression_appears_opp_5_2,
            R.string.lands_oppression_appears_opp_5_3,
        ))
        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_oppression_appears_spr_6_1,
        ))
        drawer.showCharacterMessages(oppressionCell, listOf(
            R.string.lands_oppression_appears_opp_7_1,
            R.string.lands_oppression_appears_opp_7_2,
            R.string.lands_oppression_appears_opp_7_3,
            R.string.lands_oppression_appears_opp_7_4,
        ))

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_oppression_appears_spr_8_1,
            R.string.lands_oppression_appears_spr_8_2,
            R.string.lands_oppression_appears_spr_8_3,
        ))
        oppressionCell.emotion = Emotion.FEAR
        with(drawer) {
            showTalkingHead(oppressionCell)
            showMessages(
                listOf(
                    R.string.lands_oppression_appears_opp_9_1,
                    R.string.lands_oppression_appears_opp_9_2,
                    R.string.lands_oppression_appears_opp_9_3,
                    R.string.lands_oppression_appears_opp_9_4,
                    R.string.lands_oppression_appears_opp_9_5,
                ),
                color = oppressionCell.speechColor,
                tapSoundId = oppressionCell.speechSound,
                delayAfterMessage = 0L
            )
            oppressionCell.emotion = Emotion.HOSTILITY
            showMessages(
                listOf(
                    R.string.lands_oppression_appears_opp_10_1,
                    R.string.lands_oppression_appears_opp_10_2,
                    R.string.lands_oppression_appears_opp_10_3,
                ),
                color = oppressionCell.speechColor,
                tapSoundId = oppressionCell.speechSound,
            )
            hideTalkingHead()
        }
        delay(1000L)
        gameState.currentMap.removeCellFromTop(oppressionCell)
        delay(1000L)

        drawer.showMessagesPhoneWithUnknownHead(listOf(
            R.string.lands_oppression_appears_spr_11_1,
        ))

        (gameState.protagonist as CellProtagonist).youState = CellProtagonist.YouState.INTRUDER
    }
})