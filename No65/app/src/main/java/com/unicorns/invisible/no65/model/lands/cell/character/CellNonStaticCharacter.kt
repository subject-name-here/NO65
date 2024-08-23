package com.unicorns.invisible.no65.model.lands.cell.character

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.CellNonStatic
import com.unicorns.invisible.no65.model.lands.emotion.Emotion
import kotlinx.serialization.Serializable

@Serializable
abstract class CellNonStaticCharacter: CellNonStatic() {
    abstract val id: Int
    abstract val faceCell: String
    abstract val centerSymbol: String
    abstract val centerSymbolColor: Int

    open val legsSymbol
        get() = "Ӯ"
    open val handSymbol
        get() = "+"

    abstract val emotion: Emotion
    val speechColor: Int
        get() = emotion.getColor()

    open val speechSound
        get() = R.raw.sfx_speech

    final override val symbol: Char
        get() = ' '
    final override val symbolColor: Int
        get() = R.color.no_color

    open val rotation: Float
        get() = 0f
}