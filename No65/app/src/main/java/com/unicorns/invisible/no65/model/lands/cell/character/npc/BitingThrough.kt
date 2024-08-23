package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldBitingThrough
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.BITING_THROUGH_FIRED_MADELINE
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventBitingThroughRant
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventBitingThroughKilled
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class BitingThrough(override var cellBelow: Cell) : CellNPCStandard() {
    override val nameId: Int
        get() = R.string.lands_biting_through_name
    override val id: Int
        get() = 21
    override val faceCell: String
        get() = "\uD83E\uDD8A"
    override val centerSymbol: String
        get() = "\uD83D\uDC54"
    override val centerSymbolColor: Int
        get() = R.color.dark_blue
    override var emotion: Emotion = Emotion.INDIFFERENCE

    override val attackEvent: Event
        get() = EventFactory.createWithNext { manager ->
            val hasFiredMadeline = BITING_THROUGH_FIRED_MADELINE in manager.gameState.flagsMaster
            EventAttack(
                this@BitingThrough,
                BattleFieldBitingThrough(hasFiredMadeline),
                onAfterVictoryEvent = EventBitingThroughKilled().then(EventUnlockBattle(6))
            )
        }

    private var linesSaid = 0
    override val speakEvent: Event
        get() = when (state) {
            State.JAIL -> jailSpeakEvent
            State.CITY -> citySpeakEvent
        }

    private val jailSpeakEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_biting_through_jail_0_1,
                    R.string.lands_biting_through_jail_0_2,
                    R.string.lands_biting_through_jail_0_3,
                )
            }
            1 -> EventNPCSpeak(this) {
                R.string.lands_biting_through_jail_1_1
            }
            2 -> {
                emotion = Emotion.HOSTILITY
                EventNPCSpeakCutscene(this, isSkippable = true) {
                    listOf(
                        R.string.lands_biting_through_jail_2_1,
                        R.string.lands_biting_through_jail_2_2,
                        R.string.lands_biting_through_jail_2_3,
                        R.string.lands_biting_through_jail_2_4,
                    )
                }
            }
            3 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_biting_through_jail_3_1,
                    R.string.lands_biting_through_jail_3_2,
                    R.string.lands_biting_through_jail_3_3,
                )
            }
            4 -> EventBitingThroughRant(this)
            5 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                emotion = Emotion.HOSTILITY
                listOf(
                    R.string.lands_biting_through_jail_5_1,
                    R.string.lands_biting_through_jail_5_2
                )
            }
            else -> {
                emotion = Emotion.INDIFFERENCE
                EventNPCSpeak(this) {
                    R.string.lands_biting_through_jail_else_1
                }
            }
        }

    private val citySpeakEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_biting_through_koc_0_1,
                    R.string.lands_biting_through_koc_0_2,
                )
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_biting_through_koc_1_1,
                    R.string.lands_biting_through_koc_1_2,
                )
            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) { manager ->
                val isFired = BITING_THROUGH_FIRED_MADELINE in manager.gameState.flagsMaster
                if (isFired) {
                    listOf(
                        R.string.lands_biting_through_koc_2_fired_1,
                        R.string.lands_biting_through_koc_2_fired_2,
                        R.string.lands_biting_through_koc_2_fired_3,
                        R.string.lands_biting_through_koc_2_fired_4,
                    )
                } else {
                    listOf(
                        R.string.lands_biting_through_koc_2_1,
                        R.string.lands_biting_through_koc_2_2,
                    )
                }
            }
            else -> EventNPCSpeak(this) { manager ->
                val isFired = BITING_THROUGH_FIRED_MADELINE in manager.gameState.flagsMaster
                if (isFired) {
                    R.string.lands_biting_through_koc_else_fired_1
                } else {
                    R.string.lands_biting_through_koc_else_1
                }
            }
        }


    var state = State.JAIL
    enum class State {
        JAIL,
        CITY
    }
}