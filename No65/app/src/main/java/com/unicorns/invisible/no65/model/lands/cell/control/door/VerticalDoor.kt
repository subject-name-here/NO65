package com.unicorns.invisible.no65.model.lands.cell.control.door

import com.unicorns.invisible.no65.model.lands.cell.Cell
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
class VerticalDoor(override var cellBelow: Cell): Door() {
    @EncodeDefault
    override var id: Int = -1
    @EncodeDefault
    override var mode: DoorMode = DoorMode.ALL

    override val stateToSymbol: Map<DoorState, Char>
        get() = mapOf(
            DoorState.LOCKED to '‘',
            DoorState.UNLOCKED to '’'
        )
}