package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldComingToMeet
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventComingToMeet
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventComingToMeetKilled
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable


@Serializable
class ComingToMeet(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 44
    override val nameId: Int
        get() = R.string.lands_coming_to_meet_name

    override val centerSymbol: String
        get() = "\uD83C\uDF7B"
    override val centerSymbolColor
        get() = R.color.true_yellow

    var state = State.CONCERNED
    override val faceCell: String
        get() = when (state) {
            State.CONCERNED -> "\uD83D\uDE26"
            State.BUSY -> "\uD83D\uDE1B"
            State.GLAD -> "\uD83D\uDE03"
            State.NOT_VERY_GLAD -> "\uD83D\uDE12"
            State.HAPPY -> "\uD83D\uDE01"
        }

    override fun chillCheck() = state != State.CONCERNED

    override val emotion: Emotion
        get() = if (chillCheck()) Emotion.FRIENDLINESS else Emotion.FEAR

    private var chillLinesSaid = 0
    private var meetEventFired = false
    override val chillEvent
        get() = if (!meetEventFired) {
            meetEventFired = true
            EventComingToMeet(this)
        } else {
            EventNPCSpeak(this) {
                when (chillLinesSaid++) {
                    0 -> R.string.lands_coming_to_meet_hub_0_1
                    1 -> R.string.lands_coming_to_meet_hub_1_1
                    else -> R.string.lands_coming_to_meet_hub_else_1
                }
            }
        }

    private var linesSaid = 0
    override val speakEvent
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_coming_to_meet_0_1
                1 -> R.string.lands_coming_to_meet_1_1
                2 -> R.string.lands_coming_to_meet_2_1
                3 -> R.string.lands_coming_to_meet_3_1
                else -> R.string.lands_coming_to_meet_else_1
            }
        }

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldComingToMeet(),
            onAfterVictoryEvent = EventComingToMeetKilled().then(EventUnlockBattle(38))
        )

    enum class State {
        BUSY,
        GLAD,
        NOT_VERY_GLAD,
        HAPPY,
        CONCERNED
    }
}