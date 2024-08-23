package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.view.Lands65Drawer
import com.unicorns.invisible.no65.view.lands.StringResourceForFormatWrapper
import kotlinx.serialization.Serializable

@Serializable
abstract class CellMoneyAbstract : CellStaticOnPassableDecor(), CellUsable {
    final override val symbol
        get() = 'Ӡ'
    final override val symbolColor
        get() = R.color.green

    override fun use(): Event = EventFactory.create { manager ->
        manager.gameState.currentMap.removeCellFromTop(this@CellMoneyAbstract)
        if (manager.gameState is GameState65) {
            val moneys = ++manager.gameState.protagonist.moneys
            val moneysNounId = if (moneys == 1) R.string.lands_moneys_noun_singular else R.string.lands_moneys_noun_plural
            manager.wrapCutsceneSkippable {
                drawer.showMessageFormatted(
                    R.string.lands_moneys_message,
                    color = R.color.green,
                    tapSoundId = R.raw.sfx_coin,
                    formatArgs = arrayOf(moneys, StringResourceForFormatWrapper(moneysNounId))
                )
                (drawer as Lands65Drawer).drawStats(manager.gameState.protagonist)
            }
        }
    }
}