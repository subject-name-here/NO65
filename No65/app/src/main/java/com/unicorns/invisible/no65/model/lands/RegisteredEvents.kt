package com.unicorns.invisible.no65.model.lands

import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.EventPlaced
import com.unicorns.invisible.no65.model.lands.event.events.EventSave
import com.unicorns.invisible.no65.model.lands.event.events.cutscenes.*
import com.unicorns.invisible.no65.model.lands.event.events.location.*
import com.unicorns.invisible.no65.model.lands.event.events.placed.*
import com.unicorns.invisible.no65.model.lands.event.events.util.EventEnteredTemple
import com.unicorns.invisible.no65.model.lands.event.events.util.EventLeftRHQHall
import com.unicorns.invisible.no65.model.lands.event.events.util.EventWaterAddonAvailable
import com.unicorns.invisible.no65.model.lands.event.events.waterever.EventAbysmalWaterAndBC
import com.unicorns.invisible.no65.util.Coordinates
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Serializable
class RegisteredEvents {
    @Transient
    private val masterLock = ReentrantLock()

    @Transient
    private val onMapEnterEvents = mapOf(
        "map_jal_cel" to EventJalCel(),
        "map_fst_bak" to EventWayBack(),
        "map_fst_sf1" to EventSF1(),
        "map_fst_sf3" to EventSF3(),
        "map_fst_htr" to EventHoldingTogether(),
        "map_fst_sf5_5" to EventSF5_5(),
        "map_fst_sf6" to EventSF6(),
        "map_fst_sf7" to EventSF7(),
        "map_fst_sf8" to EventSF8(),
        "map_fst_sf9" to EventSF9(),
        "map_fst_atr" to EventMeetAT(),
        "map_fst_wm0" to EventWM0(),
        "map_fst_wm1" to EventWM1(),
        "map_fst_wm2" to EventWM2(),
        "map_fst_wm3" to EventWM3(),
        "map_fst_tlf" to EventTLF(),
        "map_rhq_hmr" to EventLeftRHQHall(),
        "map_str_50" to EventStr50(),
        "map_sec3" to EventSec3(),
        "map_sec3_brr" to EventSec3Barracks(),
        "map_mpt" to EventEmptyStreet(),
        "map_rev_pz1" to EventRevPz1(),
        "map_rev_ch1" to EventRevCh1(),
        "map_rev_pz1_5" to EventRevPz15(),
        "map_rev_ch2" to EventRevCh2(),
        "map_rev_chf" to EventRevChF(),
        "map_rev_soc" to EventRevSoc(),
        "map_sec2" to EventSec2(),
        "map_jal_rev" to EventJalRev(),
        "map_sec1" to EventSec1(),
        "map_koc" to EventKOC(),
        "map_tpl_all" to EventEnteredTemple(),
        "map_mac_guf" to EventMacGuf(),
        "map_fin" to EventFin(),
        "map_wat_abh" to EventAbysmalWaterAndBC(),
    )
    private val onMapEnterEventsFlags = onMapEnterEvents.keys.associateWith { false }.toMutableMap()
    fun getOnMapEnterEvent(mapIndex: String): Event = masterLock.withLock {
        if (mapIndex !in onMapEnterEvents || onMapEnterEventsFlags[mapIndex] == true) {
            return Event.Null
        }
        onMapEnterEventsFlags[mapIndex] = true
        return onMapEnterEvents[mapIndex] ?: Event.Null
    }

