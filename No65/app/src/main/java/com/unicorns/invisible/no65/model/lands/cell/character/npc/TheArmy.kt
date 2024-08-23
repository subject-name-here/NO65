package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldTheArmy
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class TheArmy(override var cellBelow: Cell) : CellNPC() {
    override val id: Int
        get() = 7
    override val nameId: Int
        get() = R.string.lands_the_army_name

    override val centerSymbol: String
        get() = "⚔"
    override val centerSymbolColor
        get() = R.color.grey
    override val faceCell: String
        get() = "\uD83D\uDC77\uD83D\uDC77\uD83D\uDC77\uD83D\uDC77"

    override val emotion: Emotion = Emotion.HOSTILITY

    override fun use(): Event {
        return EventNPCSpeakCutscene(this) {
            listOf(R.string.lands_the_army_event_1)
        }.then(EventAttack(
            this,
            BattleFieldTheArmy(),
            onAfterVictoryEvent = EventUnlockBattle(60)
        ))
    }
}