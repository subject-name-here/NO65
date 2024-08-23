package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldFollowing
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.BROTHERHOOD_REJECTED
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class Following(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 17
    override val nameId: Int
        get() = R.string.lands_following_name

    override val centerSymbol: String
        get() = "⚝"
    override val centerSymbolColor
        get() = R.color.red

    var state = State.GRIEF
    override fun chillCheck() = state == State.CHILL

    override val faceCell: String
        get() = if (chillCheck()) "\uD83E\uDDD3" else "\uD83D\uDE22"

    override val emotion: Emotion
        get() = if (chillCheck()) Emotion.LAW else Emotion.FEAR

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldFollowing(),
            onAfterVictoryEvent = EventUnlockBattle(43)
        )

    private var linesSaid = 0
    override val speakEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_following_0_1,
                    R.string.lands_following_0_2,
                    R.string.lands_following_0_3,
                    R.string.lands_following_0_4,
                )
            }
            1 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_following_1_1,
                    R.string.lands_following_1_2,
                    R.string.lands_following_1_3,
                )
            }
            2 -> EventNPCSpeakCutscene(this, isSkippable = true) { manager ->
                val rejected = BROTHERHOOD_REJECTED in manager.gameState.flagsMaster
                if (rejected) {
                    listOf(
                        R.string.lands_following_2_rejected_1,
                        R.string.lands_following_2_rejected_2,
                        R.string.lands_following_2_rejected_3,
                        R.string.lands_following_2_rejected_4,
                        R.string.lands_following_2_rejected_5,
                    )
                } else {
                    listOf(
                        R.string.lands_following_2_1,
                        R.string.lands_following_2_2,
                        R.string.lands_following_2_3,
                    )
                }
            }
            else -> EventNPCSpeak(this) { R.string.lands_following_else_1 }
        }

    override val chillEvent
        get() = when (linesSaid++) {
            0 -> EventNPCSpeakCutscene(this, isSkippable = true) {
                listOf(
                    R.string.lands_following_chill_0_1,
                    R.string.lands_following_chill_0_2,
                )
            }
            1 -> EventNPCSpeakCutscene(this) { manager ->
                manager.gameState.flagsMaster.add(BROTHERHOOD_REJECTED)
                manager.gameState.currentMap.getTopCells()
                    .filterIsInstance<TheFamily>()
                    .forEach { it.state = TheFamily.State.CHILL_HOSTILE }
                listOf(
                    R.string.lands_following_chill_1_1,
                    R.string.lands_following_chill_1_2,
                )
            }
            else -> EventNPCSpeak(this) {
                R.string.lands_following_chill_else_1
            }
        }

    enum class State {
        CHILL,
        GRIEF
    }
}