package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldTheWell
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class TheWell(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 48
    override val nameId: Int
        get() = R.string.lands_the_well_name

    override val centerSymbol: String
        get() = "\uD83D\uDEC8"
    override val centerSymbolColor
        get() = R.color.blue
    override val faceCell: String
        get() = "\uD83D\uDC37"

    override val emotion: Emotion = Emotion.INDIFFERENCE

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldTheWell(),
        onAfterVictoryEvent = EventUnlockBattle(49)
    )

    private var linesSaid = 0
    override val speakEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_the_well_0_1,
                    R.string.lands_the_well_0_2,
                    R.string.lands_the_well_0_3,
                )
            }
            3 -> EventNPCSpeak(this) { R.string.lands_the_well_3_1 }
            4 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_the_well_4_1,
                    R.string.lands_the_well_4_2,
                    R.string.lands_the_well_4_3,
                    R.string.lands_the_well_4_4,
                    R.string.lands_the_well_4_5,
                    R.string.lands_the_well_4_6,
                    R.string.lands_the_well_4_7,
                    R.string.lands_the_well_4_8,
                    R.string.lands_the_well_4_9,
                    R.string.lands_the_well_4_10,
                    R.string.lands_the_well_4_11,
                    R.string.lands_the_well_4_12,
                    R.string.lands_the_well_4_13,
                )
            }
            7 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_the_well_7_1,
                    R.string.lands_the_well_7_2,
                    R.string.lands_the_well_7_3,
                    R.string.lands_the_well_7_4,
                    R.string.lands_the_well_7_5,
                    R.string.lands_the_well_7_6,
                )
            }
            else -> EventNPCSpeak(this) { R.string.lands_the_well_else_1 }
        }
}