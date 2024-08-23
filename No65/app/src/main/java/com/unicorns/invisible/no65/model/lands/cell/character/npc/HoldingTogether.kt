package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldHoldingTogether
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventSPOnHTDeath
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class HoldingTogether(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 8
    override val nameId: Int
        get() = R.string.lands_holding_together_name

    override val centerSymbol: String
        get() = "\uD83D\uDC65"
    override val centerSymbolColor
        get() = R.color.blue
    override val faceCell: String
        get() = "\uD83D\uDE00"

    override val emotion: Emotion = Emotion.ENERGIZED

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldHoldingTogether(),
            onAfterVictoryEvent = EventSPOnHTDeath().then(EventUnlockBattle(10))
        )

    private var linesSaid = 0
    override val speakEvent
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_holding_together_0_1
                else -> R.string.lands_holding_together_else_1
            }
        }
}