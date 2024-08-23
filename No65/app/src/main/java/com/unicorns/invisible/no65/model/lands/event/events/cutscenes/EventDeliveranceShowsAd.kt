package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.lands.BattleMode
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Deliverance
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.view.Lands65Drawer


class EventDeliveranceShowsAd(
    private val characterCell: Deliverance,
): Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showCharacterMessages(characterCell, listOf(
            R.string.lands_deliverance_sec4_show_ad_1,
            R.string.lands_deliverance_sec4_show_ad_2,
            R.string.lands_deliverance_sec4_show_ad_3,
            R.string.lands_deliverance_sec4_show_ad_4,
            R.string.lands_deliverance_sec4_show_ad_5,
            R.string.lands_deliverance_sec4_show_ad_6,
        ))
        drawer.showDeliveranceAd()

        if (gameState is GameState65) {
            gameState.battleMode = BattleMode.PEACE
            (drawer as Lands65Drawer).setBattleMode(gameState.battleMode)
        }
    }
})