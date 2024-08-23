package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldAbundance
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventAbundanceFear
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class Abundance(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 55
    override val nameId: Int
        get() = R.string.lands_abundance_name

    override val centerSymbol: String
        get() = "\uD83C\uDCBF"
    override val centerSymbolColor: Int
        get() = R.color.red

    var state = State.WHISPERING

    override val faceCell: String
        get() = when (state) {
            State.WHISPERING -> "\uD83E\uDD2B"
            State.SPEAKING -> "\uD83D\uDE10"
        }

    override val emotion: Emotion
        get() = when (state) {
            State.WHISPERING -> Emotion.WHISPER
            State.SPEAKING -> Emotion.HYPOCRISY
        }

    override val speechSound: Int
        get() = when (state) {
            State.WHISPERING -> R.raw.sfx_whisper
            State.SPEAKING -> R.raw.sfx_speech
        }

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldAbundance(),
            onAfterVictoryEvent = EventUnlockBattle(11)
        )

    private var linesSaid = 0
    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeak(this) {
                R.string.lands_abundance_0_1
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_abundance_1_1,
                    R.string.lands_abundance_1_2
                )
            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_abundance_2_1,
                    R.string.lands_abundance_2_2,
                    R.string.lands_abundance_2_3
                )
            }
            3 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_abundance_3_1,
                    R.string.lands_abundance_3_2,
                    R.string.lands_abundance_3_3
                )
            }
            4 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_abundance_4_1,
                    R.string.lands_abundance_4_2,
                    R.string.lands_abundance_4_3
                )
            }
            5 -> EventAbundanceFear(this)
            else -> EventNPCSpeak(this) {
                R.string.lands_abundance_else_1
            }
    }

    enum class State {
        WHISPERING,
        SPEAKING
    }
}