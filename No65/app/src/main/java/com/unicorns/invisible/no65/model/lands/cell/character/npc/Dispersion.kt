package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldDispersion
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventDispersionBreakdown
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class Dispersion(override var cellBelow: Cell) : CellNPCStandard() {
    override val nameId: Int
        get() = R.string.lands_dispersion_name
    override val id: Int
        get() = 59

    override val faceCell: String
        get() = "\uD83C\uDF83"
    override val centerSymbol: String
        get() = "\uD83C\uDFF6"
    override val centerSymbolColor: Int
        get() = R.color.true_yellow

    override val speechSound: Int
        get() = R.raw.sfx_low_speech

    var state = State.NORMAL
    override val emotion: Emotion
        get() = when (state) {
            State.NORMAL -> Emotion.ENERGIZED
            State.SMALL_INTEREST -> Emotion.SMALL_INTEREST
            State.FEAR -> Emotion.FEAR
            State.ICE_CREAM -> Emotion.INDIFFERENCE
        }

    override val attackEvent: Event
        get() = EventAttack(
            this,
            BattleFieldDispersion(state == State.ICE_CREAM),
            onAfterVictoryEvent = EventUnlockBattle(21, 22)
        )

    private var linesSaid = 0
    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeak(this) {
                R.string.lands_dispersion_0_1
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_dispersion_1_1,
                    R.string.lands_dispersion_1_2,
                    R.string.lands_dispersion_1_3,
                    R.string.lands_dispersion_1_4,
                )
            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_dispersion_2_1,
                    R.string.lands_dispersion_2_2,
                    R.string.lands_dispersion_2_3,
                )
            }
            3 -> {
                state = State.SMALL_INTEREST
                EventNPCSpeakCutscene(this, isSkippable = true) {
                    listOf(
                        R.string.lands_dispersion_3_1,
                        R.string.lands_dispersion_3_2,
                    )
                }
            }
            4 -> EventDispersionBreakdown(this)
            else -> EventNPCSpeak(this) {
                R.string.lands_dispersion_ice_cream
            }
        }

    enum class State {
        NORMAL,
        SMALL_INTEREST,
        FEAR,
        ICE_CREAM
    }
}