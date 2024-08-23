package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldInnerTruth
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldInnerTruthTainted
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventInnerTruthDetermined
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventInnerTruthMeet
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.FOUGHT_WITH_ITT
import com.unicorns.invisible.no65.saveload.GlobalState
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
class InnerTruth(override var cellBelow: Cell): CellNPC() {
    override val id: Int
        get() = 61
    override val nameId: Int
        get() = R.string.lands_inner_truth_name

    override val centerSymbol: String
        get() = "\uD83D\uDC41"
    override val centerSymbolColor
        get() = R.color.blue

    var state = State.HOSTILE

    override val emotion: Emotion
        get() = if (state != State.HOSTILE) Emotion.LAW else Emotion.BRAVERY

    override val faceCell: String
        get() = when (state) {
            State.CHILL -> {
                "\uD83D\uDE42"
            }
            State.SUSPICIOUS -> {
                "\uD83D\uDE15"
            }
            State.HOSTILE -> {
                "\uD83D\uDE14"
            }
        }

    @Transient
    private val attackEvent = EventFactory.createWithNext { manager ->
        val foughtWithITT = GlobalState.getBoolean(manager.activity, FOUGHT_WITH_ITT)
        EventInnerTruthDetermined(this@InnerTruth, foughtWithITT).then(
            EventAttack(
                this@InnerTruth,
                if (foughtWithITT) {
                    nonEmptyListOf(BattleFieldInnerTruthTainted())
                } else {
                    nonEmptyListOf(BattleFieldInnerTruth(), BattleFieldInnerTruthTainted())
                },
                onAfterVictoryEvent = EventUnlockBattle(2)
            )
        )
    }
    private val meetEvent
        get() = EventInnerTruthMeet(this)
    private val speakEvent
        get() = EventNPCSpeak(this) {
            R.string.lands_inner_truth_else_1
        }

    override fun use(): Event {
        return if (state == State.CHILL) {
            state = State.SUSPICIOUS
            meetEvent
        } else {
            speakEvent
        }
    }

    private var attackEventFired = false
    override fun onSight(distanceToProtagonist: Int): Event {
        return when (state) {
            State.CHILL -> {
                if (distanceToProtagonist < 2) {
                    state = State.SUSPICIOUS
                    meetEvent
                } else {
                    Event.Null
                }
            }
            State.SUSPICIOUS -> Event.Null
            State.HOSTILE -> {
                if (!attackEventFired) {
                    attackEventFired = true
                    attackEvent
                } else {
                    Event.Null
                }
            }
        }
    }

    enum class State {
        CHILL,
        SUSPICIOUS,
        HOSTILE
    }
}