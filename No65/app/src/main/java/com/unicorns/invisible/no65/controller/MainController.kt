package com.unicorns.invisible.no65.controller

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityMainBinding
import com.unicorns.invisible.no65.databinding.CreatedMapsItemBinding
import com.unicorns.invisible.no65.init.BattleItem
import com.unicorns.invisible.no65.saveload.SaveManager
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class MainController(
    override val activity: MainActivity,
    private val continueListener: () -> Unit,
    private val newGameListener: () -> Unit,
    private val replayListener: () -> Unit,
    private val redactMapListener: () -> Unit,
    private val playMapListener: () -> Unit,
    private val volumeListener: () -> Unit,
    private val helpTrigramsListener: () -> Unit,
    private val attributionsListener: () -> Unit,
    private val aboutListener: () -> Unit,
) : MenuItemController() {
    private val binding: ActivityMainBinding
        get() = activity.binding as ActivityMainBinding

    override val buttonsBlockingToListeners: Map<TextView, () -> Unit>
        get() = mapOf(
            binding.continueButton to continueListener,
            binding.newGameButton to newGameListener,
            binding.helpTrigrams to helpTrigramsListener,
            binding.attributions to attributionsListener,
            binding.about to aboutListener,
        )

    override val buttonsNonBlockingToListeners: Map<TextView, () -> Unit>
        get() = mapOf(
            binding.redactMap to redactMapListener,
            binding.playMap to playMapListener,
            binding.replayBattle to replayListener
        )

    override val buttonsFreeToListeners: Map<TextView, () -> Unit>
        get() = mapOf(
            binding.volumeModeButton to volumeListener
        )

    fun replayBattlesListListener(
        onItemSelected: (BattleItem) -> Unit
    ) {
        val createdMaps = binding.createdMaps
        createdMaps.removeAllViews()
        createdMaps.visibility = View.VISIBLE

        val battlesRaw = SaveManager.loadBattles(activity)
        val battlesFlags = battlesRaw.split(" ").map { it.toIntOrNull() }
        val itemToIsBlocking = BattleItem.BATTLES_LIST.mapIndexed { counter, (index, item) ->
            if (battlesFlags.getOrNull(index) == 1) {
                val name = activity.getString(item.nameId)
                "${counter}. $name" to true
            } else {
                activity.getString(R.string.battle_name_unknown) to false
            }
        }

        itemToIsBlocking.forEachIndexed { index, (name, isBlocking) ->
            val itemBinding = CreatedMapsItemBinding.inflate(activity.layoutInflater, createdMaps, true)
            val newItem = itemBinding.root
            newItem.text = name
            if (isBlocking) {
                setBlockingListener(newItem) { onItemSelected(BattleItem.BATTLES_LIST[index].second) }
            } else {
                setNonBlockingListener(newItem) {}
            }
        }
    }

    fun createdMapsListListener(
        mode: MainActivity.CreatedMapsMode,
        mapsNames: List<String>,
        onItemSelectedListener: (Int, MainActivity.CreatedMapsMode) -> Unit
    ) = launchCoroutineOnMain {
        val createdMaps = binding.createdMaps
        createdMaps.removeAllViews()
        createdMaps.visibility = View.VISIBLE

        if (mode == MainActivity.CreatedMapsMode.REDACT) {
            addCreatedMap(
                createdMaps,
                activity.getText(R.string.empty_map).toString(),
                -1,
                mode,
                onItemSelectedListener
            )
        }

        mapsNames.forEachIndexed { index, name ->
            addCreatedMap(
                createdMaps,
                name,
                index,
                mode,
                onItemSelectedListener
            )
        }
    }
    private fun addCreatedMap(
        mapList: LinearLayout,
        name: String,
        index: Int,
        mode: MainActivity.CreatedMapsMode,
        onItemSelectedListener: (Int, MainActivity.CreatedMapsMode) -> Unit
    ) {
        val itemBinding = CreatedMapsItemBinding.inflate(activity.layoutInflater, mapList, true)
        val newItem = itemBinding.root
        newItem.text = name
        setBlockingListener(newItem) { onItemSelectedListener(index, mode) }
    }
}