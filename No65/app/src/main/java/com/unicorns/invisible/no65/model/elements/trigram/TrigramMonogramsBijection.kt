package com.unicorns.invisible.no65.model.elements.trigram

import com.unicorns.invisible.no65.model.elements.ThreeMonograms
import com.unicorns.invisible.no65.model.elements.monogram.Yang
import com.unicorns.invisible.no65.model.elements.monogram.Yin

class TrigramMonogramsBijection {
    companion object {
        fun getTrigram(monograms: ThreeMonograms): Trigram {
            return when (monograms.first) {
                Yin -> {
                    when (monograms.second) {
                        Yin -> {
                            when (monograms.third)  {
                                Yin -> Earth
                                Yang -> Thunder
                            }
                        }
                        Yang -> {
                            when (monograms.third) {
                                Yin -> Water
                                Yang -> Lake
                            }
                        }
                    }
                }
                Yang -> {
                    when (monograms.second) {
                        Yin -> {
                            when (monograms.third)  {
                                Yin -> Mountain
                                Yang -> Fire
                            }
                        }
                        Yang -> {
                            when (monograms.third) {
                                Yin -> Wind
                                Yang -> Heaven
                            }
                        }
                    }
                }
            }
        }

        fun getMonograms(trigram: Trigram): ThreeMonograms {
            return when (trigram) {
                Heaven ->   ThreeMonograms(Yang, Yang, Yang)
                Lake ->     ThreeMonograms(Yin,  Yang, Yang)
                Fire ->     ThreeMonograms(Yang, Yin,  Yang)
                Wind ->     ThreeMonograms(Yang, Yang, Yin)
                Earth ->    ThreeMonograms(Yin,  Yin,  Yin)
                Mountain -> ThreeMonograms(Yang, Yin,  Yin)
                Water ->    ThreeMonograms(Yin,  Yang, Yin)
                Thunder ->  ThreeMonograms(Yin,  Yin,  Yang)
            }
        }
    }
}