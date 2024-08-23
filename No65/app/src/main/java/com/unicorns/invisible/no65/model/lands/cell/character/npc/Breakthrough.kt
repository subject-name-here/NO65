package com.unicorns.invisible.no65.model.lands.cell.character.npc

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.BattleFieldBreakthrough
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttack
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeak
import com.unicorns.invisible.no65.model.lands.event.events.util.EventUnlockBattle
import kotlinx.serialization.Serializable

@Serializable
class Breakthrough(override var cellBelow: Cell) : CellNPCStandard() {
    override val id: Int
        get() = 43
    override val nameId: Int
        get() = R.string.lands_breakthrough_name

    override val centerSymbol: String
        get() = "\uD83D\uDC45"
    override val centerSymbolColor: Int
        get() = R.color.black

    override val faceCell: String
        get() = "\uD83D\uDC51"

    override val handSymbol: String
        get() = "ҹ"
    override val legsSymbol: String
        get() = "ҩ"

    override val emotion: Emotion
        get() = Emotion.HYPOCRISY


    override val attackEvent
        get() = EventAttack(
            this,
            BattleFieldBreakthrough(),
            onAfterVictoryEvent = EventUnlockBattle(46)
        )

    private var linesSaid = 0
    private var wingsLineSaid = false
    private var legLineSaid = false
    override val speakEvent
        get() = EventNPCSpeak(this@Breakthrough) { manager ->
            if (manager.gameState !is GameState65) return@EventNPCSpeak R.string.empty_line

            when (linesSaid++) {
                0 -> R.string.lands_breakthrough_0_1
                1 -> R.string.lands_breakthrough_1_1
                2 -> R.string.lands_breakthrough_2_1
                3 -> R.string.lands_breakthrough_3_1
                else -> {
                    when {
                        manager.gameState.protagonist.redBullState == CellProtagonist.RedBullState.USED && !wingsLineSaid -> {
                            wingsLineSaid = true
                            R.string.lands_breakthrough_wings_1
                        }
                        manager.gameState.protagonist.legState == CellProtagonist.LegState.ADDED && !legLineSaid -> {
                            legLineSaid = true
                            R.string.lands_breakthrough_leg_1
                        }
                        else -> {
                            R.string.lands_breakthrough_else_1
                        }
                    }
                }
            }
        }
}