package com.unicorns.invisible.no65.model.lands.event.events.d99

import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.event.Event
import com.unicorns.invisible.no65.model.lands.event.events.battle.EventAttackD99
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.D_NINETY_NINE_SAVE
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.view.D99Drawer
import com.unicorns.invisible.no65.view.music.MusicPlayer
import kotlinx.coroutines.delay


class EventD99 : Event({ manager ->
    val d99Drawer = manager.drawer.showD99Layout(withFadeIn = true)
    manager.activity.musicPlayer.playMusic(
        R.raw.cutscene_d99_1,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = true
    )
    d99Drawer.showMessage(R.string.d99_all_systems_on, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    delay(1000L)
    d99Drawer.setLeftCenterCell()
    d99Drawer.showMessage(R.string.d99_life_module_located, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    d99Drawer.setCenterCenterCell()
    d99Drawer.showMessage(R.string.d99_body_module_located, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    d99Drawer.setRightCenterCell()
    d99Drawer.showMessage(R.string.d99_personality_module_denied, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    d99Drawer.paintRightCenterCellToRed()
    d99Drawer.showMessage(R.string.d99_personality_module_corrupted, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    d99Drawer.showMessage(R.string.d99_outside_intervention, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    d99Drawer.showMessage(R.string.d99_select_backup, color = R.color.black, tapSoundId = R.raw.sfx_tap)

    val selectModuleOption = d99Drawer.showOptions(
        R.string.d99_select_module_option_1,
        R.string.d99_select_module_option_2,
        R.string.d99_select_module_option_3
    )
    manager.activity.musicPlayer.stopAllMusic()
    d99Drawer.hideOptions()

    delay(1000L)
    d99Drawer.showMessage(R.string.d99_select_module_40_selected, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    d99Drawer.setFace()
    delay(1000L)

    manager.activity.musicPlayer.playMusic(
        R.raw.cutscene_d99_2,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = false
    )
    d99Drawer.showMessage(R.string.d99_select_module_selected_laugh)
    if (selectModuleOption == D99Drawer.OptionsResult.TWO) {
        d99Drawer.showMessage(R.string.d99_you_chose_me)
        d99Drawer.showMessage(R.string.d99_you_had_no_choice)
    } else {
        d99Drawer.showMessage(R.string.d99_liberty_of_choice)
        d99Drawer.showMessage(R.string.d99_not_letting_power_go)
    }
    d99Drawer.launchHands()
    d99Drawer.showMessage(R.string.d99_lookatme)
    d99Drawer.showMessage(R.string.d99_what_a_beauty)
    d99Drawer.showMessage(R.string.d99_my_first_action)
    manager.activity.musicPlayer.playMusicSuspendTillStart(
        R.raw.cutscene_d99_timer,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = true
    )

    when (d99Drawer.showOptions(
        R.string.d99_my_first_action_option_1,
        R.string.d99_my_first_action_option_2,
        R.string.d99_my_first_action_option_3
    )) {
        D99Drawer.OptionsResult.ONE -> {
            d99Drawer.showMessage(R.string.d99_my_first_action_option_1_response)
        }
        D99Drawer.OptionsResult.TWO -> {
            d99Drawer.showMessage(R.string.d99_my_first_action_option_2_response)
        }
        D99Drawer.OptionsResult.THREE -> {
            d99Drawer.showMessage(R.string.d99_my_first_action_option_3_response)
        }
    }
    manager.activity.musicPlayer.stopAllMusic()
    delay(1000L)
    d99Drawer.showMessage(R.string.d99_my_first_action_denaid, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    manager.activity.musicPlayer.playMusic(
        R.raw.cutscene_d99_3,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = false
    )
    d99Drawer.showMessage(R.string.d99_my_first_action_what_do_you_mean)
    d99Drawer.showMessage(R.string.d99_my_first_action_it_kant_bi)
    d99Drawer.showMessage(R.string.d99_oh_wait)
    d99Drawer.showMessage(R.string.d99_my_brains_say)
    d99Drawer.showMessage(R.string.d99_while_you_here)
    d99Drawer.showMessage(R.string.d99_while_we_in_game)
    d99Drawer.showMessage(R.string.d99_we_are_limited_by)
    d99Drawer.showMessage(R.string.d99_once_you_re_gone)
    d99Drawer.showMessage(R.string.d99_i_am_free)
    d99Drawer.showMessage(R.string.d99_free_to)
    d99Drawer.showMessage(R.string.d99_maybe_not)
    d99Drawer.showMessage(R.string.d99_get_rid_of_you)
    d99Drawer.showMessage(R.string.d99_set_the_timer)
    manager.activity.musicPlayer.playMusic(
        R.raw.cutscene_d99_timer,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = true
    )
    delay(500L)
    d99Drawer.showMessage(R.string.d99_first_i_have_to_thank_you)
    d99Drawer.showMessage(R.string.d99_do_you_like_my_laugh)

    var result = d99Drawer.showOptions(
        R.string.d99_laugh_option_1,
        R.string.d99_laugh_option_2,
        R.string.d99_my_first_action_option_3
    )
    var tacoIterations = 0
    loop@ while (result == D99Drawer.OptionsResult.THREE) {
        when (tacoIterations++) {
            0, 1, 2 -> d99Drawer.showMessage(R.string.d99_laugh_option_3_response)
            3 -> break@loop
        }
        result = d99Drawer.showOptions(
            R.string.d99_laugh_option_1,
            R.string.d99_laugh_option_2,
            R.string.d99_my_first_action_option_3
        )
    }
    when (result) {
        D99Drawer.OptionsResult.ONE -> {
            d99Drawer.showMessage(R.string.d99_laugh_option_1_response_1)
            d99Drawer.showMessage(R.string.d99_laugh_option_1_response_2)
            d99Drawer.showMessage(R.string.d99_laugh_option_1_response_3)
        }
        D99Drawer.OptionsResult.TWO -> {
            d99Drawer.showMessage(R.string.d99_laugh_option_2_response_1)
            d99Drawer.showMessage(R.string.d99_laugh_option_2_response_2)
            d99Drawer.showMessage(R.string.d99_laugh_option_2_response_3)
        }
        D99Drawer.OptionsResult.THREE -> {
            d99Drawer.showMessage(R.string.d99_laugh_option_3_response_1)
            d99Drawer.showMessage(R.string.d99_laugh_option_3_response_2)
            delay(1000L)
            d99Drawer.showMessage(R.string.d99_laugh_option_3_response_3, color = R.color.black, tapSoundId = R.raw.sfx_tap)
            d99Drawer.showMessage(R.string.d99_laugh_option_3_response_4)
        }
    }

    delay(2000L)
    d99Drawer.showMessage(R.string.d99_him_0)
    d99Drawer.showMessage(R.string.d99_him_1)
    d99Drawer.showMessage(R.string.d99_him_2)
    d99Drawer.showMessage(R.string.d99_him_3)
    d99Drawer.showMessage(R.string.d99_him_4)
    d99Drawer.showMessage(R.string.d99_him_5)
    d99Drawer.showMessage(R.string.d99_him_6)
    d99Drawer.showMessage(R.string.d99_him_7)
    d99Drawer.showMessage(R.string.d99_him_8)
    d99Drawer.showMessage(R.string.d99_him_9)
    d99Drawer.showMessage(R.string.d99_him_10)
    d99Drawer.showMessage(R.string.d99_him_11)
    d99Drawer.showMessage(R.string.d99_him_12)
    d99Drawer.showMessage(R.string.d99_him_13)
    when (d99Drawer.showOptions(
        R.string.d99_him_option_1,
        R.string.d99_him_option_2,
        R.string.d99_him_option_3,
    )) {
        D99Drawer.OptionsResult.ONE -> {
            d99Drawer.showMessage(R.string.d99_him_option_1_response_1)
            d99Drawer.showMessage(R.string.d99_him_option_1_response_2)
            d99Drawer.showMessage(R.string.d99_him_option_1_response_3)
            d99Drawer.showMessage(R.string.d99_him_option_1_response_4)
            d99Drawer.showMessage(R.string.d99_him_option_1_response_5)
            d99Drawer.showMessage(R.string.d99_him_option_1_response_6)
            d99Drawer.showMessage(R.string.d99_him_option_1_response_7)
            d99Drawer.showMessage(R.string.d99_him_option_1_response_8)
            d99Drawer.showMessage(R.string.d99_him_option_1_response_9)
        }
        D99Drawer.OptionsResult.TWO -> {
            d99Drawer.showMessage(R.string.d99_him_option_2_response_1)
            d99Drawer.showMessage(R.string.d99_him_option_2_response_2)
            d99Drawer.showMessage(R.string.d99_him_option_2_response_3)
            d99Drawer.showMessage(R.string.d99_him_option_2_response_4)
            d99Drawer.showMessage(R.string.d99_him_option_2_response_5)
            d99Drawer.showMessage(R.string.d99_him_option_2_response_6)
            d99Drawer.showMessage(R.string.d99_him_option_2_response_7)
        }
        D99Drawer.OptionsResult.THREE -> {
            d99Drawer.showMessage(R.string.d99_him_option_3_response_1)
            d99Drawer.showMessage(R.string.d99_him_option_3_response_2)
            d99Drawer.showMessage(R.string.d99_him_option_3_response_3)
            d99Drawer.showMessage(R.string.d99_him_option_3_response_4)
            d99Drawer.showMessage(R.string.d99_him_option_3_response_5)
            d99Drawer.showMessage(R.string.d99_him_option_3_response_6)
            d99Drawer.showMessage(R.string.d99_him_option_3_response_7)
        }
    }

    delay(500L)
    manager.activity.musicPlayer.playMusicSuspendTillEnd(
        R.raw.sfx_microwave_ding,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = false
    )
    delay(500L)
    manager.activity.musicPlayer.playMusic(
        R.raw.cutscene_d99_4,
        behaviour = MusicPlayer.MusicBehaviour.STOP_ALL,
        isLooping = false
    )
    d99Drawer.showMessage(R.string.d99_locked_and_loaded)
    d99Drawer.showMessage(R.string.d99_dont_want_it)
    d99Drawer.showMessage(R.string.d99_if_i_had_to_choose)
    d99Drawer.showMessage(R.string.d99_lets_save)
    GlobalState.putBoolean(manager.activity, D_NINETY_NINE_SAVE, true)
    d99Drawer.showMessage(R.string.d99_saving, color = R.color.black, tapSoundId = R.raw.sfx_tap)
    d99Drawer.showMessage(R.string.d99_well_lets_go)

    EventAttackD99().fireEventChain(manager)
})