package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.Abundance
import com.unicorns.invisible.no65.model.lands.event.Event


class EventAbundanceFear(abundance: Abundance) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showTalkingHead(abundance)
        drawer.showMessage(R.string.lands_abundance_fear_1, abundance.speechColor, abundance.speechSound)
        abundance.state = Abundance.State.SPEAKING
        drawer.updateTalkingHeadFace(abundance)
        drawer.showMessage(R.string.lands_abundance_fear_2, abundance.speechColor, abundance.speechSound)
        abundance.state = Abundance.State.WHISPERING
        drawer.updateTalkingHeadFace(abundance)
        drawer.showMessage(R.string.lands_abundance_fear_3, abundance.speechColor, abundance.speechSound)
        drawer.hideTalkingHead()
    }
})