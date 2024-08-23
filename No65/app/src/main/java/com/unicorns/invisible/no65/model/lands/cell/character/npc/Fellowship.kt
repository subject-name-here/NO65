package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldFellowship
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.BROTHERHOOD_REJECTED
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Fellowship(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 13
    override val nameId: Int
        get() = R.string.lands_fellowship_name

    override val centerSymbol: String
        get() = "⚝"
    override val centerSymbolColor
        get() = R.color.pink

    var state = State.HOSTILE
    override fun chillCheck() = state == State.CHILL

    override val faceCell: String
        get() = if (chillCheck()) "\uD83D\uDE03" else "\uD83D\uDE20"

    override val emotion: Emotion
        get() = if (chillCheck()) Emotion.CALMNESS else Emotion.HOSTILITY

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldFellowship(),
        onAfterVictoryEvent = EventUnlockBattle(42)
    )

    @Transient
    override val speakEvent: Event = EventNPCSpeakCutscene(this, isSkippable = true) {
        listOf(
            R.string.lands_fellowship_0_1,
            R.string.lands_fellowship_0_2,
            R.string.lands_fellowship_0_3,
        )
    }.then(attackEvent)

    private var talkFired = false
    private var rejectedLineSaid = false
    private var baseLineSaid = false
    override val chillEvent
        get() = EventFactory.createWithNext { manager ->
            val rejected = BROTHERHOOD_REJECTED in manager.gameState.flagsMaster
            when {
                !rejected && !talkFired -> {
                    talkFired = true
                    EventNPCSpeakCutscene(this@Fellowship, isSkippable = true) {
                        listOf(
                            R.string.lands_fellowship_chill_0_1,
                            R.string.lands_fellowship_chill_0_2,
                            R.string.lands_fellowship_chill_0_3,
                        )
                    }
                }
                !rejected && talkFired -> EventNPCSpeak(this@Fellowship) {
                    R.string.lands_fellowship_chill_else_1
                }
                rejected && !talkFired -> {
                    talkFired = true
                    rejectedLineSaid = true
                    EventNPCSpeakCutscene(this@Fellowship, isSkippable = true) {
                        listOf(
                            R.string.lands_fellowship_chill_rejected_meet_1,
                            R.string.lands_fellowship_chill_rejected_meet_2,
                            R.string.lands_fellowship_chill_rejected_meet_3,
                            R.string.lands_fellowship_chill_rejected_meet_4,
                        )
                    }
                }
                !rejectedLineSaid -> {
                    rejectedLineSaid = true
                    EventNPCSpeakCutscene(this@Fellowship, isSkippable = true) {
                        listOf(
                            R.string.lands_fellowship_chill_rejected_1,
                        )
                    }
                }
                !baseLineSaid -> {
                    baseLineSaid = true
                    EventNPCSpeak(this@Fellowship) {
                        R.string.lands_fellowship_chill_base_1
                    }
                }
                else -> EventNPCSpeak(this@Fellowship) {
                    R.string.lands_fellowship_chill_see_ya_later_1
                }
            }
        }

    enum class State {
        CHILL,
        HOSTILE
    }
}