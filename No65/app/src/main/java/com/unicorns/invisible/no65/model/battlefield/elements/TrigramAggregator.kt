package com.unicorns.invisible.no65.model.battlefield.elements

import com.unicorns.invisible.no65.model.elements.ThreeMonograms
import com.unicorns.invisible.no65.model.elements.monogram.Monogram
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.TrigramMonogramsBijection

class TrigramAggregator {
    private var monograms = ArrayList<Monogram>()
    fun getLastMonogramNumber() = monograms.size - 1

    fun take(monogram: Monogram) {
        monograms.add(monogram)
    }

    fun clear() {
        monograms.clear()
    }

    fun hasTrigram(): Boolean {
        return monograms.size == 3
    }

    fun getTrigram(): Trigram? {
        if (monograms.size < 3) {
            return null
        }

        val threeMonograms = ThreeMonograms(monograms[0], monograms[1], monograms[2])
        return TrigramMonogramsBijection.getTrigram(threeMonograms)
    }
}