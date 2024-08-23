package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttackInfluenceDuration
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventDurationJoke
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import kotlinx.serialization.Serializable


@Serializable
class Duration(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 32
    override val nameId: Int
        get() = R.string.lands_duration_name

    override val centerSymbol: String
        get() = "\uD83D\uDDD3"
    override val centerSymbolColor
        get() = R.color.red

    var state = State.NORMAL
    override val faceCell: String
        get() = when (state) {
            State.NORMAL -> "\uD83E\uDDD1"
            State.LAUGHING -> "\uD83E\uDDD2"
        }

    override val emotion: Emotion = Emotion.BRAVERY

    override val attackEvent: Event
        get() = EventAttackInfluenceDuration()

    private var linesSaid = 0
    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_duration_0_1,
                    R.string.lands_duration_0_2,
                    R.string.lands_duration_0_3,
                )
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_duration_1_1,
                    R.string.lands_duration_1_2,
                    R.string.lands_duration_1_3,
                )
            }
            2 -> EventDurationJoke(this)
            else -> EventNPCSpeak(this) { R.string.lands_duration_else_1 }
        }

    enum class State {
        NORMAL,
        LAUGHING
    }
}