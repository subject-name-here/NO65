package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldClingingFire
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventClingingFireAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventClingingFireMeet
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class ClingingFire(override var cellBelow: Cell) : CellNPC() {
    override val id: Int
        get() = 30
    override val nameId: Int
        get() = R.string.lands_clinging_fire_name

    var centerCellState = CenterCellState.BADGE
    override val centerSymbol: String
        get() = when (centerCellState) {
            CenterCellState.BADGE -> "\uD83D\uDEE1"
            CenterCellState.FIRE -> "\uD83D\uDD25"
        }
    override val centerSymbolColor
        get() = when (centerCellState) {
            CenterCellState.BADGE -> R.color.blue
            CenterCellState.FIRE -> R.color.red
        }
    override val faceCell: String
        get() = "\uD83D\uDC6E"

    override val emotion: Emotion
        get() = Emotion.LAW

    private var chillEventTriggered = false
    private var attackEventTriggered = false

    var state: State = State.HOSTILE

    private val regularSpeakEvent
        get() = EventNPCSpeak(this) {
            R.string.lands_clinging_fire_regular_1
        }

    override fun use(): Event {
        return when (state) {
            State.CHILL -> {
                if (chillEventTriggered) {
                    regularSpeakEvent
                } else {
                    getChillEvent()
                }
            }
            State.HOSTILE -> {
                getAttackEvent()
            }
        }
    }

    override fun onSight(distanceToProtagonist: Int): Event {
        if (distanceToProtagonist < 2) {
            return when (state) {
                State.CHILL -> {
                    getChillEvent()
                }
                State.HOSTILE -> {
                    getAttackEvent()
                }
            }
        }
        return Event.Null
    }

    private fun getChillEvent(): Event {
        if (!chillEventTriggered) {
            chillEventTriggered = true
            return EventClingingFireMeet(this)
        }
        return Event.Null
    }

    private fun getAttackEvent(): Event {
        if (!attackEventTriggered) {
            attackEventTriggered = true
            return EventClingingFireAttack(this)
                .then(EventAttack(
                    this,
                    BattleFieldClingingFire(),
                    onAfterVictoryEvent = EventUnlockBattle(4)
                ))
        }
        return Event.Null
    }

    enum class CenterCellState {
        BADGE,
        FIRE
    }
    enum class State {
        CHILL,
        HOSTILE
    }
}