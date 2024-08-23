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
class Decrease(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 41
    override val nameId: Int
        get() = R.string.lands_decrease_name

    override val centerSymbol: String
        get() = "\uD83D\uDCC9"
    override val centerSymbolColor
        get() = R.color.dark_red
    override val faceCell: String
        get() = "\uD83E\uDD28"

    override val emotion: Emotion = Emotion.SMALL_INTEREST

    override val attackEvent: Event
        get() = EventAttackIncreaseDecrease(isIncreaseFirst = false)

    private var linesSaid = 0
    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_decrease_0_1,
                    R.string.lands_decrease_0_2,
                    R.string.lands_decrease_0_3,
                )
            }
            else -> EventNPCSpeak(this) { R.string.lands_decrease_else_1 }
        }
}