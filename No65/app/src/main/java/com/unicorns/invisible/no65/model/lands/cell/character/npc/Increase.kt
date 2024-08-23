package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttackIncreaseDecrease
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import kotlinx.serialization.Serializable

@Serializable
class Increase(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 42
    override val nameId: Int
        get() = R.string.lands_increase_name

    override val centerSymbol: String
        get() = "\uD83D\uDCC8"
    override val centerSymbolColor
        get() = R.color.green
    override val faceCell: String
        get() = "\uD83D\uDE00"

    override val emotion: Emotion = Emotion.ENERGIZED

    override val attackEvent: Event
        get() = EventAttackIncreaseDecrease(isIncreaseFirst = true)

    private var linesSaid = 0
    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_increase_0_1,
                    R.string.lands_increase_0_2,
                )
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_increase_1_1,
                    R.string.lands_increase_1_2,
                    R.string.lands_increase_1_3,
                    R.string.lands_increase_1_4,
                )
            }
            else -> EventNPCSpeak(this) { R.string.lands_increase_else_1 }
        }
}