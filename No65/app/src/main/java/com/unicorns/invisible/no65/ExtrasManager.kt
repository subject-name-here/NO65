package com.unicorns.invisible.no65

import com.quickbirdstudios.nonEmptyCollection.list.nonEmptyListOf
import com.unicorns.invisible.no65.controller.ExtrasController
import com.unicorns.invisible.no65.databinding.ActivityExtrasBinding
import com.unicorns.invisible.no65.extras.BattleItem
import com.unicorns.invisible.no65.model.battlefield.enemy.enemies.*
import com.unicorns.invisible.no65.model.battlefield.fighter.BattleFieldProtagonist
import com.unicorns.invisible.no65.model.battlefield.fighter.equal.BattleFieldEqualAbysmalWater
import com.unicorns.invisible.no65.model.battlefield.fighter.equal.BattleFieldEqualBeforeCompletion
import com.unicorns.invisible.no65.model.knowledge.Knowledge
import com.unicorns.invisible.no65.model.lands.cell.character.npc.TheWanderer
import com.unicorns.invisible.no65.saveload.GlobalFlags
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.HAS_SEEN_WATER_CUTSCENE
import com.unicorns.invisible.no65.saveload.GlobalFlags.Companion.WATER_ADDON_AVAILABLE
import com.unicorns.invisible.no65.saveload.GlobalState
import com.unicorns.invisible.no65.saveload.SaveManager
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.ExtrasDrawer


