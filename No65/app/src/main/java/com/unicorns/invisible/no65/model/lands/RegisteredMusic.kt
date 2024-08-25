package com.unicorns.invisible.no65.model.lands

import com.unicorns.invisible.no65.LandsManager
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.ALIEN_SPACESHIP_TO_LEAVE
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.CREATIVE_HEAVEN_DEAD
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.GENTLE_WIND_ENCOUNTERED_SEC2
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.GENTLE_WIND_ENCOUNTERED_SEC3
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.GENTLE_WIND_ENCOUNTERED_SEC4
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.RETREAT_DEAD
import com.unicorns.invisible.no65.model.lands.RegisteredFlags.Companion.SEC1_STARTED
import com.unicorns.invisible.no65.model.lands.cell.character.npc.GatheringTogether

class RegisteredMusic {
    companion object {
        val locationToMusic = HashMap<String, (LandsManager) -> Int>(mapOf(
            "map_a" to { 0 },
            "map_b" to { 0 },
            "map_eow" to { R.raw.location_eow },
            "map_hub" to { R.raw.location_hub },
            "map_hub_bgh" to { R.raw.location_hub },
            "map_hub_mus" to { R.raw.location_hub },
            "map_hub_smh" to { R.raw.location_hub },
            "map_hub_mkt" to { R.raw.location_hub },
            "map_hub_bhb" to { R.raw.location_hub },
            "map_aoc" to { R.raw.location_aoc },
            "map_abh" to { R.raw.location_abh },
            "map_jal_cel" to { R.raw.location_jal_cel },
            "map_jal_cor" to { R.raw.location_jal_cel },
            "map_jal" to { R.raw.location_jal },
            "map_jal_of1" to { R.raw.location_jal },
            "map_jal_of2" to { R.raw.location_jal_of2 },
            "map_fst_trp" to { R.raw.location_fst_trp },
            "map_fst_bak" to { R.raw.location_fst },
            "map_fst_sf0" to { R.raw.location_fst },
            "map_fst_sf1" to { R.raw.location_fst },
            "map_fst_sf3" to { R.raw.location_fst },
            "map_fst_htr" to { R.raw.location_fst },
            "map_fst_sf4" to { R.raw.location_fst },
            "map_fst_sf5" to { R.raw.location_fst_2 },
            "map_fst_sf6" to { R.raw.location_fst_2 },
            "map_fst_sf8" to { R.raw.location_fst_2 },
            "map_fst_sf9" to { R.raw.location_fst_2 },
            "map_fst_wot" to { R.raw.location_fst_wot },
            "map_fst_atr" to { R.raw.location_fst_atr },
            "map_fst_wmf" to { R.raw.location_fst_3 },
            "map_fst_tl1" to { R.raw.location_fst_tl },
            "map_fst_tlf" to { R.raw.location_fst_tl },
            "map_fst_atb" to { 0 },
            "map_fst_sec" to { R.raw.location_fst_sec },
            "map_fst_opr" to { R.raw.location_fst_ror },
            "map_fst_ror" to { R.raw.location_fst_ror },
            "map_fst_ror_ur2" to { R.raw.location_fst_ror },
            "map_rhq_hal" to { R.raw.location_rhq },
            "map_rhq_hmr" to { R.raw.location_rhq },
            "map_rhq_mus" to { R.raw.location_rhq },
            "map_hub_mus_rvs" to { R.raw.location_hub_mus_rvs },
            "map_hub_rvs" to { R.raw.location_hub_rvs },
            "map_str_62" to { R.raw.location_hub_rvs },
            "map_hub_mkt_rvs" to { R.raw.location_hub_rvs },
            "map_sec4" to ::getSec4Music,
            "map_sec4_bio" to ::getSec4Music,
            "map_sec4_math" to ::getSec4Music,
            "map_str_49" to { manager ->
                if (manager.gameState.currentMap.getTopCells().filterIsInstance<GatheringTogether>().isNotEmpty()) {
                    R.raw.location_str_49
                } else {
                    R.raw.location_str_49_2
                }
            },
            "map_str_50" to ::getSec4Music,
            "map_str_51" to ::getSec4Music,
            "map_str_52" to ::getSec4Music,
            "map_str_53" to ::getSec4Music,
            "map_str_55" to ::getSec4Music,
            "map_str_56" to ::getSec4Music,
            "map_sec3" to ::getSec3Music,
            "map_sec3_saw" to ::getSec3Music,
            "map_sec3_brr" to ::getSec3Music,
            "map_str_38" to ::getSec3Music,
            "map_str_41" to ::getSec3Music,
            "map_str_42" to ::getSec3Music,
            "map_str_43" to { manager ->
                if (RETREAT_DEAD in manager.gameState.flagsMaster) {
                    0
                } else if (ALIEN_SPACESHIP_TO_LEAVE in manager.gameState.flagsMaster) {
                    R.raw.location_str_43_2
                } else {
                    R.raw.location_str_43
                }
            },
            "map_str_44" to ::getSec3Music,
            "map_mpt" to { R.raw.location_mpt },
            "map_mpt_esr" to { R.raw.location_fst_atr },
            "map_str_48" to { R.raw.location_mpt },
            "map_str_47" to { R.raw.location_mpt },
            "map_str_46_1" to { R.raw.location_mpt },
            "map_str_46_2" to { R.raw.location_mpt },
            "map_str_45" to { R.raw.location_mpt },
            "map_rev_ch1" to { R.raw.location_rev },
            "map_rev_ch2" to { R.raw.location_rev },
            "map_rev_chf" to { R.raw.location_rev },
            "map_rev_cor" to { R.raw.location_rev },
            "map_rev_pz1" to { R.raw.location_rev },
            "map_rev_pz1_5" to { R.raw.location_rev },
            "map_rev_pz2" to { R.raw.location_rev },
            "map_rev_soc" to { R.raw.location_rev },
            "map_sec2" to ::getSec2Music,
            "map_sec2_bar" to ::getSec2Music,
            "map_str_23" to ::getSec2Music,
            "map_str_33" to ::getSec2Music,
            "map_str_32" to ::getSec2Music,
            "map_str_31" to ::getSec2Music,
            "map_str_35" to ::getSec2Music,
            "map_str_36" to ::getSec2Music,
            "map_jal_rev" to { R.raw.location_jal_rev },
            "map_jal_of2_rev" to { R.raw.location_jal_of2 },
            "map_inf_rec" to { R.raw.location_inf },
            "map_inf_man" to { R.raw.location_inf },
            "map_inf_app" to { R.raw.location_inf },
            "map_sec1" to ::getSec1Music,
            "map_str_06" to ::getSec1Music,
            "map_str_07" to ::getSec1Music,
            "map_str_08" to ::getSec1Music,
            "map_str_12" to ::getSec1Music,
            "map_str_14" to ::getSec1Music,
            "map_sec1_fas" to { R.raw.location_sec1_fas },
            "map_koc" to { R.raw.location_koc },
            "map_sec1_bro_hq" to { R.raw.location_sec1_bro },
            "map_sec1_bro_ss" to { R.raw.location_sec1_bro },
            "map_gds" to { R.raw.location_gds },
            "map_gds_shp" to { R.raw.location_gds },
            "map_gds_ghs" to { R.raw.location_gds },
            "map_gds_mhs" to { R.raw.location_gds },
            "map_gds_wh1" to { R.raw.location_gds },
            "map_gds_wh2" to { R.raw.location_gds },
            "map_gds_wh3" to { R.raw.location_gds },
            "map_gds_wh4" to { R.raw.location_gds },
            "map_tpl_all" to ::getTplMusic,
            "map_rev_dep" to { R.raw.location_rev },
            "map_tpl" to ::getTplMusic,
            "map_tpl_trs" to ::getTplMusic,
            "map_tpl_off" to ::getTplMusic,
            "map_str_05" to ::getTplMusic,
            "map_str_03" to ::getTplMusic,
            "map_tpl_cor" to ::getTplMusic,
            "map_tpl_rm1" to { R.raw.location_tpl_rm },
            "map_tpl_rm2" to { R.raw.location_tpl_rm },
            "map_tpl_rm3" to { R.raw.location_tpl_rm },
            "map_tpl_rm4" to { R.raw.location_tpl_rm },
            "map_tpl_rm5" to { R.raw.location_tpl_rm },
            "map_mac_guf" to { R.raw.location_mac_guf },
            "map_tpl_rm6" to { R.raw.location_tpl_rm },
            "map_tpl_rm7" to { 0 },
            "map_tpl_rm8" to { 0 },
            "map_fin" to { 0 },
            "temporary_end" to { 0 },
            "map_cht_rum" to { 0 },
        ))
        
        private fun getSec4Music(manager: LandsManager): Int {
            return if (GENTLE_WIND_ENCOUNTERED_SEC4 in manager.gameState.flagsMaster) {
                R.raw.location_sec4
            } else {
                0
            }
        }

        private fun getSec3Music(manager: LandsManager): Int {
            return if (GENTLE_WIND_ENCOUNTERED_SEC3 in manager.gameState.flagsMaster) {
                R.raw.location_sec3
            } else {
                0
            }
        }
        private fun getSec2Music(manager: LandsManager): Int {
            return if (GENTLE_WIND_ENCOUNTERED_SEC2 in manager.gameState.flagsMaster) {
                R.raw.location_sec2
            } else {
                0
            }
        }
        private fun getSec1Music(manager: LandsManager): Int {
            return if (SEC1_STARTED in manager.gameState.flagsMaster) {
                R.raw.location_sec1
            } else {
                0
            }
        }
        private fun getTplMusic(manager: LandsManager): Int {
            return if (CREATIVE_HEAVEN_DEAD in manager.gameState.flagsMaster) {
                R.raw.location_tpl
            } else {
                0
            }
        }
    }
}