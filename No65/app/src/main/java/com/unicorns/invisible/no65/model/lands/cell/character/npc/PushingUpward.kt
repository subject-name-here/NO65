package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldPushingUpward
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class PushingUpward(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 46
    override val nameId: Int
        get() = R.string.lands_pushing_upward_name

    override val centerSymbol: String
        get() = "\uD83D\uDD1D"
    override val centerSymbolColor: Int
        get() = COLORS.random()

    override val faceCell: String
        get() = "\uD83D\uDE43"

    override val emotion: Emotion
        get() = Emotion.ENERGIZED

    override var rotation: Float = 0f

    private val place: Place = Place.KOC
    override fun chillCheck() = place == Place.AOC

    @Transient
    override val attackEvent = EventAttack(
        this,
        BattleFieldPushingUpward(),
        onAfterVictoryEvent = EventUnlockBattle(44)
    )

    override val chillEvent
        get() = EventNPCSpeak(this) {
            R.string.lands_pushing_upward_chill_else_1
        }

    private var linesSaid = 0
    override val speakEvent: Event
        get() = EventNPCSpeak(this) {
            when (linesSaid++) {
                0 -> R.string.lands_pushing_upward_0_1
                1 -> R.string.lands_pushing_upward_1_1
                2 -> R.string.lands_pushing_upward_2_1
                3 -> R.string.lands_pushing_upward_3_1
                else -> R.string.lands_pushing_upward_else_1
            }
        }

    override fun onTick(tick: Int) {
        val tps = LandsManager.TICKS_PER_SECOND
        rotation = (tick % tps / tps.toFloat()) * 360
    }

    enum class Place {
        AOC,
        KOC
    }

    companion object {
        val COLORS = arrayListOf(R.color.red, R.color.blue, R.color.true_green, R.color.true_yellow)
    }
}