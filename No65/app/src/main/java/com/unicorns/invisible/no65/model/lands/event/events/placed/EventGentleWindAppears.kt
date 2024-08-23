package com.unicorns.invisible.no65.model.lands.event.events.placed

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.GENTLE_WIND_ENCOUNTERED_SEC4
import com.unicorns.invisible.no65.model.lands.cell.character.npc.GentleWind
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.util.CellUtils
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventGentleWindAppears : EventPlaced({ manager ->
    manager.gameState.flagsMaster.add(GENTLE_WIND_ENCOUNTERED_SEC4)
    manager.wrapCutscene {
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_gentle_wind_appears_0_1,
                R.string.lands_gentle_wind_appears_0_2,
                R.string.lands_gentle_wind_appears_0_3,
                R.string.lands_gentle_wind_appears_0_4,
                R.string.lands_gentle_wind_appears_0_5,
            )
        )

        delay(500L)

        val closestCell = CellUtils.findCurrentMapClosestFloor(this, gameState.protagonist)!!
        val gentleWind = gameState.currentMap.createCellOnTop(closestCell, GentleWind::class)
        activity.musicPlayer.playMusicSuspendTillEnd(
            R.raw.sfx_fanfares,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )

        delay(500L)

        activity.musicPlayer.playMusicSuspendTillStart(
            R.raw.cutscene_gentle_wind,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )

        drawer.showCharacterMessages(gentleWind,
            listOf(
                R.string.lands_gentle_wind_appears_1_1,
                R.string.lands_gentle_wind_appears_1_2,
                R.string.lands_gentle_wind_appears_1_3,
                R.string.lands_gentle_wind_appears_1_4,
                R.string.lands_gentle_wind_appears_1_5,
                R.string.lands_gentle_wind_appears_1_6,
            )
        )

        delay(500L)
        gentleWind.state = GentleWind.State.NEUTRAL
        delay(500L)

        drawer.showCharacterMessages(gentleWind,
            listOf(
                R.string.lands_gentle_wind_appears_2_1,
                R.string.lands_gentle_wind_appears_2_2,
                R.string.lands_gentle_wind_appears_2_3,
                R.string.lands_gentle_wind_appears_2_4,
            )
        )

        delay(3000L)
        gentleWind.state = GentleWind.State.THINKING
        drawer.showTalkingHead(gentleWind)
        drawer.showMessages(
            listOf(
                R.string.lands_gentle_wind_appears_3_1,
            ),
            color = gentleWind.speechColor,
            tapSoundId = gentleWind.speechSound
        )

        gentleWind.state = GentleWind.State.NEUTRAL
        drawer.updateTalkingHeadFace(gentleWind)
        drawer.showMessages(
            listOf(
                R.string.lands_gentle_wind_appears_3_2,
            ),
            color = gentleWind.speechColor,
            tapSoundId = gentleWind.speechSound,
            delayAfterMessage = 0L
        )
        gentleWind.state = GentleWind.State.OFFERING
        drawer.updateTalkingHeadFace(gentleWind)
        drawer.showMessages(
            listOf(
                R.string.lands_gentle_wind_appears_3_3,
            ),
            color = gentleWind.speechColor,
            tapSoundId = gentleWind.speechSound
        )
        gentleWind.state = GentleWind.State.STANDARD
        drawer.hideTalkingHead()

        delay(5000L)

        gentleWind.state = GentleWind.State.HAPPY
        drawer.showTalkingHead(gentleWind)
        drawer.showMessages(
            listOf(
                R.string.lands_gentle_wind_appears_4_1,
                R.string.lands_gentle_wind_appears_4_2,
            ),
            color = gentleWind.speechColor,
            tapSoundId = gentleWind.speechSound
        )
        gentleWind.state = GentleWind.State.STANDARD
        drawer.updateTalkingHeadFace(gentleWind)
        drawer.showMessages(
            listOf(
                R.string.lands_gentle_wind_appears_4_3,
                R.string.lands_gentle_wind_appears_4_4,
                R.string.lands_gentle_wind_appears_4_5,
            ),
            color = gentleWind.speechColor,
            tapSoundId = gentleWind.speechSound
        )
        gentleWind.state = GentleWind.State.THINKING
        drawer.updateTalkingHeadFace(gentleWind)
        drawer.showMessages(
            listOf(
                R.string.lands_gentle_wind_appears_4_6,
                R.string.lands_gentle_wind_appears_4_7,
                R.string.lands_gentle_wind_appears_4_8,
            ),
            color = gentleWind.speechColor,
            tapSoundId = gentleWind.speechSound
        )
        gentleWind.state = GentleWind.State.STANDARD
        drawer.updateTalkingHeadFace(gentleWind)
        drawer.showMessages(
            listOf(
                R.string.lands_gentle_wind_appears_4_9,
                R.string.lands_gentle_wind_appears_4_10,
                R.string.lands_gentle_wind_appears_4_11,
            ),
            color = gentleWind.speechColor,
            tapSoundId = gentleWind.speechSound
        )
        drawer.hideTalkingHead()

        activity.musicPlayer.playMusicSuspendTillEnd(
            R.raw.sfx_fanfares,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )
        gameState.currentMap.removeCellFromTop(gentleWind)

        delay(2000L)
        drawer.showMessagesPhoneWithUnknownHead(
            listOf(
                R.string.lands_gentle_wind_appears_5_1,
            )
        )
        gameState.currentMap.addCellOnTop(gentleWind, gentleWind.coordinates)
        activity.musicPlayer.playMusicSuspendTillEnd(
            R.raw.sfx_fanfares,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
        delay(500L)

        activity.musicPlayer.playMusicSuspendTillStart(
            R.raw.cutscene_gentle_wind_1a,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )

        drawer.showTalkingHead(gentleWind)
        drawer.showMessages(
            listOf(
                R.string.lands_gentle_wind_appears_5_2,
                R.string.lands_gentle_wind_appears_5_3,
            ),
            color = gentleWind.speechColor,
            tapSoundId = gentleWind.speechSound
        )

        drawer.showMessages(
            listOf(
                R.string.lands_gentle_wind_appears_5_4,
            ),
            color = gentleWind.speechColor,
            tapSoundId = gentleWind.speechSound,
            delayAfterMessage = 0L
        )
        drawer.showMessages(
            listOf(
                R.string.lands_gentle_wind_appears_5_5,
                R.string.lands_gentle_wind_appears_5_6,
                R.string.lands_gentle_wind_appears_5_7,
            ),
            color = gentleWind.speechColor,
            tapSoundId = gentleWind.speechSound
        )
        drawer.hideTalkingHead()

        activity.musicPlayer.playMusicSuspendTillEnd(
            R.raw.sfx_fanfares,
            behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
            isLooping = false
        )
        gameState.currentMap.removeCellFromTop(gentleWind)

        delay(500L)
        activity.musicPlayer.playMusic(
            manager.getCurrentMapMusicThemeId(),
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = true
        )
    }
})