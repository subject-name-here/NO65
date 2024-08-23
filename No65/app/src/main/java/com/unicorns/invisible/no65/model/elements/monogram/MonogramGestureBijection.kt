package com.unicorns.invisible.no65.model.elements.monogram

import com.unicorns.invisible.no65.controller.ElementsGestureType

class MonogramGestureBijection {
    companion object {
        fun getMonogram(type: ElementsGestureType): Monogram {
            return when (type) {
                ElementsGestureType.SWIPE -> Yang
                ElementsGestureType.DOUBLE_TAP -> Yin
            }
        }

        fun getGestureType(monogram: Monogram): ElementsGestureType {
            return when (monogram) {
                Yang -> ElementsGestureType.SWIPE
                Yin -> ElementsGestureType.DOUBLE_TAP
            }
        }
    }
}