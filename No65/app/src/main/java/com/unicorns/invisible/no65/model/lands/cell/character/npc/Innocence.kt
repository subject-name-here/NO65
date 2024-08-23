package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldInnocence
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Innocence(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 25
    override val nameId: Int
        get() = R.string.lands_innocence_name

    override val centerSymbol: String
        get()= "\uD83C\uDF23"
    override val centerSymbolColor
        get() = R.color.true_yellow
    override val faceCell: String
        get() = "\uD83D\uDC36"

    override val emotion: Emotion
        get() = Emotion.CALMNESS

    private val state: State = State.DOOMED
    override fun chillCheck() = state == State.CHILL

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldInnocence(),
        onAfterVictoryEvent = EventUnlockBattle(28)
    )

    private var speakLinesSaid = 0
    override val speakEvent
        get() = when (speakLinesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_innocence_0_1,
                    R.string.lands_innocence_0_2,
                    R.string.lands_innocence_0_3,
                    R.string.lands_innocence_0_4,
                )
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_innocence_1_1,
                    R.string.lands_innocence_1_2,
                    R.string.lands_innocence_1_3,
                    R.string.lands_innocence_1_4,
                )
            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_innocence_2_1,
                    R.string.lands_innocence_2_2,
                    R.string.lands_innocence_2_3,
                )
            }
            else -> Event.Null
        }

    private var linesSaid = 0
    override val chillEvent: Event
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_innocence_chill_0_1
                1 -> R.string.lands_innocence_chill_1_1
                2 -> R.string.lands_innocence_chill_2_1
                3 -> R.string.lands_innocence_chill_3_1
                4 -> R.string.lands_innocence_chill_4_1
                5 -> R.string.lands_innocence_chill_5_1
                6 -> R.string.lands_innocence_chill_6_1
                else -> R.string.lands_innocence_chill_else_1
            }
        }

    enum class State {
        CHILL,
        DOOMED,
    }
}