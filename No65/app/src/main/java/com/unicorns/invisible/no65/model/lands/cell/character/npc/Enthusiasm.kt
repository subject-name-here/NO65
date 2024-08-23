package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldEnthusiasm
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Enthusiasm(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 16
    override val nameId: Int
        get() = R.string.lands_enthusiasm_name

    override val centerSymbol: String
        get() = "⛳"
    override val centerSymbolColor
        get() = R.color.red
    override val faceCell: String
        get() = "⍥"

    private val place: Place = Place.SEC_1
    override fun chillCheck() = place == Place.HUB

    override val emotion: Emotion = Emotion.ENERGIZED

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldEnthusiasm(),
        onAfterVictoryEvent = EventUnlockBattle(45)
    )

    private var chillLinesSaid = 0
    override val chillEvent: Event
        get() = EventNPCSpeak(this@Enthusiasm) {
            when (chillLinesSaid++) {
                0 -> R.string.lands_enthusiasm_chill_0_1
                1 -> R.string.lands_enthusiasm_chill_1_1
                2 -> R.string.lands_enthusiasm_chill_2_1
                3 -> R.string.lands_enthusiasm_chill_3_1
                4 -> R.string.lands_enthusiasm_chill_4_1
                5 -> R.string.lands_enthusiasm_chill_5_1
                6 -> R.string.lands_enthusiasm_chill_6_1
                7 -> R.string.lands_enthusiasm_chill_7_1
                else -> R.string.lands_enthusiasm_chill_else_1
            }
        }

    private var linesSaid = 0
    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeak(this) { R.string.lands_enthusiasm_0_1 }
            1 -> EventNPCSpeak(this) { R.string.lands_enthusiasm_1_1 }
            2 -> EventNPCSpeak(this) { R.string.lands_enthusiasm_2_1 }
            3 -> EventNPCSpeak(this) { R.string.lands_enthusiasm_3_1 }
            4 -> EventNPCSpeak(this) { R.string.lands_enthusiasm_4_1 }.then(attackEvent)
            else -> Event.Null
        }

    enum class Place {
        HUB,
        SEC_1
    }
}