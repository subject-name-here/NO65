package com.unicorns.invisible.no65.view

import android.view.View
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.databinding.ActivityTrigramsBinding
import com.unicorns.invisible.no65.model.elements.trigram.*
import com.unicorns.invisible.no65.model.knowledge.Knowledge
import com.unicorns.invisible.no65.util.launchCoroutineOnMain


class TrigramsSheetDrawer(
    override val activity: MainActivity,
    private val binding: ActivityTrigramsBinding
) : FadeDrawer {
    override val screen: View
        get() = binding.screen

    private val trigramToTextView = mapOf(
        Water to binding.waterEffect,
        Wind to binding.windEffect,
        Lake to binding.lakeEffect,
        Fire to binding.fireEffect,
        Mountain to binding.mountainEffect,
        Thunder to binding.thunderEffect,
        Earth to binding.earthEffect,
        Heaven to binding.heavenEffect,
    )
    private val trigramToRequiemText = mapOf(
        Wind to R.string.trigrams_effect_dp_x_inf,
        Lake to R.string.trigrams_effect_hp_max
    )

    fun updateFromKnowledge(knowledge: Knowledge) = launchCoroutineOnMain {
        trigramToTextView.forEach {
            val trigram = it.key
            val view = it.value
            when {
                knowledge.knowsRequiem(trigram) -> {
                    val stringId = trigramToRequiemText[trigram] ?: R.string.unknown_trigram
                    view.text = activity.getString(stringId)
                }
                !knowledge.knowsTrigram(trigram) -> {
                    view.text = activity.getString(R.string.unknown_trigram)
                }
            }
        }
    }
}