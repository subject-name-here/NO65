package com.unicorns.invisible.no65.model.lands.event.events.cutscenes

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.MET_WITH_CF
import com.unicorns.invisible.no65.model.lands.cell.character.CellProtagonist
import com.unicorns.invisible.no65.model.lands.cell.character.npc.ClingingFire
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.FOUGHT_WITH_CF
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventClingingFireAttack(
    private val characterCell: ClingingFire,
): Event(lambda@ { manager ->
    suspend fun lighterSequence() {
        manager.activity.musicPlayer.playMusic(
            R.raw.sfx_lighter,
            behaviour = MusicPlayer.MusicBehaviour.IGNORE,
            isLooping = false
        )
        delay(500L)
        characterCell.centerCellState = ClingingFire.CenterCellState.FIRE
        delay(500L)
    }

    val foughtWithCF = GlobalState.getBoolean(manager.activity, FOUGHT_WITH_CF)
    if (foughtWithCF) {
        manager.wrapCutscene {
            lighterSequence()
            drawer.showCharacterMessages(characterCell, listOf(R.string.lands_clinging_fire_attack_again_1))
        }
        return@lambda
    }

    val metCF = MET_WITH_CF in manager.gameState.flagsMaster
    val messages = listOf(
        if (metCF) R.string.lands_clinging_fire_attack_1_met else R.string.lands_clinging_fire_attack_1,
        R.string.lands_clinging_fire_attack_2,
        R.string.lands_clinging_fire_attack_3,
        R.string.lands_clinging_fire_attack_4,
        if (metCF) R.string.lands_clinging_fire_attack_5_met else R.string.lands_clinging_fire_attack_5,
        R.string.lands_clinging_fire_attack_6,
        R.string.lands_clinging_fire_attack_7,
        R.string.lands_clinging_fire_attack_8,
        R.string.lands_clinging_fire_attack_9,
        R.string.lands_clinging_fire_attack_10,
        R.string.lands_clinging_fire_attack_11,
        R.string.lands_clinging_fire_attack_12,
        R.string.lands_clinging_fire_attack_13,
        R.string.lands_clinging_fire_attack_14,
        R.string.lands_clinging_fire_attack_15,
        R.string.lands_clinging_fire_attack_16,
        R.string.lands_clinging_fire_attack_17,
        R.string.lands_clinging_fire_attack_18,
        R.string.lands_clinging_fire_attack_19,
        R.string.lands_clinging_fire_attack_20,
    )

    manager.wrapCutscene {
        drawer.showCharacterMessages(characterCell, messages)
        lighterSequence()
        drawer.showCharacterMessages(characterCell, listOf(R.string.lands_clinging_fire_attack_21))
    }

    (manager.gameState.protagonist as CellProtagonist).youState = CellProtagonist.YouState.BANNERMAN
})