package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import kotlinx.serialization.Serializable

@Serializable
class Treading(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 10
    override val nameId: Int
        get() = R.string.lands_treading_name

    override val centerSymbol: String
        get() = "\uD83D\uDC22"
    override val centerSymbolColor
        get() = R.color.green
    override val faceCell: String
        get() = "\uD83D\uDE25"

    override val emotion: Emotion = Emotion.CALMNESS

    override val attackEvent
        get() = EventNPCSpeak(this) {
            R.string.lands_treading_attack_useless_1
        }

    var state = State.SEC_4

    private var linesSaid = 0
    override val speakEvent
        get() = when (state) {
            State.SEC_4 -> EventNPCSpeak(this) {
                when (linesSaid++) {
                    0 -> R.string.lands_treading_0_1
                    1 -> R.string.lands_treading_1_1
                    2 -> R.string.lands_treading_2_1
                    else -> R.string.lands_treading_else_1
                }
            }
            State.SECRET -> when (linesSaid++) {
                0 -> {
                    EventNPCSpeak(this) {
                        R.string.lands_treading_secret_0_1
                    }
                }
                1 -> {
                    EventNPCSpeakCutscene(this, isSkippable = true) {
                        listOf(
                            R.string.lands_treading_secret_1_1,
                            R.string.lands_treading_secret_1_2,
                        )
                    }
                }
                2 -> {
                    EventNPCSpeakCutscene(this, isSkippable = true) {
                        listOf(
                            R.string.lands_treading_secret_2_1,
                            R.string.lands_treading_secret_2_2,
                        )
                    }
                }
                else -> {
                    EventNPCSpeak(this) {
                        R.string.lands_treading_secret_else_1
                    }
                }
            }
        }

    enum class State {
        SEC_4,
        SECRET,
    }
}