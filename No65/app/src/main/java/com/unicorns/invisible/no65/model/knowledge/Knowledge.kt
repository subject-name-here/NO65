package com.unicorns.invisible.no65.model.knowledge

import com.unicorns.invisible.no65.model.elements.trigram.Lake
import com.unicorns.invisible.no65.model.elements.trigram.Trigram
import com.unicorns.invisible.no65.model.elements.trigram.Wind
import kotlinx.serialization.Serializable


@Serializable
class Knowledge {
    private val basicTrigrams = KnowledgeMap()
    private val requiems = KnowledgeMap()

    fun knowsTrigram(trigram: Trigram) = basicTrigrams.knows(trigram)
    fun learnTrigram(trigram: Trigram) = basicTrigrams.learn(trigram)
    fun knowsRequiem(trigram: Trigram) = requiems.knows(trigram)
    fun learnRequiem(trigram: Trigram) = requiems.learn(trigram)

    fun setAllBasics() {
        basicTrigrams.setAll()
    }
    fun setWindRequiem() {
        requiems.learn(Wind)
    }
    fun setLakeRequiem() {
        requiems.learn(Lake)
    }
}