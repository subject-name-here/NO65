package com.unicorns.invisible.no65.saveload

import android.content.Context
import com.unicorns.invisible.no65.MainActivity


class GlobalState {
    companion object {
        fun put(activity: MainActivity, key: String, value: String) {
            val sharedPref = getSharedPref(activity)
            with (sharedPref.edit()) {
                putString(key, value)
                apply()
            }
        }

        fun putBoolean(activity: MainActivity, key: String, value: Boolean) {
            val sharedPref = getSharedPref(activity)
            with (sharedPref.edit()) {
                putBoolean(key, value)
                apply()
            }
        }

        fun putInt(activity: MainActivity, key: String, value: Int) {
            val sharedPref = getSharedPref(activity)
            with (sharedPref.edit()) {
                putInt(key, value)
                apply()
            }
        }

        fun get(activity: MainActivity, key: String, default: String = ""): String {
            val sharedPref = getSharedPref(activity)
            val value = sharedPref.getString(key, default) ?: default
            return if (value == "") default else value
        }

        fun getBoolean(activity: MainActivity, key: String, default: Boolean = false): Boolean {
            val sharedPref = getSharedPref(activity)
            return sharedPref.getBoolean(key, default)
        }

        fun getInt(activity: MainActivity, key: String, default: Int = 0): Int {
            val sharedPref = getSharedPref(activity)
            return sharedPref.getInt(key, default)
        }

        fun clearWalkthroughFlags(activity: MainActivity) {
            val sharedPref = getSharedPref(activity)
            with (sharedPref.edit()) {
                for (flag in GlobalFlags.WALKTHROUGH_FLAGS) {
                    remove(flag)
                }
                apply()
            }
        }

        private fun getSharedPref(activity: MainActivity) = activity.getPreferences(Context.MODE_PRIVATE)
    }
}