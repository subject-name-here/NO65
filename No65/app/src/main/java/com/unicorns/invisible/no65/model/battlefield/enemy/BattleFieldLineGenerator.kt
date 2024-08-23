package com.unicorns.invisible.no65.model.battlefield.enemy

import com.unicorns.invisible.no65.R

interface BattleFieldLineGenerator {
    fun getLine(moveNumber: Int): Int
    fun getDefeatedLine(): Int
    fun getVictoryLine(): Int

    companion object {
        val EMPTY = object : BattleFieldLineGenerator {
            override fun getLine(moveNumber: Int): Int = R.string.empty_line
            override fun getDefeatedLine(): Int = R.string.empty_line
            override fun getVictoryLine(): Int = R.string.empty_line
        }
    }
}