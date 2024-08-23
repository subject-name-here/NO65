package com.unicorns.invisible.no65.model.elements.monogram

sealed interface Monogram {
    fun getSymbol(): String
    fun getNameId(): Int
}