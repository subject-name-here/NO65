package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldSplittingApart
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventSplittingApartBreakdown
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class SplittingApart(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 23
    override val nameId: Int
        get() = R.string.lands_splitting_apart_name

    var state = State.WORKING
    override val centerSymbol: String
        get() = "\uD83D\uDCBE"
    override val centerSymbolColor
        get() = R.color.blue
    override val faceCell: String
        get() = when (state) {
            State.WORKING -> "\uD83D\uDE1B"
            State.PANICKING -> "\uD83D\uDE1F"
            State.DONE -> "\uD83D\uDE41"
        }

    override val emotion: Emotion
        get() = when (state) {
            State.WORKING -> Emotion.SMALL_INTEREST
            State.PANICKING -> Emotion.FEAR
            State.DONE -> Emotion.INDIFFERENCE
        }

    private val introEvent
        get() = EventSplittingApartBreakdown(this)

    override val attackEvent: Event
        get() = EventAttack(
            this@SplittingApart,
            BattleFieldSplittingApart(),
            onAfterVictoryEvent = EventUnlockBattle(26)
        )

    override val speakEvent: Event
        get() = EventNPCSpeak(this@SplittingApart) {
            R.string.lands_splitting_apart_else_1
        }

    private var introEventFired = false
    override fun onSight(distanceToProtagonist: Int): Event {
        if (!introEventFired) {
            introEventFired = true
            return introEvent
        }

        return Event.Null
    }

    enum class State {
        WORKING,
        PANICKING,
        DONE
    }
}