package com.unicorns.invisible.no65.model.knowledge

import com.unicorns.invisible.no65.model.elements.trigram.*
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

    companion object {
        val TYPES = listOf(
            Knowledge(),
            Knowledge().apply { learnTrigram(Water) },
            Knowledge().apply { learnTrigram(Water); learnTrigram(Wind); learnTrigram(Lake) },
            Knowledge().apply { learnTrigram(Water); learnTrigram(Wind); learnTrigram(Lake); learnTrigram(Fire) },
            Knowledge().apply { learnTrigram(Water); learnTrigram(Wind); learnTrigram(Lake); learnTrigram(Fire); learnTrigram(Mountain) },
            Knowledge().apply { learnTrigram(Water); learnTrigram(Wind); learnTrigram(Lake); learnTrigram(Fire); learnTrigram(Mountain); learnTrigram(Thunder) },
            Knowledge().apply { learnTrigram(Water); learnTrigram(Wind); learnTrigram(Lake); learnTrigram(Fire); learnTrigram(Mountain); learnTrigram(Thunder); learnTrigram(Earth) },
            Knowledge().apply { setAllBasics() },
            Knowledge().apply { setAllBasics(); setWindRequiem() },
            Knowledge().apply { setAllBasics(); setWindRequiem(); setLakeRequiem() },
        )
    }
}