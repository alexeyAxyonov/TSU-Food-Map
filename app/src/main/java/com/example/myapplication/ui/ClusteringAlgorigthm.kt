package com.example.myapplication.ui

import Cell

// Определение начальных кластеров - выборка далеких
// Определение метрики - евклидова
//
// Распределение обьектов по кластерам
// Пересчет центров кластеров - через среднее значение внутри кластера
//
// Центры кластеров не двигаются - конец
// Сохранение кластеров
//

// будет какой то список Cell,  в нем уже готовые n-точек, и у нас есть k заранее известного
// кол-ва кластеров, для вывода кластера - создаем k - списков.

data class ClusteringResult (
    val centroids : List<Cell>,
    val clusters : List<List<Cell>>
)
fun findRandomPoint(points : List<Cell>) : Cell
{
    return points.random()
}
fun metric(point : Cell, centroid : Cell) : Int {
    return (point.row - centroid.row)  * (point.row - centroid.row) +
            (point.col - centroid.col) * (point.col - centroid.col)
}

val numberOfCluster : Int = 6
val points = Cell(5,3, true)

fun findClusters (numberOfCluster : Int, points : List<Cell>) : ClusteringResult
{
    val clusters = MutableList(numberOfCluster) { mutableListOf<Cell>() }
    val centroids = mutableListOf<Cell>()
    val pointsMutable = points.toMutableList()

    //Задание эвристики - первых центроидов
    val weigths = mutableListOf<Int>()

    var firstCentroid = findRandomPoint(points)
    centroids.add(firstCentroid)
    pointsMutable.remove(firstCentroid)

    while (centroids.size < numberOfCluster) {
        var nextCentroid: Cell
        weigths.clear()
        //заполнили веса для вероятности
        pointsMutable.forEach { point ->
            var minDistance = centroids.minOf { centroid ->
                metric(point, centroid)
            }

            weigths.add(minDistance)
        }
        //выбор по случайности где отдается приоритет самым дальним точкам
        val totalWeigth = weigths.sum()
        val randomPoint = Math.random() * totalWeigth

        var cumulative = 0.0
        nextCentroid = pointsMutable.first() // просто задание нужно для предотвращения
                                            // ошибки если вдруг цикл не найдет nextCentroid
        for ((index,point) in pointsMutable.withIndex()) {
            cumulative += weigths[index]
            if (randomPoint <= cumulative) {
                nextCentroid = point
                break
            }
        }
        centroids.add(nextCentroid)
        pointsMutable.remove(nextCentroid)
    }

    //Сам  цикл - уточнения и скучкивания кластеров//
    while (true) {
        //так как центроиды поменялись => обновляем для них кластеры и веса
        clusters.forEach { cluster ->
            cluster.clear()
        }

        //сохраняем позицию старых центроидов и при их изменении продолжаем скучкивать кластеры
        val oldCentroids = centroids.toList()

        //добавление в каждый кластер все точки имеющие мин расстояние до центроида:
        points.forEach { point ->

            //нахождение ближайшего центроида у одной точки
            var nearestCentroid = centroids.minByOrNull { metric(point, it) } !!
            var indexCentroid = centroids.indexOf(nearestCentroid)
            clusters[indexCentroid].add(point)
        }

        //перезаписывание центроида как среднего арифметического координат его кластера
        for (index in centroids.indices) {
            val cluster = clusters[index]
            if (cluster.isNotEmpty()) {
                val averageClusterRow = cluster.sumOf { it.row } / cluster.size
                val averageClusterCol = cluster.sumOf { it.col } / cluster.size

                //перезаписывание нового центроида для кластера
                centroids[index] = Cell( averageClusterRow,
                                         averageClusterCol,
                                         false )
            }
        }
        //если центроиды не поменялись => кластеры готовы
        if (oldCentroids == centroids)
            break
    }
    return ClusteringResult( centroids, clusters )
}