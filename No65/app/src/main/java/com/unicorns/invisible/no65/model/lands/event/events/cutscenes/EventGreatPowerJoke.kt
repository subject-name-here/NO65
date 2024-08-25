package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.character.npc.GreatPower
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.util.launchCoroutine
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay

class EventGreatPowerJoke(greatPower: GreatPower) : Event({ manager ->
    val messagesSetup = listOf(
        R.string.lands_great_power_chill_joke_1,
        R.string.lands_great_power_chill_joke_2,
    )
    val messagePunchline = R.string.lands_great_power_chill_joke_3

    manager.wrapCutscene {
        drawer.showTalkingHead(greatPower)
        drawer.showMessages(messagesSetup, greatPower.speechColor, greatPower.speechSound)
        greatPower.state = GreatPower.State.HUB_LAUGHING
        activity.musicPlayer.playMusic(
            R.raw.sfx_joke,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
        drawer.updateTalkingHeadFace(greatPower)
        drawer.showMessage(messagePunchline, greatPower.speechColor, greatPower.speechSound)
        drawer.hideTalkingHead()
    }
    launchCoroutine {
        // TODO: test
        delay(5000L)
        greatPower.state = GreatPower.State.HUB_NOT_LAUGHING
    }
})