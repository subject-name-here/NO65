package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldRevolution
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class Revolution(override var cellBelow: Cell) : CellNPCStandard() {
    override val nameId: Int
        get() = R.string.lands_revolution_name
    override val id: Int
        get() = 49

    override val faceCell: String
        get() = "\uD83D\uDE20"
    override val centerSymbol: String
        get() = "\uD83D\uDDE1"
    override val centerSymbolColor: Int
        get() = R.color.dark_red
    override val emotion: Emotion
        get() = Emotion.HOSTILITY

    override val attackEvent: Event
        get() = EventAttack(
            this,
            BattleFieldRevolution(),
            onAfterVictoryEvent = EventUnlockBattle(59)
        )
    override val speakEvent: Event
        get() = attackEvent
}