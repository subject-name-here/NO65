package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldSmallPreponderance
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventSmallPreponderanceLastLesson
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class SmallPreponderance(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 62
    override val nameId: Int
        get() = R.string.lands_small_preponderance_name

    override val centerSymbol: String
        get() = "❷"
    override val centerSymbolColor
        get() = R.color.grey
    override val faceCell: String
        get() = "\uD83D\uDE11"

    override val emotion: Emotion
        get() = Emotion.SMALL_INTEREST

    var state = State.IDLE

    override val attackEvent
        get() = EventNPCSpeak(this) { R.string.lands_small_preponderance_attack_1 }

    private var linesSaid = 0
    override val speakEvent
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_small_preponderance_0_1
                1 -> R.string.lands_small_preponderance_1_1
                2 -> R.string.lands_small_preponderance_2_1
                3 -> R.string.lands_small_preponderance_3_1
                else -> R.string.empty_line
            }
        }

    private var finalLessonFired = false
    override fun onSight(distanceToProtagonist: Int): Event {
        return when (state) {
            State.IDLE -> Event.Null
            State.FINAL_LESSON -> if (finalLessonFired) {
                Event.Null
            } else {
                finalLessonFired = true
                EventSmallPreponderanceLastLesson(this)
                    .then(EventAttack(
                        this,
                        BattleFieldSmallPreponderance(),
                        onAfterVictoryEvent = EventUnlockBattle(15)
                    ))
            }
        }
    }

    enum class State {
        IDLE,
        FINAL_LESSON
    }
}