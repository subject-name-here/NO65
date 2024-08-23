package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldTheCauldron
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.EventTheCauldronAggression
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class TheCauldron(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 50
    override val nameId: Int
        get() = R.string.lands_the_cauldron_name

    var state: State = State.SAD
    override fun chillCheck() = state == State.CHILL

    override val centerSymbol: String
        get() = "\uD83E\uDD43"
    override val centerSymbolColor
        get() = R.color.almost_black
    override val faceCell: String
        get() = when (state) {
            State.CHILL -> "\uD83E\uDD24"
            State.SAD -> "\uD83D\uDE2A"
            State.AGGRESSIVE -> "\uD83E\uDD24"
        }

    override val emotion: Emotion
        get() = when (state) {
            State.CHILL -> Emotion.SMALL_INTEREST
            State.SAD -> Emotion.FEAR
            State.AGGRESSIVE -> Emotion.HOSTILITY
        }

    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldTheCauldron(),
            onAfterVictoryEvent = EventUnlockBattle(16)
        )

    private var linesSaid = 0
    override val chillEvent
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_the_cauldron_chill_0_1
                1 -> R.string.lands_the_cauldron_chill_1_1
                2 -> R.string.lands_the_cauldron_chill_2_1
                3 -> R.string.lands_the_cauldron_chill_3_1
                else -> R.string.lands_the_cauldron_chill_else_1
            }
        }

    override val speakEvent: Event
        get() = EventTheCauldronAggression(this).then(attackEvent)


    enum class State {
        CHILL,
        SAD,
        AGGRESSIVE
    }
}