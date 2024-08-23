package com.unicorns.invisible.no65.model.lands.cell.character

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.STARTED_GAME
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.cell.character.npc.CellNPCStandard
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttackTheCreature
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventPreFinalBoss
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.THE_CREATURE_KILLED
import com.unicorns.invisible.no65.saveload.GlobalState
import kotlinx.serialization.Serializable


@Serializable
class CellTheCreature(override var cellBelow: Cell) : CellNPCStandard(), CellUsable {
    override val id: Int
        get() = 63
    override val nameId: Int
        get() = R.string.the_creature_name
    override val faceCell: String
        get() = "∞"
    override val centerSymbol: String
        get() = "\uD83D\uDD1B"
    override val centerSymbolColor: Int
        get() = R.color.black
    override val emotion: Emotion
        get() = Emotion.INDIFFERENCE
    override val speechSound: Int
        get() = R.raw.sfx_speech_creature

    override val handSymbol: String
        get() = "Ҷ"
    override val legsSymbol: String
        get() = "ҵ"

    override val attackEvent: Event
        get() = EventPreFinalBoss(this).then(EventAttackTheCreature())
    override val speakEvent: Event
        get() = EventFactory.createWithNext { manager ->
            when {
                GlobalState.getBoolean(manager.activity, THE_CREATURE_KILLED) -> {
                    EventNPCSpeakCutscene(this@CellTheCreature, isSkippable = true) {
                        listOf(
                            R.string.lands_the_creature_dead_else_1,
                            R.string.lands_the_creature_dead_else_2,
                        )
                    }
                }
                STARTED_GAME !in manager.gameState.flagsMaster -> {
                    EventNPCSpeakCutscene(this@CellTheCreature, isSkippable = true) {
                        listOf(
                            R.string.lands_the_creature_cheated_else_1,
                            R.string.lands_the_creature_cheated_else_2,
                            R.string.lands_the_creature_cheated_else_3,
                            R.string.lands_the_creature_cheated_else_4,
                        )
                    }
                }
                else -> {
                    EventNPCSpeakCutscene(this@CellTheCreature, isSkippable = true) {
                        listOf(
                            R.string.lands_the_creature_else_1,
                            R.string.lands_the_creature_else_2,
                        )
                    }
                }
            }
        }
}