    @Serializable
    data class OnStepEventKey(val mapName: String, val cell: Coordinates)
    @Transient
    private val onStepEvents = mapOf(
        OnStepEventKey("map_jal_of2", Coordinates(1, -1)) to EventJailKSMOffices2OnTeleport(),
        OnStepEventKey("map_fst_trp", Coordinates(-6, 0)) to EventSmallPreponderanceAppears(),
        OnStepEventKey("map_fst_sf3", Coordinates(0, -6)) to EventSF3Stuck(),
        OnStepEventKey("map_fst_sf4", Coordinates(1, 5)) to EventSF4JumpTutorial(),
        OnStepEventKey("map_fst_sf4", Coordinates(1, 4)) to EventSF4JumpTutorial(),
        OnStepEventKey("map_fst_sf5_new", Coordinates(11, 2)) to EventSF5RewindTutorial1(),
        OnStepEventKey("map_fst_sf5_new", Coordinates(11, 3)) to EventSF5RewindTutorial2(),
        OnStepEventKey("map_fst_sf5_new", Coordinates(11, 4)) to EventSF5RewindTutorial2(),
        OnStepEventKey("map_fst_sf6", Coordinates(17, 0)) to EventOppressionAppears(),
        OnStepEventKey("map_fst_sf8", Coordinates(2, 10)) to EventOppressionChase(),
        OnStepEventKey("map_fst_sf8", Coordinates(4, 50)) to EventChaseOver(),
        OnStepEventKey("map_fst_sf9", Coordinates(0, 12)) to EventOppressionAppears2(),
        OnStepEventKey("map_fst_atr", Coordinates(0, -3)) to EventArousingThunderRoomNotice(),
        OnStepEventKey("map_fst_wmf", Coordinates(0, -13)) to Event20Doors(),
        OnStepEventKey("map_str_49", Coordinates(1, 0)) to EventGatheringTogetherParty(),
        OnStepEventKey("map_str_49", Coordinates(0, -1)) to EventGatheringTogetherParty(),
        OnStepEventKey("map_sec4", Coordinates(3, -2)) to EventGentleWindAppears(),
        OnStepEventKey("map_sec3", Coordinates(15, 0)) to EventRevolutionAndGenocideLock(),
        OnStepEventKey("map_str_43", Coordinates(2, 1)) to EventSpaceshipToLeave(),
        OnStepEventKey("map_mpt_esr", Coordinates(4, 7)) to EventRemoveEstonianRoomRoad(),
        OnStepEventKey("map_rev_ch1", Coordinates(3, 1)) to EventRevCh1RightSocket(),
        OnStepEventKey("map_rev_ch1", Coordinates(3, -1)) to EventRevCh1LeftSocket(),
        OnStepEventKey("map_rev_ch2", Coordinates(3, -1)) to EventRevCh2Crash(),
        OnStepEventKey("map_rev_ch2", Coordinates(3, 1)) to EventRevCh2Crash(),
        OnStepEventKey("map_rev_cor", Coordinates(3, 5)) to EventRevCorSocket(),
        OnStepEventKey("map_str_46_1", Coordinates(-19, 1)) to EventTeleportStr46(),
        OnStepEventKey("map_str_46_1", Coordinates(-19, 2)) to EventTeleportStr46(),
        OnStepEventKey("map_tpl_all", Coordinates(4, 0)) to EventTempleAlleyTeleport(),
        OnStepEventKey("map_tpl_all", Coordinates(5, 0)) to EventTempleAlleyTeleport(),
    )
    val onStepEventsFlags = onStepEvents.values
        .map { it.id }
        .associateWith { false }
        .toMutableMap()
    fun getOnStepEvent(mapIndex: String, coordinates: Coordinates): EventPlaced = masterLock.withLock {
        val key = OnStepEventKey(mapIndex, coordinates)
        val event = onStepEvents[key] ?: return EventPlaced.Null
        val eventKey = event.id
        if (eventKey !in onStepEventsFlags || onStepEventsFlags[eventKey] == true) {
            return EventPlaced.Null
        }
        onStepEventsFlags[eventKey] = true
        return event
    }

    @Serializable
    data class OnUseEventKey(val mapName: String, val cell: Coordinates)
    @Transient
    private val onUseEvents = mapOf(
        OnUseEventKey("map_fst_wot", Coordinates(0, 16)) to EventOnFistWayOut(),
        OnUseEventKey("map_rhq_hal", Coordinates(-11, 0)) to Event5Minutes(),
        OnUseEventKey("map_str_50", Coordinates(4, -12)) to EventOppositionMoneyStolen(),
        OnUseEventKey("map_str_53", Coordinates(-5, -1)) to EventTreadingGraffitiWarning(),
        OnUseEventKey("map_rev_ch2", Coordinates(-3, 0)) to EventRevCh2Save(),
        OnUseEventKey("map_jal_of2_rev", Coordinates(-2, 1)) to EventSave(),
        OnUseEventKey("map_tpl_off", Coordinates(4, -2)) to EventWaterAddonAvailable(),
    )
    val onUseEventsFlags = onUseEvents.keys.associateWith { false }.toMutableMap()
    fun getOnUseEvent(mapIndex: String, coordinates: Coordinates): Event = masterLock.withLock {
        val key = OnUseEventKey(mapIndex, coordinates)
        if (key !in onUseEvents || onUseEventsFlags[key] == true) {
            return Event.Null
        }
        onUseEventsFlags[key] = true
        return onUseEvents[key] ?: Event.Null
    }
}