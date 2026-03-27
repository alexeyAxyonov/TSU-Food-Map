package com.example.myapplication

data class MapPoint(val row : Int,
                    val col : Int
) {
}



fun findMyPath(): List<MapPoint> {
    val myPath = listOf<MapPoint>(
        MapPoint(row = 2, col = 3),
        MapPoint(row = 10, col = 12),
        MapPoint(row = 100, col = 21),
        MapPoint(row = 30, col = 300)
    )
    return myPath
}