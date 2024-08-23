package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.interactive.EventDestroyMacGuffinSoundKeeper
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
class MacGuffinSoundKeeper(override var cellBelow: Cell) : MacGuffin() {
    @Transient
    private val soundIndex = SOUNDS.indices.random()
    @Transient
    private val colorIndex = COLORS.indices.random()

    override val symbolColor: Int
        get() = COLORS[colorIndex]

    override val destroyEvent: Event
        get() = EventDestroyMacGuffinSoundKeeper(this, SOUNDS[soundIndex])

    companion object {
        val SOUNDS = listOf(
            R.raw.sfx_applause,
            R.raw.sfx_avalanche,
            R.raw.sfx_beepbeat,
            R.raw.sfx_chair_move,
            R.raw.sfx_coin,
            R.raw.sfx_damage_enemy,
            R.raw.sfx_doom,
            R.raw.sfx_fanfares,
            R.raw.sfx_huh,
            R.raw.sfx_itt,
            R.raw.sfx_joke,
            R.raw.sfx_kiss,
            R.raw.sfx_lightning,
            R.raw.sfx_mechanical_failure,
            R.raw.sfx_menu,
            R.raw.sfx_microwave_ding,
            R.raw.sfx_plateau,
            R.raw.sfx_scratch,
            R.raw.sfx_second_chance,
            R.raw.sfx_spaceship_leaving,
            R.raw.sfx_unlock,
            R.raw.sfx_waterball,
            R.raw.sfx_web_through,
            R.raw.sfx_youbeat,
            R.raw.sfx_ze,
        )

        val COLORS = listOf(
            R.color.red,
            R.color.orange,
            R.color.true_green,
            R.color.blue,
            R.color.energized_yellow,
            R.color.grey,
            R.color.dark_blue,
            R.color.purple,
            R.color.poison_pink,
            R.color.light_blue
        )
    }
}