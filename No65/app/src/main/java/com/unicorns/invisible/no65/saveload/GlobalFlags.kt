package com.unicorns.invisible.no65.saveload

import com.unicorns.invisible.no65.MainActivity


class GlobalFlags {
    companion object {
        // DO NOT FORGET TO ADD FLAG IN WALKTHROUGH_FLAGS OR ETERNAL_FLAGS!!!
        const val BC_BATTLE_EVENT_REACHED = "bc_reached_battle"
        const val TRIGRAMS_HELP_AVAILABLE = "trigrams_help_available"
        const val DIED_AT_LEAST_ONCE = "died_at_least_once"
        const val FOUGHT_WITH_ITT = "fought_with_itt"
        const val FOUGHT_WITH_CF = "fought_with_cf"
        const val CF_BATTLE_KNOWS_ABOUT_EVASIONS = "cf_knows_about_evasions"
        const val CF_BATTLE_KNOWS_HOW_TO_DAMAGE = "cf_knows_how_to_damage"
        const val KSM_BOSS_REVEALED = "ksm_boss_revealed"
        const val OPPRESSION_CHASE_FIRED = "oppression_chase_fired"
        const val AT_BATTLE_EVENT_REACHED = "at_battle_event_reached"
        const val AT_LIGHTNING_STRIKE_REACHED = "at_lightning_strike_reached"
        const val AT_EVADED_LIGHTNING = "at_evaded_lightning"
        const val CONTEMPLATION_ENCOUNTERED_COUNTER = "contemplation_counter"
        const val CONTEMPLATION_WAS_KILLED = "contemplation_was_killed"
        const val REVOLUTION_RED_BUTTON_PRESSED = "revolution_red_button_pressed"
        const val TREADING_CUTSCENE_FIRED = "treading_cutscene_fired"
        const val D_NINETY_NINE_SAVE = "d_ninety_nine_save"
        const val D_NINETY_NINE_RULES_EXPLAINED = "d_ninety_nine_rules_explained"
        const val THE_MM_CUTSCENE_PLAYED = "the_mm_cutscene_played"
        const val THE_MM_POWER_EMBRACED = "the_mm_power_embraced"
        const val THE_MM_ENCOUNTERED_COUNTER = "the_mm_encountered_counter"
        const val CREATIVE_HEAVEN_BATTLE_IN_PROGRESS = "creative_heaven_battle_in_progress"
        const val GENTLE_WIND_WAR_DECLARED = "gentle_wind_war_declared"
        const val GENTLE_WIND_TAINTED_SHOWN = "gentle_wind_tainted_shown"
        const val ATTRIBUTIONS_AVAILABLE = "attributions_available"
        const val THE_CREATURE_LAST_MOVE_NUMBER = "the_creature_last_attack_number"
        const val THE_CREATURE_MET_COUNTER = "the_creature_met_counter"
        const val THE_CREATURE_EXPLAINED_FIRST_ATTACK = "the_creature_explained_first_attack"
        const val THE_CREATURE_ENSURES_VICTORY = "the_creature_ensures_victory"
        const val THE_CREATURE_TIMEBACK_LIMIT_EXPLAINED = "the_creature_timeback_limit_explained"
        const val THE_CREATURE_KILLED = "the_creature_killed"

        const val WATER_ADDON_AVAILABLE = "water_addon_available"
        const val HAS_SEEN_WATER_CUTSCENE = "has_seen_water_cutscene"

        const val SOUND_TEST_AVAILABLE = "sound_test_available"
        const val SOUND_TEST_D99_DEAD = "sound_test_d99_dead"
        const val SOUND_TEST_NO_ENEMY_DEAD = "sound_test_d99_dead"
        const val SOUND_TEST_TMM_DEAD = "sound_test_d99_dead"
        const val SOUND_TEST_WATEREVER_COMPLETE = "sound_test_waterever_complete"

        const val MACGUFFIN_QUEST_COMPLETED = "macguffin_quest_completed"

        val WALKTHROUGH_FLAGS = listOf(
            BC_BATTLE_EVENT_REACHED,
            TRIGRAMS_HELP_AVAILABLE,
            DIED_AT_LEAST_ONCE,
            FOUGHT_WITH_ITT,
            FOUGHT_WITH_CF,
            CF_BATTLE_KNOWS_ABOUT_EVASIONS,
            CF_BATTLE_KNOWS_HOW_TO_DAMAGE,
            KSM_BOSS_REVEALED,
            OPPRESSION_CHASE_FIRED,
            AT_BATTLE_EVENT_REACHED,
            AT_LIGHTNING_STRIKE_REACHED,
            AT_EVADED_LIGHTNING,
            CONTEMPLATION_ENCOUNTERED_COUNTER,
            CONTEMPLATION_WAS_KILLED,
            REVOLUTION_RED_BUTTON_PRESSED,
            TREADING_CUTSCENE_FIRED,
            D_NINETY_NINE_SAVE,
            D_NINETY_NINE_RULES_EXPLAINED,
            THE_MM_CUTSCENE_PLAYED,
            THE_MM_POWER_EMBRACED,
            THE_MM_ENCOUNTERED_COUNTER,
            CREATIVE_HEAVEN_BATTLE_IN_PROGRESS,
            GENTLE_WIND_WAR_DECLARED,
            GENTLE_WIND_TAINTED_SHOWN,
            THE_CREATURE_LAST_MOVE_NUMBER,
        )

        val ETERNAL_FLAGS = listOf(
            MainActivity.VOLUME_KEY,
            MainActivity.DEBUG_KEY,
            THE_CREATURE_TIMEBACK_LIMIT_EXPLAINED,
            THE_CREATURE_ENSURES_VICTORY,
            THE_CREATURE_MET_COUNTER,
            THE_CREATURE_EXPLAINED_FIRST_ATTACK,
            THE_CREATURE_KILLED,
            ATTRIBUTIONS_AVAILABLE,
            WATER_ADDON_AVAILABLE,
            HAS_SEEN_WATER_CUTSCENE,
            SOUND_TEST_AVAILABLE,
            SOUND_TEST_D99_DEAD,
            SOUND_TEST_NO_ENEMY_DEAD,
            SOUND_TEST_TMM_DEAD,
            SOUND_TEST_WATEREVER_COMPLETE,
            MACGUFFIN_QUEST_COMPLETED,
        )

        val SOUND_TEST_AVAILABILITY_FLAGS = listOf(
            SOUND_TEST_AVAILABLE,
            SOUND_TEST_D99_DEAD,
            SOUND_TEST_NO_ENEMY_DEAD,
            SOUND_TEST_TMM_DEAD,
            SOUND_TEST_WATEREVER_COMPLETE,
        )
    }
}