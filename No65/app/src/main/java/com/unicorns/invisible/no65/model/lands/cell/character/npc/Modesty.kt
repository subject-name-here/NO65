package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldModesty
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventModestyKilled
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class Modesty(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 15
    override val nameId: Int
        get() = R.string.lands_modesty_name

    override val centerSymbol: String
        get() = "\uD83D\uDE4F"
    override val centerSymbolColor
        get() = R.color.blue
    override val faceCell: String
        get() = "\uD83D\uDE07"

    override val emotion: Emotion
        get() = Emotion.WHISPER

    override val speechSound: Int
        get() = R.raw.sfx_whisper

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldModesty(),
            onAfterVictoryEvent = EventModestyKilled().then(EventUnlockBattle(5))
        )

    private var linesSaid = 0
    override val speakEvent
        get() = when (state) {
            State.JAIL -> jailSpeakEvent
            State.CITY -> citySpeakEvent
        }
    private val jailSpeakEvent
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_modesty_jail_0_1
                2 -> R.string.lands_modesty_jail_2_1
                4 -> R.string.lands_modesty_jail_4_1
                6 -> R.string.lands_modesty_jail_6_1
                8 -> R.string.lands_modesty_jail_8_1
                10 -> R.string.lands_modesty_jail_10_1
                12 -> R.string.lands_modesty_jail_12_1
                15 -> R.string.lands_modesty_jail_15_1
                else -> R.string.lands_modesty_jail_else_1
            }
        }
    private val citySpeakEvent
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_modesty_city_0_1
                1 -> R.string.lands_modesty_city_1_1
                2 -> R.string.lands_modesty_city_2_1
                3 -> R.string.lands_modesty_city_3_1
                4 -> R.string.lands_modesty_city_4_1
                else -> R.string.empty_line
            }
        }

    var state = State.JAIL
    enum class State {
        JAIL,
        CITY
    }
}