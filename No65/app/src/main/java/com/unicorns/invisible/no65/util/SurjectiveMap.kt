package com.unicorns.invisible.no65.util

// Multimap with one neat property: every value is unique.
class SurjectiveMap<K, V> {
    private val map = HashMap<V, K>()

    fun put(key: K, value: V) {
        map[value] = key
    }
    operator fun get(key: K): List<V> {
        return map
            .filter { it.value == key }
            .map { it.key }
    }
    val values: MutableCollection<V>
        get() = map.keys

    fun containsValue(value: V): Boolean {
        return map.containsKey(value)
    }
    fun removeKey(key: K) {
        map
            .filter { it.value == key }
            .forEach { map.remove(it.key) }
    }
    fun removeValue(value: V) {
        map.remove(value)
    }

    fun clear() {
        map.clear()
    }
}