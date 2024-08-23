package com.unicorns.invisible.no65.model.lands.cell.interactive

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellStaticOnPassableDecor
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.cell.character.npc.SplittingApart
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventFactory
import com.unicorns.invisible.no65.model.lands.event.events.speak.EventNPCSpeakCutscene
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.serialization.Serializable

@Serializable
class TheMachine(override var cellBelow: Cell): CellStaticOnPassableDecor(), CellUsable {
    private var triggerState = MachineState.WIP

    override val symbol: Char
        get() = when (triggerState) {
            MachineState.WIP, -> 'ҁ'
            MachineState.BROKEN, MachineState.BROKEN_FINAL -> '҂'
        }
    override val symbolColor
        get() = R.color.grey

    override fun use(): Event {
        triggerState = triggerState.next()
        return when (triggerState) {
            MachineState.WIP, MachineState.BROKEN_FINAL -> Event.Null
            MachineState.BROKEN -> EventFactory.createWithNext { manager ->
                val pointOfInterest = manager.gameState.currentMap.getTopCells()
                    .filterIsInstance<PointOfInterest>()
                    .firstOrNull()
                if (pointOfInterest != null) {
                    manager.gameState.currentMap.removeCellFromTop(pointOfInterest)
                }

                manager.activity.musicPlayer.playMusicSuspendTillEnd(
                    R.raw.sfx_mechanical_failure,
                    behaviour = MusicPlayer.MusicBehaviour.IGNORE,
                    isLooping = false
                )

                val sa = manager.gameState.currentMap.getTopCells()
                    .filterIsInstance<SplittingApart>()
                    .firstOrNull()
                if (sa == null) {
                    Event.Null
                } else {
                    EventNPCSpeakCutscene(sa, isSkippable = true) {
                        listOf(
                            R.string.lands_splitting_apart_machine_destroyed_1,
                            R.string.lands_splitting_apart_machine_destroyed_2,
                            R.string.lands_splitting_apart_machine_destroyed_3,
                            R.string.lands_splitting_apart_machine_destroyed_4,
                        )
                    }
                }
            }
        }
    }

    enum class MachineState {
        WIP,
        BROKEN,
        BROKEN_FINAL;

        fun next(): MachineState {
            return when (this) {
                WIP -> BROKEN
                BROKEN -> BROKEN_FINAL
                BROKEN_FINAL -> BROKEN_FINAL
            }
        }
    }
}