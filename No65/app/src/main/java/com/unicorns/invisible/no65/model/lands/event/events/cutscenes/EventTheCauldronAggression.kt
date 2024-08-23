package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheCauldron
import com.unicorns.invisible.no65.model.lands.event.Event


class EventTheCauldronAggression(theCauldron: TheCauldron) : Event({ manager ->
    manager.wrapCutsceneSkippable {
        drawer.showTalkingHead(theCauldron)
        drawer.showMessages(
            listOf(
                R.string.lands_the_cauldron_event_1,
                R.string.lands_the_cauldron_event_2,
            ),
            color = theCauldron.speechColor,
            tapSoundId = theCauldron.speechSound
        )
        theCauldron.state = TheCauldron.State.AGGRESSIVE
        drawer.updateTalkingHeadFace(theCauldron)
        drawer.showMessage(
            R.string.lands_the_cauldron_event_3,
            color = theCauldron.speechColor,
            tapSoundId = theCauldron.speechSound
        )
        drawer.hideTalkingHead()
    }
})