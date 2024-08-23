package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldTheFamily
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.DUMPED_BY_FAMILY
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
class TheFamily(override var cellBelow: Cell): CellNPCStandard() {
    override val id: Int
        get() = 37
    override val nameId: Int
        get() = R.string.lands_the_family_name

    override val centerSymbol: String
        get() = "\uD83D\uDC9F"
    override val centerSymbolColor
        get() = R.color.true_green

    var state = State.AFRAID
    override fun chillCheck() = state != State.AFRAID

    override val faceCell: String
        get() = if (chillCheck()) "\uD83D\uDC91" else "\uD83D\uDE31\uD83D\uDE31"
    override val emotion: Emotion
        get() = when (state) {
            State.CHILL_NORMAL -> Emotion.HYPOCRISY
            State.CHILL_HOSTILE -> Emotion.HOSTILITY
            State.AFRAID -> Emotion.FEAR
        }

    private var linesSaid = 0
    private var whatAShameLineSaidLocal = false
    override val chillEvent
        get() = EventNPCSpeak(this) { manager ->
            if (state == State.CHILL_HOSTILE) {
                if (!whatAShameLineSaidLocal && linesSaid > 0) {
                    whatAShameLineSaidLocal = true
                    manager.gameState.flagsMaster.add(DUMPED_BY_FAMILY)
                    R.string.lands_the_family_chill_shame_1
                } else {
                    R.string.lands_the_family_chill_shame_else_1
                }
            } else {
                when (linesSaid++) {
                    0 -> R.string.lands_the_family_chill_0_1
                    1 -> R.string.lands_the_family_chill_1_1
                    2 -> R.string.lands_the_family_chill_2_1
                    else -> R.string.lands_the_family_chill_else_1
                }
            }
        }


    @Transient
    override val attackEvent = EventFactory.createWithNext { manager ->
        val whatAShameLineSaid = DUMPED_BY_FAMILY in manager.gameState.flagsMaster
        EventAttack(
            this@TheFamily,
            BattleFieldTheFamily(whatAShameLineSaid),
            onAfterVictoryEvent = EventUnlockBattle(30)
        )
    }

    @Transient
    override val speakEvent: Event = EventNPCSpeak(this) {
        R.string.lands_the_family_else_1
    }

    enum class State {
        CHILL_NORMAL,
        CHILL_HOSTILE,
        AFRAID
    }
}