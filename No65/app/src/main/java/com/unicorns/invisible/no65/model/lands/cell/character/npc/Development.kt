package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldDevelopment
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable


@Serializable
class Development(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 53
    override val nameId: Int
        get() = R.string.lands_development_name

    override val centerSymbol: String
        get() = "\uD83D\uDCD6"
    override val centerSymbolColor
        get() = R.color.blue
    override val faceCell: String
        get() = "\uD83D\uDE42"

    private val state: State = State.TRAINING
    override fun chillCheck() = state == State.CHILL

    override val emotion: Emotion
        get() = Emotion.ARROGANCE

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldDevelopment(),
            onAfterVictoryEvent = EventUnlockBattle(29)
        )

    private var speakLinesSaid = 0
    override val speakEvent
        get() = when (speakLinesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_development_0_1,
                    R.string.lands_development_0_2,
                    R.string.lands_development_0_3,
                )
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_development_1_1,
                    R.string.lands_development_1_2,
                    R.string.lands_development_1_3,
                    R.string.lands_development_1_4,
                    R.string.lands_development_1_5,
                )
            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_development_2_1,
                    R.string.lands_development_2_2,
                    R.string.lands_development_2_3,
                    R.string.lands_development_2_4,
                )
            }
            3 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_development_3_1,
                    R.string.lands_development_3_2,
                    R.string.lands_development_3_3,
                    R.string.lands_development_3_4,
                )
            }
            4 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_development_4_1,
                    R.string.lands_development_4_2,
                    R.string.lands_development_4_3,
                )
            }
            5 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_development_5_1,
                    R.string.lands_development_5_2,
                )
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_development_else_1
            }
        }

    private var linesSaid = 0
    override val chillEvent
        get() = EventNPCSpeak(this@Development) {
            when (linesSaid++) {
                0 -> R.string.lands_development_chill_0_1
                1 -> R.string.lands_development_chill_1_1
                2 -> R.string.lands_development_chill_2_1
                else -> R.string.lands_development_chill_else_1
            }
        }

    enum class State {
        CHILL,
        TRAINING
    }
}