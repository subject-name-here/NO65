package com.unicorns.invisible.no65.util

import android.app.Activity
import com.unicorns.invisible.no65.view.lands.StringResourceForFormatWrapper


class SpeechUtils {
    companion object {
        fun processFormatArgs(argsList: List<Any>, activity: Activity): List<Any> {
            return argsList.map {
                if (it is StringResourceForFormatWrapper) {
                    activity.getString(it.id)
                } else {
                    it
                }
            }
        }
    }
}