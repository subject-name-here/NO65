package com.unicorns.invisible.no65.model.lands.cell.character

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.init.InitData
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventYou
import kotlinx.serialization.Serializable

@Serializable
class CellProtagonist(override var cellBelow: Cell) : CellNonStaticCharacter(), CellUsable {
    override val centerSymbol: String
        get() = killed.toString()
    override val centerSymbolColor
        get() = R.color.black

    override val faceCell: String
        get() = "\uD83D\uDE36"
    override val id: Int
        get() = 65
    override val emotion: Emotion
        get() = Emotion.SILENCE
    override val speechSound: Int
        get() = R.raw.sfx_speech_65

    var redBullState = RedBullState.NOT_USED
    override val handSymbol: String
        get() = when (redBullState) {
            RedBullState.NOT_USED -> super.handSymbol
            RedBullState.USED -> "љ"
        }

    var legState = LegState.NOT_ADDED
    override val legsSymbol: String
        get() = when (legState) {
            LegState.NOT_ADDED -> super.legsSymbol
            LegState.ADDED -> ";"
        }

    var killed = InitData.INIT_ENEMIES_KILLED
    var moneys = InitData.INIT_MONEYS

    enum class RedBullState {
        USED,
        NOT_USED
    }

    enum class LegState {
        ADDED,
        NOT_ADDED
    }

    var youState = YouState.UNKNOWN
    enum class YouState {
        UNKNOWN,
        BC,
        NOT_BC,
        BANNERMAN,
        CREEP,
        INTRUDER,
        CREEP_AGAIN,
        ONLY_YOU
    }

    override fun use(): Event = EventYou()
}