class ExtrasManager(
    override val activity: MainActivity
) : MenuItemManager {
    private val isWaterAddonAvailable = GlobalState.getBoolean(activity, WATER_ADDON_AVAILABLE)
    private val isWaterAddonButtonBlocking = !GlobalState.getBoolean(activity, HAS_SEEN_WATER_CUTSCENE) && isWaterAddonAvailable

    override val binding = ActivityExtrasBinding.inflate(activity.layoutInflater)
    override val controller = ExtrasController(
        activity,
        binding,
        ::exit,
        {},
        ::goToSoundTest,
        ::replayBattlesButtonListener,
        ::waterAddonButtonListener,
        isWaterAddonButtonBlocking
    )
    override val drawer = ExtrasDrawer(activity, binding)

    private val battlesNamesToIsBlocking = run {
        val battlesRaw = SaveManager.loadBattles(activity)
        val battlesFlags = battlesRaw.split(" ").map { it.toIntOrNull() }
        BATTLES_LIST.mapIndexed { counter, (index, item) ->
            if (battlesFlags.getOrNull(index) == 1) {
                val name = activity.getString(item.nameId)
                "${counter}. $name" to true
            } else {
                activity.getString(R.string.battle_name_unknown) to false
            }
        }
    }

    override fun setupContent() {
        drawer.setWaterAddonButton(isWaterAddonAvailable)

        val soundTestConditions = GlobalFlags.SOUND_TEST_AVAILABILITY_FLAGS.all { flag ->
            GlobalState.getBoolean(activity, flag)
        }
        if (soundTestConditions) {
            drawer.showSoundTestButton()
        }
    }

    private fun replayBattlesButtonListener() {
        controller.showBattlesList(battlesNamesToIsBlocking) { counter ->
            val item = BATTLES_LIST[counter].second
            launchCoroutineOnMain {
                drawer.fadeToWhite().join()
                activity.musicPlayer.stopAllMusic()
                startBattle(item)
            }
        }
    }

    private fun startBattle(item: BattleItem) = launchCoroutineOnMain {
        BattleManager(
            activity,
            BattleFieldProtagonist(item.knowledge, isInverted = false),
            item.enemiesPool,
            item.managerMode
        ) { _ ->
            returnToExtras()
        }.apply {
            launchBattle()
        }
    }

    private fun waterAddonButtonListener() {
        when {
            isWaterAddonAvailable && isWaterAddonButtonBlocking -> {
                startWaterAddon(isFull = true)
            }
            isWaterAddonAvailable -> {
                controller.showWaterAddonButtons(
                    R.string.water_addon_cutscene, {
                        startWaterAddon(isFull = true)
                    },
                    R.string.water_addon_only_fight, {
                        startWaterAddon(isFull = false)
                    }
                )
            }
        }
    }

    private fun startWaterAddon(isFull: Boolean) = launchCoroutineOnMain {
        drawer.fadeToBlack().join()
        activity.musicPlayer.stopAllMusic()

        if (isFull) {
            startWaterAddonFull()
        } else {
            startWaterAddonFight()
        }
    }
    private fun startWaterAddonFull() = launchCoroutineOnMain {
        val manager = LandsManager(activity, SaveManager.loadInitStateWaterever(activity))
        manager.init()
        manager.processMap()
    }
    private fun startWaterAddonFight() = launchCoroutineOnMain {
        BattleManager(
            activity,
            BattleFieldEqualBeforeCompletion(),
            nonEmptyListOf(BattleFieldEqualAbysmalWater(isWatereverCutsceneless = true)),
            managerMode = BattleManager.Mode.EQUAL,
        ) { _ ->
            returnToExtras()
        }.apply {
            launchBattle()
        }
    }

    private fun goToSoundTest() {
        launchCoroutineOnMain {
            drawer.fadeToWhite().join()
            activity.musicPlayer.stopAllMusic()
            SoundTestManager(activity).launch()
        }
    }
    private fun returnToExtras() {
        launchCoroutineOnMain {
            ExtrasManager(activity).launch(playMusic = true)
        }
    }

    companion object {
        // It has getter so that enemies were new each time.
        val BATTLES_LIST
            get() = listOf(
                0 to BattleItem(R.string.battle_name_0, BattleFieldBeforeCompletion(), Knowledge.TYPES[0], BattleManager.Mode.STANDARD_TUTORIAL),
                1 to BattleItem(R.string.battle_name_1, BattleFieldWaiting(), Knowledge.TYPES[1]),
                2 to BattleItem(R.string.battle_name_2, nonEmptyListOf(BattleFieldInnerTruth(), BattleFieldInnerTruthTainted()), Knowledge.TYPES[1]),
                3 to BattleItem(R.string.battle_name_3, BattleFieldApproach(), Knowledge.TYPES[2]),
                4 to BattleItem(R.string.battle_name_4, BattleFieldClingingFire(isFromExtras = true), Knowledge.TYPES[2]),
                5 to BattleItem(R.string.battle_name_5, BattleFieldModesty(), Knowledge.TYPES[3]),
                6 to BattleItem(R.string.battle_name_6, BattleFieldBitingThrough(hasFiredMadeline = false), Knowledge.TYPES[3]),
                7 to BattleItem(R.string.battle_name_7, BattleFieldOpposition(BattleFieldOpposition.State.JAIL_MODESTY_ALIVE), Knowledge.TYPES[3]),
                8 to BattleItem(R.string.battle_name_8, BattleFieldOpposition(BattleFieldOpposition.State.JAIL_MODESTY_DEAD), Knowledge.TYPES[3]),
                9 to BattleItem(R.string.battle_name_9, BattleFieldKeepingStillMountain(isFromExtras = true), Knowledge.TYPES[3]),
                10 to BattleItem(R.string.battle_name_10, BattleFieldHoldingTogether(), Knowledge.TYPES[4]),
                11 to BattleItem(R.string.battle_name_11, BattleFieldAbundance(), Knowledge.TYPES[4]),
                12 to BattleItem(R.string.battle_name_12, BattleFieldOppression(speechEventTriggered = true), Knowledge.TYPES[4]),
                13 to BattleItem(R.string.battle_name_13, BattleFieldArousingThunder(isFromExtras = true), Knowledge.TYPES[4]),
                14 to BattleItem(R.string.battle_name_14, BattleFieldReceptiveEarth(), Knowledge.TYPES[5]),
                15 to BattleItem(R.string.battle_name_15, BattleFieldSmallPreponderance(), Knowledge.TYPES[6]),
                16 to BattleItem(R.string.battle_name_16, BattleFieldTheCauldron(), Knowledge.TYPES[6]),
                17 to BattleItem(R.string.battle_name_17, BattleFieldDifficultyAtTheBeginning(), Knowledge.TYPES[6]),
                18 to BattleItem(R.string.battle_name_18, BattleFieldOpposition(BattleFieldOpposition.State.STR_50), Knowledge.TYPES[6]),
                19 to BattleItem(R.string.battle_name_19, BattleFieldProgress(), Knowledge.TYPES[6]),
                20 to BattleItem(R.string.battle_name_20, BattleFieldGatheringTogether(), Knowledge.TYPES[6]),
                21 to BattleItem(R.string.battle_name_21, BattleFieldDispersion(isIndifferent = true), Knowledge.TYPES[6]),
                22 to BattleItem(R.string.battle_name_22, BattleFieldDispersion(isIndifferent = false), Knowledge.TYPES[6]),
                23 to BattleItem(R.string.battle_name_23, BattleFieldObstruction(), Knowledge.TYPES[6]),
                24 to BattleItem(R.string.battle_name_24, BattleFieldPeace(), Knowledge.TYPES[6]),
                25 to BattleItem(R.string.battle_name_25, BattleFieldRetreat(), Knowledge.TYPES[6]),
                26 to BattleItem(R.string.battle_name_26, BattleFieldSplittingApart(), Knowledge.TYPES[6]),
                27 to BattleItem(R.string.battle_name_27, BattleFieldWorkOnTheDecayed(), Knowledge.TYPES[6]),
                28 to BattleItem(R.string.battle_name_28, BattleFieldInnocence(), Knowledge.TYPES[6]),
                29 to BattleItem(R.string.battle_name_29, BattleFieldDevelopment(), Knowledge.TYPES[6]),
                30 to BattleItem(R.string.battle_name_30, BattleFieldTheFamily(whatAShameLineSaid = false), Knowledge.TYPES[6]),
                31 to BattleItem(R.string.battle_name_31, BattleFieldContemplation(), Knowledge.TYPES[6]),
                32 to BattleItem(R.string.battle_name_32, BattleFieldReturn(), Knowledge.TYPES[6]),
                33 to BattleItem(R.string.battle_name_33, BattleFieldStandstill(), Knowledge.TYPES[6]),
                34 to BattleItem(R.string.battle_name_34, BattleFieldYouthfulFolly(), Knowledge.TYPES[6]),
                35 to BattleItem(R.string.battle_name_35, BattleFieldDarkeningOfTheLight(), Knowledge.TYPES[6]),
                36 to BattleItem(R.string.battle_name_36, BattleFieldTheWanderer(TheWanderer.State.SEC_2), Knowledge.TYPES[6]),
                37 to BattleItem(R.string.battle_name_37, BattleFieldConflict(), Knowledge.TYPES[6]),
                38 to BattleItem(R.string.battle_name_38, BattleFieldComingToMeet(), Knowledge.TYPES[6]),
                39 to BattleItem(R.string.battle_name_39, nonEmptyListOf(BattleFieldDuration(), BattleFieldInfluence()), Knowledge.TYPES[6]),
                40 to BattleItem(R.string.battle_name_40, BattleFieldSmallTaming(), Knowledge.TYPES[6]),
                41 to BattleItem(R.string.battle_name_41, BattleFieldMouthCorners(hasBrokeHeart = false), Knowledge.TYPES[6]),
                42 to BattleItem(R.string.battle_name_42, BattleFieldFellowship(), Knowledge.TYPES[6]),
                43 to BattleItem(R.string.battle_name_43, BattleFieldFollowing(), Knowledge.TYPES[6]),
                44 to BattleItem(R.string.battle_name_44, BattleFieldPushingUpward(), Knowledge.TYPES[6]),
                45 to BattleItem(R.string.battle_name_45, BattleFieldEnthusiasm(), Knowledge.TYPES[6]),
                46 to BattleItem(R.string.battle_name_46, BattleFieldBreakthrough(), Knowledge.TYPES[6]),
                47 to BattleItem(R.string.battle_name_47, BattleFieldGreatPower(), Knowledge.TYPES[6]),
                48 to BattleItem(R.string.battle_name_48, BattleFieldGreatPossession(hasAttackedFirst = false, beforeD99 = false), Knowledge.TYPES[6]),
                49 to BattleItem(R.string.battle_name_49, BattleFieldTheWell(), Knowledge.TYPES[6]),
                50 to BattleItem(R.string.battle_name_50, BattleFieldDeliverance(hadADeal = false), Knowledge.TYPES[6]),
                51 to BattleItem(R.string.battle_name_51, BattleFieldTreading(alreadyFought = true), Knowledge.TYPES[6]),
                64 to BattleItem(R.string.battle_name_51_2, BattleFieldTreading(alreadyFought = false), Knowledge.TYPES[6]),
                52 to BattleItem(R.string.battle_name_52, BattleFieldTheMarryingMaiden(isFromExtras = true), Knowledge.TYPES[6]),
                53 to BattleItem(R.string.battle_name_53, BattleFieldCreativeHeaven(hasBeginning = true, isFromExtras = true), Knowledge.TYPES[6]),
                54 to BattleItem(R.string.battle_name_54, nonEmptyListOf(BattleFieldDecrease(isBrotherDead = false), BattleFieldIncrease(isBrotherDead = true)), Knowledge.TYPES[7]),
                55 to BattleItem(R.string.battle_name_55, nonEmptyListOf(BattleFieldIncrease(isBrotherDead = false), BattleFieldDecrease(isBrotherDead = true)), Knowledge.TYPES[7]),
                56 to BattleItem(R.string.battle_name_56, BattleFieldGreatTaming(), Knowledge.TYPES[7]),
                57 to BattleItem(R.string.battle_name_57, BattleFieldLimitation(), Knowledge.TYPES[7]),
                58 to BattleItem(R.string.battle_name_58, BattleFieldGrace(), Knowledge.TYPES[7]),
                59 to BattleItem(R.string.battle_name_59, BattleFieldRevolution(), Knowledge.TYPES[7]),
                60 to BattleItem(R.string.battle_name_60, BattleFieldTheArmy(), Knowledge.TYPES[7]),
                61 to BattleItem(R.string.battle_name_61, BattleFieldGreatPreponderance(), Knowledge.TYPES[7]),
                62 to BattleItem(R.string.battle_name_62, nonEmptyListOf(BattleFieldGentleWind(isFromExtras = true), BattleFieldGentleWindTainted()), Knowledge.TYPES[7]),
                63 to BattleItem(R.string.battle_name_63, nonEmptyListOf(BattleFieldJoyousLake(), BattleFieldJoyousLakeTainted()), Knowledge.TYPES[8]),
            )
    }
}