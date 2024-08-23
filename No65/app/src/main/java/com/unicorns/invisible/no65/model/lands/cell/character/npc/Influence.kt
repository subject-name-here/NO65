package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttackInfluenceDuration
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import kotlinx.serialization.Serializable

@Serializable
class Influence(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 31
    override val nameId: Int
        get() = R.string.lands_influence_name

    override val centerSymbol: String
        get() = "\uD83D\uDDBC"
    override val centerSymbolColor
        get() = R.color.yellow
    override val faceCell: String
        get() = "\uD83D\uDC73"

    override val emotion: Emotion = Emotion.CALMNESS

    override val attackEvent: Event
        get() = EventAttackInfluenceDuration()

    private var linesSaid = 0
    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_influence_0_1,
                    R.string.lands_influence_0_2,
                    R.string.lands_influence_0_3,
                )
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_influence_1_1,
                    R.string.lands_influence_1_2,
                    R.string.lands_influence_1_3,
                    R.string.lands_influence_1_4,
                )
            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_influence_2_1,
                    R.string.lands_influence_2_2,
                    R.string.lands_influence_2_3,
                )
            }
            3 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_influence_3_1,
                    R.string.lands_influence_3_2,
                    R.string.lands_influence_3_3,
                )
            }
            4 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_influence_4_1,
                    R.string.lands_influence_4_2,
                )
            }
            else -> EventNPCSpeak(this) { R.string.lands_influence_else_1 }
        }
}