package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldJoyousLake
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldJoyousLakeTainted
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.events.EventSave
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventJoyousLake
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventJoyousLakeDead
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class JoyousLake(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 58
    override val nameId: Int
        get() = R.string.lands_joyous_lake_name

    override val centerSymbol: String
        get() = "\uD83D\uDC9D"
    override val centerSymbolColor
        get() = R.color.red
    override val faceCell: String
        get() = "\uD83D\uDC78"

    override val emotion: Emotion
        get() = Emotion.INDIFFERENCE

    override val attackEvent
        get() = EventAttack(
            this,
            nonEmptyListOf(BattleFieldJoyousLake(), BattleFieldJoyousLakeTainted()),
            onAfterVictoryEvent = EventJoyousLakeDead().then(EventUnlockBattle(63)).then(EventSave())
        )

    private var linesSaid = 0
    private val speakInnerEvent
        get() = EventNPCSpeak(this) { R.string.lands_joyous_lake_else_1 }
    override val speakEvent
        get() = when (linesSaid++) {
            0 -> EventJoyousLake(this)
            else -> speakInnerEvent
        }
}