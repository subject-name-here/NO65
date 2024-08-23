package com.unicorns.invisible.no65.view

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import com.unicorns.invisible.no65.CreatorManager
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.*
import com.unicorns.invisible.no65.model.lands.RegisteredCells
import com.unicorns.invisible.no65.model.lands.cell.decor.LetterCell
import com.unicorns.invisible.no65.model.lands.cell.interactive.CellMessage
import com.unicorns.invisible.no65.model.lands.cell.service.TeleportCell
import com.unicorns.invisible.no65.model.lands.map.LandsMap
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.getColorById
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import com.unicorns.invisible.no65.view.dpad.CircleDPadMk2
import kotlin.properties.Delegates


class CreatorDrawer(
    activity: MainActivity,
    private val landsWidth: Int,
    private val landsHeight: Int,
    private val binding: ActivityCreatorBinding
): LandsFieldDrawer(activity, landsWidth, landsHeight) {
    override val landsTable: TableLayout
        get() = binding.landsTable
    override val loadingLayout: ConstraintLayout
        get() = binding.loadingLayout

    // Creator has no screen.
    override val screen: View
        get() = View(activity)

    private val itemsList: LinearLayout
        get() = binding.allCellList
    private lateinit var centerCell0: TextView
    private lateinit var centerCell1: TextView

    private val teleportRedactorBinding: TeleportRedactorBinding
        get() = binding.teleportRedactor
    private val controlRedactorBinding: ControlRedactorBinding
        get() = binding.controlRedactor
    private val messageRedactorBinding: InfoRedactorBinding
        get() = binding.messageRedactor
    private val letterCellRedactorBinding: LetterCellRedactorBinding
        get() = binding.letterCellRedactor

    private val dataPanel: ConstraintLayout
        get() = binding.dataPanel
    private val locationEditText: EditText
        get() = binding.locationName
    private val drawEraseButton: TextView
        get() = binding.drawEraseButton

    private val cdp: CircleDPadMk2
        get() = binding.cdp

    override fun initSecondary() {
        val itemsInflater = LayoutInflater.from(activity).cloneInContext(ContextThemeWrapper(activity, R.style.creator_item_style))
        RegisteredCells.cells.forEachIndexed { index, it ->
            val itemBinding = CreatorListItemBinding.inflate(itemsInflater, itemsList, true)
            itemBinding.creatorItemNumber.text = "$index"
            itemBinding.creatorItemName.text = "${it.simpleName}"
        }

        val screenCenter = Coordinates(landsHeight, landsWidth) / 2
        val centerCellWrapper = landsCells[screenCenter.row][screenCenter.col]
        val inflater = LayoutInflater.from(activity)
        val centerCellBinding = CreatorCenterCellBinding.inflate(inflater, centerCellWrapper.root, true)
        centerCell0 = centerCellBinding.coordinatesBlack
        centerCell1 = centerCellBinding.coordinatesWhite
    }

    override fun drawMap(map: LandsMap, centerCell: Coordinates) {
        super.drawMap(map, centerCell)

        launchCoroutineOnMain {
            centerCell0.text = centerCell.toString()
            centerCell1.text = centerCell.toString()
        }
    }

    var highlightedItemIndex by Delegates.observable(0) { _, old, new ->
        highlightItem(old, R.color.black)
        highlightItem(new, R.color.red)
    }
    private fun highlightItem(index: Int, colorId: Int) {
        val item = itemsList.getChildAt(index) as ConstraintLayout
        (item[1] as TextView).setTextColor(activity.getColorById(colorId))
    }

    fun setMode(mode: CreatorManager.Mode) = launchCoroutineOnMain {
        drawEraseButton.text = activity.getString(when (mode) {
            CreatorManager.Mode.DRAW -> R.string.creator_draw
            CreatorManager.Mode.ERASE -> R.string.creator_erase
        })
    }

    fun showTeleportRedactor(
        cell: TeleportCell,
        callback: (Int, Coordinates) -> Unit
    ) = launchCoroutineOnMain {
        landsTable.visibility = View.INVISIBLE
        dataPanel.visibility = View.INVISIBLE
        teleportRedactorBinding.root.visibility = View.VISIBLE

        with(teleportRedactorBinding) {
            mapIndex.setText(cell.toMapIndex.toString())
            coordinatesR.setText(cell.toCoordinates.row.toString())
            coordinatesC.setText(cell.toCoordinates.col.toString())

            saveTeleportData.setOnClickListener {
                launchCoroutineOnMain {
                    val mapIndex = mapIndex.text.toString().toInt()
                    val row = coordinatesR.text.toString().toInt()
                    val col = coordinatesC.text.toString().toInt()
                    landsTable.visibility = View.VISIBLE
                    dataPanel.visibility = View.VISIBLE
                    teleportRedactorBinding.root.visibility = View.INVISIBLE
                    callback(mapIndex, Coordinates(row, col))
                }
            }
        }
    }


    fun showControlRedactor(
        id: Int,
        callback: (Int) -> Unit
    ) = launchCoroutineOnMain {
        landsTable.visibility = View.INVISIBLE
        dataPanel.visibility = View.INVISIBLE
        controlRedactorBinding.root.visibility = View.VISIBLE

        with(controlRedactorBinding) {
            connectIndex.setText(id.toString())
            saveControlData.setOnClickListener {
                launchCoroutineOnMain {
                    val connectIndex = connectIndex.text.toString().toInt()
                    landsTable.visibility = View.VISIBLE
                    dataPanel.visibility = View.VISIBLE
                    controlRedactorBinding.root.visibility = View.INVISIBLE
                    callback(connectIndex)
                }
            }
        }
    }

    fun showMessageRedactor(cell: CellMessage, callback: (Int) -> Unit) = launchCoroutineOnMain {
        landsTable.visibility = View.INVISIBLE
        dataPanel.visibility = View.INVISIBLE
        messageRedactorBinding.root.visibility = View.VISIBLE

        with(messageRedactorBinding) {
            editTextMessage.setText(cell.messageNumber.toString())
            messageSave.setOnClickListener {
                launchCoroutineOnMain {
                    val messageNumber = editTextMessage.text.toString().toInt()
                    landsTable.visibility = View.VISIBLE
                    dataPanel.visibility = View.VISIBLE
                    messageRedactorBinding.root.visibility = View.INVISIBLE
                    callback(messageNumber)
                }
            }
        }
    }

    fun showLetterRedactor(cell: LetterCell, callback: (Char) -> Unit) = launchCoroutineOnMain {
        landsTable.visibility = View.INVISIBLE
        dataPanel.visibility = View.INVISIBLE
        letterCellRedactorBinding.root.visibility = View.VISIBLE

        with(letterCellRedactorBinding) {
            editLetter.setText(cell.symbol.toString())
            letterSave.setOnClickListener {
                launchCoroutineOnMain {
                    val letter = editLetter.text.toString().getOrNull(0) ?: '?'
                    landsTable.visibility = View.VISIBLE
                    dataPanel.visibility = View.VISIBLE
                    letterCellRedactorBinding.root.visibility = View.INVISIBLE
                    callback(letter)
                }
            }
        }
    }

    fun getLocationName(): String = locationEditText.text.toString()
    fun setLocationName(name: String) = launchCoroutineOnMain {
        locationEditText.setText(name)
    }

    fun stop() {
        cdp.stop()
    }
}