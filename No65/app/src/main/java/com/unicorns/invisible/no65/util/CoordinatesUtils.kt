package com.unicorns.invisible.no65.util


fun getMapCoordinatesByScreenCoordinates(
    screenWidth: Int,
    screenHeight: Int,
    screenCoordinates: Coordinates,
    mapCenterCoordinates: Coordinates
): Coordinates {
    val screenCenterCoordinates = Coordinates(screenHeight / 2, screenWidth / 2)
    return mapCenterCoordinates - screenCenterCoordinates + screenCoordinates
}