package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldGrace
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
class Grace(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 22
    override val nameId: Int
        get() = R.string.lands_grace_name

    override val centerSymbol: String
        get() = "\uD83D\uDC8E"
    override val centerSymbolColor
        get() = R.color.light_blue
    override val faceCell: String
        get() = "\uD83E\uDD34"

    override val emotion: Emotion = Emotion.ARROGANCE

    private val place: Place = Place.TEMPLE
    override fun chillCheck() = place == Place.HUB_AOC

    @Transient
    override val attackEvent = EventAttack(
        this@Grace,
        BattleFieldGrace(),
        onAfterVictoryEvent = EventUnlockBattle(58)
    )

    override val chillEvent
        get() = EventNPCSpeak(this) { R.string.lands_grace_chill_else_1 }


    @Transient
    override val speakEvent: Event = EventNPCSpeak(this) {
        R.string.lands_grace_else_1
    }.then(attackEvent)

    enum class Place {
        HUB_AOC,
        TEMPLE,
    }
}