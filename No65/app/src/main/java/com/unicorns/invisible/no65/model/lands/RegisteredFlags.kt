package com.unicorns.invisible.no65.model.lands

import kotlinx.serialization.Serializable


@Serializable
class RegisteredFlags {
    private val set = HashSet<String>()

    operator fun contains(value: String): Boolean = value in set
    fun add(value: String) = set.add(value)
    fun remove(value: String) = set.remove(value)

    companion object {
        const val STARTED_GAME = "started_game"

        const val BROTHERHOOD_REJECTED = "brotherhood_rejected"
        const val DUMPED_BY_FAMILY = "what_a_shame_line_said"
        const val DELIVERANCE_BARGAIN_STARTED = "bargain_started"
        const val MET_WITH_CF = "met_with_cf"
        const val MACGUFFIN_QUEST_STARTED = "macguffin_quest_started"

        const val JAIL_ENTERED = "jail_entered"

        const val OPPOSITION_CONTRACT_IN_PLACE = "opposition_contract_in_place"
        const val OPPOSITION_DEAD = "opposition_dead"
        const val MODESTY_DEAD = "modesty_dead"
        const val BITING_THROUGH_DEAD = "biting_through_dead"
        const val BITING_THROUGH_FIRED_MADELINE = "biting_through_fired_madeline"
        const val KSM_TRAPPORT_FIRED = "trapport_fired"

        const val AT_GHOST_ATTACKED = "attacked_at"
        const val COLLIDED_WITH_GRUB_CUTSCENE1 = "collision_cutscene1_played"
        const val AT_STARTED_SHOWDOWN = "started_showdown_with_at"

        const val LEFT_RHQ_HALL = "left_rhq_hall"

        const val GENTLE_WIND_ENCOUNTERED_SEC4 = "gentle_wind_encountered_sec4"
        const val GATHERING_TOGETHER_PARTY_EXPLAINED = "gt_party_explained"
        const val TREADING_WARNING_ACTIVATED = "treading_warning_activated"

        const val GENTLE_WIND_ENCOUNTERED_SEC3 = "gentle_wind_encountered_sec3"
        const val ALIEN_SPACESHIP_TO_LEAVE = "alien_spaceship_to_leave"
        const val RETREAT_DEAD = "retreat_dead"

        const val EMPTY_STREET_ENTERED = "empty_street_entered"

        const val REVOLUTION_COURSE_VISITED = "revolution_course_visited"
        const val REVOLUTION_CHOICE1_LEFT_SOCKET_FIRED = "revolution_choice1_left_socket_fired"
        const val REVOLUTION_CHOICE1_RIGHT_SOCKET_FIRED = "revolution_choice1_right_socket_fired"

        const val GENTLE_WIND_ENCOUNTERED_SEC2 = "gentle_wind_encountered_sec2"
        const val THE_WANDERER_STOLE_MONEYS = "the_wanderer_stole_moneys"
        const val TREADING_EVENT_FIRED = "treading_event_fired"
        const val COMING_TO_MEET_DEAD = "coming_to_meet_dead"

        const val SEC1_STARTED = "sec1_started"
        const val THE_MARRYING_MAIDEN_LEFT_THE_PARTY = "the_mm_left_the_party"

        const val D_NINETY_NINE_DEFEATED = "d_ninety_nine_defeated"

        const val GREAT_POSSESSION_MONEY_ABSORBED = "great_possession_money_absorbed"
        const val GREAT_POSSESSION_GAVE_MONEY = "great_possession_gave_money"

        const val ENTERED_TEMPLE = "entered_temple"
        const val CREATIVE_HEAVEN_DEAD = "creative_heaven_dead"

        const val ENTERED_FINALE = "entered_finale"
    }
}