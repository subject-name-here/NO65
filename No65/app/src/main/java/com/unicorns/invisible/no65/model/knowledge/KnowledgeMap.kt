package com.unicorns.invisible.no65.model.knowledge

import com.unicorns.invisible.no65.model.elements.trigram.*
import kotlinx.serialization.Serializable


@Serializable
class KnowledgeMap {
    private val knowledgeMap = mutableMapOf(
        Water to false,
        Wind to false,
        Thunder to false,
        Mountain to false,
        Lake to false,
        Heaven to false,
        Fire to false,
        Earth to false
    )

    fun knows(trigram: Trigram) = knowledgeMap[trigram] ?: false
    fun learn(trigram: Trigram) {
        knowledgeMap[trigram] = true
    }

    fun setAll() {
        knowledgeMap.entries.forEach { it.setValue(true) }
    }
}