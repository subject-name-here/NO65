package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldBeforeCompletion
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.battle.victory.EventBeforeCompletionAfterVictory
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventBeforeCompletion
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume


@Serializable
class BeforeCompletion(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 64
    override val nameId: Int
        get() = R.string.lands_before_completion_name

    override val centerSymbol: String
        get() = "\uD83D\uDD1C"
    override val centerSymbolColor
        get() = R.color.white
    override val faceCell: String
        get() = "\uD83D\uDC76"

    override var emotion: Emotion = Emotion.FRIENDLINESS
    override var rotation: Float = 0f

    var state: State = State.TUTORIAL

    private val meetEvent
        get() = EventBeforeCompletion(this)
            .then(
                EventAttack(
                    this,
                    BattleFieldBeforeCompletion(),
                    onAfterVictoryEvent = EventBeforeCompletionAfterVictory().then(EventUnlockBattle(0)),
                    isTutorial = true
                )
            )
    private var meetEventFired = false
    override fun onSight(distanceToProtagonist: Int): Event {
        if (state == State.PROTAGONIST) return Event.Null
        if (!meetEventFired) {
            meetEventFired = true
            return meetEvent
        }
        return Event.Null
    }

    @Transient
    var awaitingUseContinuation: Continuation<Unit>? = null
    @Transient
    private val lock = ReentrantLock()
    override fun isUsableDuringCutscene(): Boolean {
        return awaitingUseContinuation != null
    }

    override val speakEvent: Event
        get() = Event.Null
    override val attackEvent: Event
        get() = EventFactory.create {
            lock.withLock {
                awaitingUseContinuation?.resume(Unit)
                awaitingUseContinuation = null
            }
        }

    enum class State {
        TUTORIAL,
        PROTAGONIST
    }
}