package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Oppression
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event


class EventOppressionSpeech(private val oppressionCell: Oppression) : Event({ manager ->
    manager.wrapCutscene {
        with(drawer) {
            oppressionCell.emotion = Emotion.BRAVERY
            showTalkingHead(oppressionCell)
            showMessages(
                listOf(
                    R.string.lands_oppression_speech_1,
                    R.string.lands_oppression_speech_2,
                    R.string.lands_oppression_speech_3,
                    R.string.lands_oppression_speech_4,
                    R.string.lands_oppression_speech_5,
                    R.string.lands_oppression_speech_6,
                ),
                oppressionCell.speechColor,
                oppressionCell.speechSound
            )
            oppressionCell.emotion = Emotion.ANGER
            showMessages(
                listOf(
                    R.string.lands_oppression_speech_7,
                    R.string.lands_oppression_speech_8,
                    R.string.lands_oppression_speech_9,
                    R.string.lands_oppression_speech_10,
                    R.string.lands_oppression_speech_11,
                    R.string.lands_oppression_speech_12,
                ),
                oppressionCell.speechColor,
                oppressionCell.speechSound
            )
            oppressionCell.emotion = Emotion.BRAVERY
            showMessages(
                listOf(
                    R.string.lands_oppression_speech_13,
                    R.string.lands_oppression_speech_14,
                    R.string.lands_oppression_speech_15,
                    R.string.lands_oppression_speech_16,
                    R.string.lands_oppression_speech_17,
                    R.string.lands_oppression_speech_18,
                ),
                oppressionCell.speechColor,
                oppressionCell.speechSound
            )
            hideTalkingHead()

            showMessagesPhoneWithUnknownHead(
                listOf(
                    R.string.lands_oppression_speech_sp_1,
                    R.string.lands_oppression_speech_sp_2,
                    R.string.lands_oppression_speech_sp_3,
                    R.string.lands_oppression_speech_sp_4,
                    R.string.lands_oppression_speech_sp_5,
                    R.string.lands_oppression_speech_sp_6,
                )
            )
        }
    }
})