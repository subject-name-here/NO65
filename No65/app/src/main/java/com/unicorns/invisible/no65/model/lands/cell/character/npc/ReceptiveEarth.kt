package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldReceptiveEarth
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventReceptiveEarthMeet
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventReceptiveEarthProvoked
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.util.randBoolean
import kotlinx.serialization.Serializable


@Serializable
class ReceptiveEarth(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 2
    override val nameId: Int
        get() = R.string.lands_receptive_earth_name

    override val centerSymbol: String
        get() = "\uD83C\uDF10"
    override val centerSymbolColor
        get() = R.color.brown
    var state = State.WITHOUT_MASK
    override val faceCell: String
        get() = when (state) {
            State.WITHOUT_MASK -> "\uD83E\uDD27"
            State.MASKED -> "\uD83D\uDE37"
            State.PROVOKED -> "\uD83D\uDE32"
            State.ANGRY -> "\uD83D\uDE24"
        }

    override val emotion: Emotion
        get() = when (state) {
            State.WITHOUT_MASK -> Emotion.ARROGANCE
            State.MASKED -> Emotion.WHISPER
            State.PROVOKED -> Emotion.FEAR
            State.ANGRY -> Emotion.ANGER
        }

    override val speechSound: Int
        get() = when (state) {
            State.MASKED -> R.raw.sfx_whisper
            else -> R.raw.sfx_speech
        }

    private val achooEvent
        get() = EventNPCSpeak(this) {
            R.string.lands_receptive_earth_achoo_1
        }
    private val leaveEvent
        get() = EventNPCSpeak(this) {
            R.string.lands_receptive_earth_leave_1
        }
    override val speakEvent
        get() = if (randBoolean()) {
            achooEvent
        } else {
            leaveEvent
        }

    private var attackLinesSaid = 0
    override val attackEvent
        get() = EventNPCSpeak(this) {
            when (attackLinesSaid++) {
                0 -> R.string.lands_receptive_earth_attack_0_1
                1 -> R.string.lands_receptive_earth_attack_1_1
                else -> R.string.lands_receptive_earth_attack_else_1
            }
        }

    val attackSequence
        get() = EventReceptiveEarthProvoked(this)
            .then(EventAttack(
                this,
                BattleFieldReceptiveEarth(),
                onAfterVictoryEvent = EventUnlockBattle(14)
            ))


    var wearMaskEventFired = false
    override fun onSight(distanceToProtagonist: Int): Event = if (wearMaskEventFired) {
        Event.Null
    } else {
        wearMaskEventFired = true
        EventReceptiveEarthMeet(this@ReceptiveEarth)
    }

    enum class State {
        WITHOUT_MASK,
        MASKED,
        PROVOKED,
        ANGRY
    }
}