package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldReturn
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventTMMAndReturn
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class Return(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 24
    override val nameId: Int
        get() = R.string.lands_return_name

    override val centerSymbol: String
        get() = "\uD83C\uDF33"
    override val centerSymbolColor
        get() = R.color.green
    override val faceCell: String
        get() = "\uD83E\uDD89"

    override val emotion: Emotion = Emotion.INDIFFERENCE

    override val attackEvent: Event
        get() = EventAttack(
            this,
            BattleFieldReturn(),
            onAfterVictoryEvent = EventUnlockBattle(32)
        )

    private var linesSaid = 0
    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeak(this) { R.string.lands_return_0_1 }
            1 -> EventNPCSpeak(this) { R.string.lands_return_1_1 }
            2 -> EventNPCSpeak(this) { R.string.lands_return_2_1 }
            3 -> EventNPCSpeak(this) { R.string.lands_return_3_1 }
            4 -> EventFactory.createWithNext { manager ->
                val mms = manager.gameState.companions.filterIsInstance<TheMarryingMaiden>()
                if (mms.isNotEmpty()) {
                    EventTMMAndReturn(mms.first(), this@Return)
                } else {
                    speakEvent
                }
            }
            else -> EventNPCSpeak(this) { R.string.lands_return_else_1 }
        }
}