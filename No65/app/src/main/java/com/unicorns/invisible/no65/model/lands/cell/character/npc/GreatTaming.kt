package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldGreatTaming
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class GreatTaming (override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 26
    override val nameId: Int
        get() = R.string.lands_great_taming_name

    override val centerSymbol: String
        get() = "\uD83D\uDC3E"
    override val centerSymbolColor
        get() = R.color.brown
    override val faceCell: String
        get() = "\uD83E\uDD81"

    override val emotion: Emotion = Emotion.ENERGIZED

    private val state: State = State.HOSTILE
    override fun chillCheck() = state == State.CHILL

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldGreatTaming(),
        onAfterVictoryEvent = EventUnlockBattle(56)
    )

    private var linesSaid = 0
    override val chillEvent
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_great_taming_chill_0_1
                1 -> R.string.lands_great_taming_chill_1_1
                2 -> R.string.lands_great_taming_chill_2_1
                3 -> R.string.lands_great_taming_chill_3_1
                else -> R.string.lands_great_taming_chill_else_1
            }
        }

    @Transient
    override val speakEvent: Event = EventNPCSpeak(this) {
        R.string.lands_great_taming_0_1
    }.then(attackEvent)

    enum class State {
        CHILL,
        HOSTILE
    }
}