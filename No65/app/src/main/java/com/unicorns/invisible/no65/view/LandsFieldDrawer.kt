package com.unicorns.invisible.no65.view

import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.TableLayout
import android.widget.TableRow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.LandsCellBinding
import com.unicorns.invisible.no65.model.lands.cell.Cell
import com.unicorns.invisible.no65.model.lands.cell.CellUsable
import com.unicorns.invisible.no65.model.lands.cell.character.CellNonStaticCharacter
import com.unicorns.invisible.no65.model.lands.cell.character.CellTheCreature
import com.unicorns.invisible.no65.model.lands.map.LandsMap
import com.unicorns.invisible.no65.util.Coordinates
import com.unicorns.invisible.no65.util.getColorById
import com.unicorns.invisible.no65.util.getMapCoordinatesByScreenCoordinates
import com.unicorns.invisible.no65.util.launchCoroutineOnMain
import kotlinx.coroutines.delay


abstract class LandsFieldDrawer(
    override val activity: MainActivity,
    private val landsWidth: Int,
    private val landsHeight: Int
) : FadeDrawer {
    abstract val landsTable: TableLayout
    abstract val loadingLayout: ConstraintLayout
    protected val landsCells = ArrayList<ArrayList<LandsCellBinding>>()

    fun initMain() = launchCoroutineOnMain {
        val inflater = LayoutInflater.from(activity)
        val rowParam = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT,
            1f / landsHeight
        )
        val cellParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT,
            1f / landsWidth
        )

        repeat(landsHeight) { i ->
            val row = TableRow(activity)
            row.layoutParams = rowParam
            landsTable.addView(row)
            landsCells.add(ArrayList())
            repeat(landsWidth) {
                val landsCellBinding = LandsCellBinding.inflate(inflater, row, true)
                val cell = landsCellBinding.root
                cell.layoutParams = cellParams
                landsCells[i].add(landsCellBinding)
            }
        }

        initSecondary()
    }
    protected open fun initSecondary() {}

    open fun drawMap(map: LandsMap, centerCell: Coordinates) {
        repeat(landsHeight) { r ->
            repeat(landsWidth) { c ->
                val cell = map.getTopCell(
                    getMapCoordinatesByScreenCoordinates(
                        landsWidth,
                        landsHeight,
                        Coordinates(r, c),
                        centerCell
                    )
                )
                val viewBinding = landsCells[r][c]
                launchCoroutineOnMain {
                    drawCell(cell, viewBinding)
                }
            }
        }
    }

    private fun drawCell(cell: Cell, layoutViewBinding: LandsCellBinding) {
        val textView = layoutViewBinding.cellSymbol
        val characterViewBinding = layoutViewBinding.cellCharacter

        when (cell) {
            is CellNonStaticCharacter -> {
                if (textView.text != "") {
                    textView.text = ""
                }

                characterViewBinding.root.visibility = View.VISIBLE

                val cell0 = characterViewBinding.cell0
                val cell1 = characterViewBinding.cell1
                val cell2 = characterViewBinding.cell2

                if (cell1.text != cell.faceCell) {
                    cell1.text = cell.faceCell
                }

                if (cell is CellTheCreature) {
                    if (cell0.text != "" || cell2.text != "") {
                        cell0.text = ""
                        cell2.text = ""
                    }
                } else {
                    val div = cell0.text.toString().toIntOrNull() ?: 0
                    val mod = cell2.text.toString().toIntOrNull() ?: 0

                    if (div * 10 + mod != cell.id) {
                        cell0.text = "${cell.id / 10}"
                        cell2.text = "${cell.id % 10}"
                    }
                }

                val centerCell = characterViewBinding.cell4
                if (centerCell.text != cell.centerSymbol) {
                    centerCell.text = cell.centerSymbol
                }
                centerCell.setTextColor(activity.getColorById(cell.centerSymbolColor))

                val legsCell = characterViewBinding.cell7
                if (legsCell.text != cell.legsSymbol) {
                    legsCell.text = cell.legsSymbol
                }

                val leftHandCell = characterViewBinding.cell3
                val rightHandCell = characterViewBinding.cell5
                if (leftHandCell.text != cell.handSymbol) {
                    leftHandCell.text = cell.handSymbol
                    rightHandCell.text = cell.handSymbol
                }

                characterViewBinding.root.rotation = cell.rotation
            }
            else -> {
                characterViewBinding.root.visibility = View.INVISIBLE
                val textColor = activity.getColorById(cell.symbolColor)
                textView.setTextColor(textColor)

                if (cell is CellUsable) {
                    val str = SpannableString(cell.symbol.toString())
                    str.setSpan(BackgroundColorSpan(activity.getColorById(cell.backgroundColor)), 0, 1, 0)
                    textView.text = str
                    val shadowCandidates = listOf(
                        activity.getColorById(R.color.black),
                        activity.getColorById(R.color.white),
                    )
                    val shadowColor = shadowCandidates.map {
                        it to ColorUtils.calculateContrast(it, activity.getColorById(cell.backgroundColor))
                    }.maxBy { it.second }.first
                    textView.setShadowLayer(2f, 2f, 2f, shadowColor)
                } else {
                    textView.setShadowLayer(0f, 0f, 0f, activity.getColorById(R.color.transparent))

                    if (textView.text != "${cell.symbol}") {
                        textView.text = "${cell.symbol}"
                    }
                }
            }
        }

        textView.setBackgroundColor(activity.getColorById(cell.backgroundColor))
    }

    fun hideLoadingLayout(duration: Long = 1000L) = launchCoroutineOnMain {
        val animation = AlphaAnimation(1.0f, 0.0f)
        animation.duration = duration
        loadingLayout.startAnimation(animation)
        delay(duration)
        loadingLayout.visibility = View.GONE
    }

    companion object {
        const val LANDS_WEIGHT = 0.77

        // const val SYMBOL_PROPORTION = 10f / 16
        // Important: true cell proportion has width less than height,
        // since orientation is fixed portrait.
        // BUT! It's not necessary is the same as symbol proportion.
        // Therefore, if we want symbol to take all the width,
        // symbol should be at least square proportion.
        // ALSO! Consider upper and lower padding created by division error
        // (height by number of cells).
    }
}