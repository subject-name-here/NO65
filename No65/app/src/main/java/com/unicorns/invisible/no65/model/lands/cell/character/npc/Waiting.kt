package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldWaiting
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventWaiting
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable


@Serializable
class Waiting(override var cellBelow: Cell): CellNPC() {
    override val id: Int
        get() = 5
    override val nameId: Int
        get() = R.string.lands_waiting_name

    override val centerSymbol: String
        get() = "\uD83D\uDCA4"
    override val centerSymbolColor
        get() = R.color.light_blue
    override val faceCell: String
        get() = "\uD83D\uDE34"

    override val emotion: Emotion
        get() = Emotion.CALMNESS

    override fun use(): Event = EventWaiting(this)

    val attackEvent
        get() = EventAttack(
            this@Waiting,
            BattleFieldWaiting(),
            onAfterVictoryEvent = EventUnlockBattle(1)
        )
}