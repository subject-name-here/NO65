package com.unicorns.invisible.no65

import com.unicorns.invisible.no65.controller.SoundTestController
import com.unicorns.invisible.no65.databinding.ActivitySoundTestBinding
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.FadeDrawer
import com.unicorns.invisible.no65.view.music.MusicPlayer


class SoundTestManager(
    override val activity: MainActivity
) : MenuItemManager {
    override val binding = ActivitySoundTestBinding.inflate(activity.layoutInflater)
    override val drawer = object : FadeDrawer {
        override val activity = this@SoundTestManager.activity
        override val screen = binding.screen
    }
    override val controller = SoundTestController(activity, binding, ::exit)

    override fun setupContent() {
        val names = NAME_TO_RESOURCE.keys.toList()
        controller.setSoundNames(names) { index ->
            val resource = NAME_TO_RESOURCE.values.toList()[index]
            activity.musicPlayer.playMusic(
                resource,
                behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
                isLooping = false
            )
        }
    }

    override fun returnBack() {
        launchCoroutineOnMain {
            ExtrasManager(activity).launch(playMusic = true)
        }
    }

    companion object {
        val NAME_TO_RESOURCE = linkedMapOf(
            "You Have Been Warned" to R.raw.main_menu,
            "There Was Darkness" to R.raw.cutscene_intro,
            "Begin The End" to R.raw.location_eow,
            "The Hub" to R.raw.location_hub,
            "Club of Aces" to R.raw.location_aoc,
            "Simple Beat of Abandoned House" to R.raw.location_abh,
            "Before Before Completion" to R.raw.cutscene_bc,
            "Before Completion" to R.raw.battle_bc,
            "Three, Two, One" to R.raw.battle_enemy,
            "Battle Outro" to R.raw.battle_outro,
            "Determination" to R.raw.cutscene_inner_truth_determined,
            "Battle Intro Again" to R.raw.battle_intro_restart,
            "Outer Lies" to R.raw.battle_inner_truth_tainted,
            "Go to Jail" to R.raw.location_jal_cel,
            "Triple F (1)" to R.raw.battle_clinging_fire1,
            "Triple F (2)" to R.raw.battle_clinging_fire2,
            "Police Castle" to R.raw.location_jal,
            "Boss Backroom" to R.raw.location_jal_of2,
            "Bigger Than Chicken" to R.raw.battle_enemy_boss,
            "Level 0" to R.raw.location_fst_trp,
            "Ocean of Stone" to R.raw.location_fst,
            "Iron Fist" to R.raw.location_fst_2,
            "Chase U" to R.raw.cutscene_chase,
            "Holder of Chains" to R.raw.location_fst_wot,
            "Free Soul, Tied Body" to R.raw.location_fst_atr,
            "Finger Cage" to R.raw.location_fst_3,
            "In The Core" to R.raw.location_fst_tl,
            "Fading Thunder" to R.raw.battle_arousing_thunder,
            "Inside the Secret Tunnel" to R.raw.location_fst_sec,
            "Days of Oppression" to R.raw.location_fst_ror,
            "Rexhibition" to R.raw.location_rhq,
            "Her Majesty" to R.raw.cutscene_receptive_earth_appears,
            "Her Majesty Is Furious" to R.raw.cutscene_receptive_earth_triggered,
            "Grandmother Earth" to R.raw.battle_receptive_earth,
            "The Final Lesson" to R.raw.battle_small_preponderance,
            "The Final (Boring) Lecture" to R.raw.battle_small_preponderance2,
            "The Final Strike" to R.raw.battle_small_preponderance3,
            "The Hub Revisited Intro" to R.raw.location_hub_mus_rvs,
            "The Hub Revisited" to R.raw.location_hub_rvs,
            "Gentle Wind" to R.raw.cutscene_gentle_wind,
            "Gentle Wind (Short)" to R.raw.cutscene_gentle_wind_1a,
            "Children's Crusade" to R.raw.location_sec4,
            "G.T. Party" to R.raw.location_str_49,
            "G.T. Blues" to R.raw.location_str_49_2,
            "Realization" to R.raw.cutscene_dispersion,
            "Three Mississippi River" to R.raw.battle_enemy_slow,
            "The Advert" to R.raw.cutscene_the_advert,
            "Gentle Wind and Breaking News" to R.raw.cutscene_gentle_wind_sec3,
            "City's Life" to R.raw.location_sec3,
            "Aliens!" to R.raw.location_str_43,
            "Aliens are Leaving!" to R.raw.location_str_43_2,
            "Empty Street" to R.raw.location_mpt,
            "Course For U" to R.raw.location_rev,
            "The Marrying Maiden Appears" to R.raw.cutscene_the_marrying_maiden,
            "Gentle Wind and Breaking News and Farewell" to R.raw.cutscene_gentle_wind_sec2,
            "City's Lights" to R.raw.location_sec2,
            "Forge" to R.raw.location_inf,
            "Police Castle Busted!" to R.raw.location_jal_rev,
            "The Broken Heart" to R.raw.cutscene_broken_heart,
            "City's Limbo" to R.raw.location_sec1,
            "Friendship is Fragile" to R.raw.location_sec1_bro,
            "Club of Kings" to R.raw.location_koc,
            "FASHION is My Profession" to R.raw.location_sec1_fas,
            "Visit Hyper Shop!" to R.raw.location_gds,
            "Dynamite and Squirrels (1)" to R.raw.cutscene_pre_d99_1,
            "Dynamite and Squirrels (2)" to R.raw.cutscene_pre_d99_2,
            "-SELECT-" to R.raw.cutscene_d99_1,
            "Your Choice Matters Not" to R.raw.cutscene_d99_2,
            "The Doomsday Timer" to R.raw.cutscene_d99_timer,
            "Deadly Deduction" to R.raw.cutscene_d99_3,
            "Absolutefulnesslessered" to R.raw.cutscene_d99_4,
            "Fr99dom" to R.raw.battle_d99,
            "The Power of Broken Heart" to R.raw.cutscene_the_mm_bout,
            "The MM's Bout" to R.raw.battle_tmm_2,
            "Higher Than Gods" to R.raw.battle_creative_heaven,
            "Heaven's Requiem" to R.raw.location_tpl,
            "Piano Lines" to R.raw.location_tpl_rm,
            "MacGuffins All I Know" to R.raw.location_mac_guf,
            "There is No Enemy" to R.raw.battle_no_enemy,
            "Gentle Wind and War" to R.raw.cutscene_gentle_wind_war,
            "Tornado" to R.raw.battle_gentle_wind,
            "Your Greatest Enemy" to R.raw.battle_gentle_wind_tainted,
            "Not So Joyous" to R.raw.cutscene_joyous_lake,
            "Lake of Tears" to R.raw.battle_joyous_lake,
            "Lady of Darkness" to R.raw.battle_joyous_lake_tainted,
            "Thoughtful Movement" to R.raw.location_fin,
            "SINTEGRAL OF YOU" to R.raw.battle_the_creature,
            "SINTEGRAL OF YOU (KiQ V)" to R.raw.battle_the_creature_kq,
            "TRIMPERATOR" to R.raw.battle_the_creature_cheated,

            "Meeting in Abandoned House" to R.raw.location_wat_abh,
            "Dangerous Waters" to R.raw.battle_abysmal_water,
            "Before Completion (Short V)" to R.raw.battle_abysmal_water_bc_cutscene,

            "SFX_applause" to R.raw.sfx_applause,
            "SFX_avalanche" to R.raw.sfx_avalanche,
            "SFX_beep_beat" to R.raw.sfx_beepbeat,
            "SFX_break_glass" to R.raw.sfx_break_glass,
            "SFX_break_glass_big" to R.raw.sfx_break_glass_big,
            "SFX_chair_move" to R.raw.sfx_chair_move,
            "SFX_coin" to R.raw.sfx_coin,
            "SFX_creature_appear" to R.raw.sfx_creature_appear,
            "SFX_creature_bit" to R.raw.sfx_creature_bit,
            "SFX_damage" to R.raw.sfx_damage,
            "SFX_damage_enemy" to R.raw.sfx_damage_enemy,
            "SFX_damage_enemy_2" to R.raw.sfx_damage_enemy_2,
            "SFX_doom" to R.raw.sfx_doom,
            "SFX_fanfares" to R.raw.sfx_fanfares,
            "SFX_heal" to R.raw.sfx_heal,
            "SFX_heartbeat" to R.raw.sfx_heartbeat,
            "SFX_huh" to R.raw.sfx_huh,
            "SFX_itt" to R.raw.sfx_itt,
            "SFX_joke" to R.raw.sfx_joke,
            "SFX_kiss" to R.raw.sfx_kiss,
            "SFX_kiss_short" to R.raw.sfx_kiss_short,
            "SFX_knocks_reversed" to R.raw.sfx_knocks_reversed,
            "SFX_lighter" to R.raw.sfx_lighter,
            "SFX_lightning" to R.raw.sfx_lightning,
            "SFX_low_speech" to R.raw.sfx_low_speech,
            "SFX_macguffin_teleport" to R.raw.sfx_macguffin_teleport,
            "SFX_mechanical_failure" to R.raw.sfx_mechanical_failure,
            "SFX_menu" to R.raw.sfx_menu,
            "SFX_menu2" to R.raw.sfx_menu2,
            "SFX_microwave_ding" to R.raw.sfx_microwave_ding,
            "SFX_no_enemy_beat" to R.raw.sfx_no_enemy_beat,
            "SFX_phone" to R.raw.sfx_phone,
            "SFX_plateau" to R.raw.sfx_plateau,
            "SFX_retrobeat" to R.raw.sfx_retrobeat,
            "SFX_retrobeat_distorted" to R.raw.sfx_retrobeat_distorted,
            "SFX_scratch" to R.raw.sfx_scratch,
            "SFX_second_chance" to R.raw.sfx_second_chance,
            "SFX_snap" to R.raw.sfx_snap,
            "SFX_snap_2" to R.raw.sfx_snap_2,
            "SFX_spaceship_leaving" to R.raw.sfx_spaceship_leaving,
            "SFX_speech" to R.raw.sfx_speech,
            "SFX_speech_65" to R.raw.sfx_speech_65,
            "SFX_speech_creature" to R.raw.sfx_speech_creature,
            "SFX_speech_robot" to R.raw.sfx_speech_robot,
            "SFX_tap" to R.raw.sfx_tap,
            "SFX_teleport_bc" to R.raw.sfx_teleport_bc,
            "SFX_tv_on" to R.raw.sfx_tv_on,
            "SFX_unlock" to R.raw.sfx_unlock,
            "SFX_waterball" to R.raw.sfx_waterball,
            "SFX_web_through" to R.raw.sfx_web_through,
            "SFX_whisper" to R.raw.sfx_whisper,
            "SFX_wind" to R.raw.sfx_wind,
            "SFX_youbeat" to R.raw.sfx_youbeat,
            "SFX_ze" to R.raw.sfx_ze,
        )
    }
}