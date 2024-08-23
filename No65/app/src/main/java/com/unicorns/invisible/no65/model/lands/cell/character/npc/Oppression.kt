package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldOppression
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventOppressionDefeated
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventOppressionSpeech
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class Oppression(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 47
    override val nameId: Int
        get() = R.string.lands_oppression_name

    override val centerSymbol: String
        get() = "\uD83D\uDD17"
    override val centerSymbolColor: Int
        get() = R.color.dark_grey

    override val faceCell: String
        get() = "\uD83E\uDDDB"

    override var emotion: Emotion = Emotion.HOSTILITY

    private var speechEventTriggered = false
    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldOppression(speechEventTriggered),
            onAfterVictoryEvent = EventOppressionDefeated().then(EventUnlockBattle(12))
        )

    override val speakEvent
        get() = if (speechEventTriggered) {
            EventNPCSpeak(this) { R.string.lands_oppression_else_1 }
        } else {
            speechEventTriggered = true
            EventOppressionSpeech(this)
        }
}