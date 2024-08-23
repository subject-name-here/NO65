package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldProgress
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventProgressFall
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable


@Serializable
class Progress(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 35
    override val nameId: Int
        get() = R.string.lands_progress_name

    var state = State.ROBBED
    override fun chillCheck() = state != State.ROBBED

    override val faceCell: String
        get() = when (state) {
            State.ROBBED -> "\uD83D\uDE41"
            State.FALLEN -> "\uD83D\uDE35"
            State.CHILL -> "\uD83D\uDE04"
        }

    override val centerSymbol: String
        get() = "\uD83E\uDDE0"
    override val centerSymbolColor
        get() = R.color.pink

    override val legsSymbol: String
        get() = if (chillCheck()) ";" else " "

    override val emotion: Emotion
        get() = when (state) {
            State.ROBBED -> Emotion.INDIFFERENCE
            State.FALLEN -> Emotion.HYPOCRISY
            State.CHILL -> Emotion.JOY
        }

    override var rotation: Float = 0f

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldProgress(),
            onAfterVictoryEvent = EventUnlockBattle(19)
        )
    override val speakEvent
        get() = EventNPCSpeak(this) { R.string.lands_progress_else_1 }

    private var linesSaid = 0
    override val chillEvent: Event
        get() {
            return when(linesSaid++) {
                0 -> EventNPCSpeak(this) { R.string.lands_progress_chill_0_1 }
                1 -> EventProgressFall(this)
                else -> EventNPCSpeak(this) { R.string.lands_progress_chill_else_1 }
            }
        }

    enum class State {
        CHILL,
        FALLEN,
        ROBBED
    }
}