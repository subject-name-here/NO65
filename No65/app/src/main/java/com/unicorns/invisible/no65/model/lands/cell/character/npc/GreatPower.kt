package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldGreatPower
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventGreatPowerJoke
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class GreatPower(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 34
    override val nameId: Int
        get() = R.string.lands_great_power_name

    override val centerSymbol: String
        get() = "\uD83D\uDED4"
    override val centerSymbolColor
        get() = R.color.blue
    override val faceCell: String
        get() = when (state) {
            State.HUB_LAUGHING -> "\uD83D\uDE06"
            else -> "\uD83E\uDD21"
        }
    override val handSymbol: String
        get() = "Ӟ"

    var state: State = State.HOSTILE
    override fun chillCheck() = state != State.HOSTILE

    override val emotion: Emotion
        get() = if (chillCheck()) Emotion.JOY else Emotion.HOSTILITY

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldGreatPower(),
        onAfterVictoryEvent = EventUnlockBattle(47)
    )

    private var eventsDistributed = 0
    override val chillEvent
        get() = when (eventsDistributed++) {
            0 -> EventGreatPowerJoke(this)
            1 -> EventNPCSpeak(this) {
                state = State.HUB_NOT_LAUGHING
                R.string.lands_great_power_chill_1_1
            }
            2 -> EventNPCSpeak(this) {
                R.string.lands_great_power_chill_2_1
            }
            3 -> EventNPCSpeak(this) {
                R.string.lands_great_power_chill_3_1
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_great_power_chill_else_1
            }
        }

    private var speakLinesSaid = 0
    @Transient
    override val speakEvent: Event = EventNPCSpeak(this) {
        when (speakLinesSaid++) {
            0 -> R.string.lands_great_power_0_1
            else -> R.string.lands_great_power_else_1
        }
    }

    enum class State {
        HUB_LAUGHING,
        HUB_NOT_LAUGHING,
        HOSTILE
    }
}