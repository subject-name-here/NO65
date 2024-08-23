package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldSmallTaming
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.DIED_AT_LEAST_ONCE
import com.unicorns.invisible.no65.saveload.GlobalState
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
class SmallTaming(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 9
    override val nameId: Int
        get() = R.string.lands_small_taming_name

    override val centerSymbol: String
        get() = "\uD83C\uDF4C"
    override val centerSymbolColor
        get() = R.color.yellow
    override val faceCell: String
        get() = "\uD83D\uDE49"

    override val emotion: Emotion
        get() = Emotion.INDIFFERENCE

    private val state = State.DOOMED
    override fun chillCheck() = state == State.CHILL

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldSmallTaming(),
        onAfterVictoryEvent = EventUnlockBattle(40)
    )

    private var linesSaid = 0
    override val chillEvent
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_small_taming_chill_0_1
                1 -> R.string.lands_small_taming_chill_1_1
                2 -> R.string.lands_small_taming_chill_2_1
                3 -> R.string.lands_small_taming_chill_3_1
                4 -> R.string.lands_small_taming_chill_4_1
                5 -> R.string.lands_small_taming_chill_5_1
                6 -> R.string.lands_small_taming_chill_6_1
                else -> R.string.lands_small_taming_chill_else_1
            }
        }

    override val speakEvent: Event
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_small_taming_0_1,
                    R.string.lands_small_taming_0_2,
                    R.string.lands_small_taming_0_3,
                )
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) { manager ->
                if (GlobalState.getBoolean(manager.activity, DIED_AT_LEAST_ONCE)) {
                    listOf(
                        R.string.lands_small_taming_1_1,
                        R.string.lands_small_taming_1_2,
                        R.string.lands_small_taming_1_3,
                        R.string.lands_small_taming_1_4,
                    )
                } else {
                    listOf(
                        R.string.lands_small_taming_1_1,
                        R.string.lands_small_taming_1_2,
                        R.string.lands_small_taming_1_3_alt,
                        R.string.lands_small_taming_1_4_alt,
                    )
                }

            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_small_taming_2_1,
                    R.string.lands_small_taming_2_2,
                )
            }
            3 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_small_taming_3_1,
                    R.string.lands_small_taming_3_2,
                    R.string.lands_small_taming_3_3,
                    R.string.lands_small_taming_3_4,
                    R.string.lands_small_taming_3_5,
                    R.string.lands_small_taming_3_6,
                    R.string.lands_small_taming_3_7,
                )
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_small_taming_else_1
            }
        }

    enum class State {
        CHILL,
        DOOMED,
    }
}