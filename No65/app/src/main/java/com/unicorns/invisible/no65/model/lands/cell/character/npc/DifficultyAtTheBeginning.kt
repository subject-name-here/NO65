package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldDifficultyAtTheBeginning
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class DifficultyAtTheBeginning(override var cellBelow: Cell): CellNPCStandard() {
    override val centerSymbol: String
        get() = "⛔"
    override val centerSymbolColor
        get() = R.color.red
    override val faceCell: String
        get() = "\uD83D\uDE12"

    override val id: Int
        get() = 3
    override val nameId: Int
        get() = R.string.lands_difficulty_at_the_beginning_name

    private val state: State = State.HOSTILE
    override fun chillCheck() = state == State.CHILL

    override val emotion: Emotion = Emotion.INDIFFERENCE

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldDifficultyAtTheBeginning(),
        onAfterVictoryEvent = EventUnlockBattle(17)
    )

    private var linesSaid = 0
    override val chillEvent
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_difficulty_at_the_beginning_chill_0_1
                1 -> R.string.lands_difficulty_at_the_beginning_chill_1_1
                else -> R.string.empty_line
            }
        }

    override val speakEvent: Event
        get() = EventNPCSpeak(this) {
            R.string.lands_difficulty_at_the_beginning_else_1
        }

    enum class State {
        CHILL,
        HOSTILE
    }
}