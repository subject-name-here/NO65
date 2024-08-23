package com.unicorns.invisible.no65.model.lands

import kotlinx.serialization.Serializable


@Serializable
class RegisteredCounters {
    private val map = HashMap<String, Int>()

    fun preInc(key: String): Int {
        map[key] = (map[key] ?: 0) + 1
        return map[key] ?: 0
    }

    operator fun get(key: String): Int = map[key] ?: 0
    operator fun set(key: String, value: Int) {
        map[key] = value
    }

    companion object {
        const val WORLD_TRACE_NUMBER = "world_trace_number"
        const val SAVE_COUNTER = "save_counter"
        const val MACGUFFINS_DESTROYED = "macguffins_destroyed"
        const val SP_TELEPORT_COUNT = "sp_teleport_count"
        const val AT_SHOWDOWN_NUMBER_OF_HITS = "at_showdown_number_of_hits"
        const val TIMES_RE_TRIGGER_FIRED = "re_pre_mask_trigger_count"
        const val MONEYS_STOLEN_BY_THE_WANDERER = "moneys_stolen_by_the_wanderer"
    }
}