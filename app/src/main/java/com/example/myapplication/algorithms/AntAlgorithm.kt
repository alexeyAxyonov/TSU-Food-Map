import com.example.myapplication.ui.Restourant
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

data class Result(val path: List<Int>, val length: Double)
data class RouteResult(
    val path: List<Restourant>,
    val length: Double
)
fun buildDistanceMatrix(points: List<Restourant>): Array<DoubleArray> {
    val n = points.size
    return Array(n) { i ->
        DoubleArray(n) { j ->
            if (i == j) 0.0
            else {
                val dx = points[i].coordinates.row - points[j].coordinates.row
                val dy = points[i].coordinates.col - points[j].coordinates.col
                sqrt(dx.toDouble().pow(2) + dy.toDouble().pow(2))
            }
        }
    }
}
class AntColony(
    val distances: Array<DoubleArray>,
    val alpha: Double = 1.0,
    val beta: Double = 5.0,
    val rho: Double = 0.5,
    val Q: Double = 100.0,
    val antsCount: Int = 10,
    val iterations: Int = 100
) {

    val n = distances.size
    val pheromone = Array(n) { DoubleArray(n) { 1.0 } }
    val visibility = Array(n) { DoubleArray(n) }

    init {
        for (i in 0 until n) {
            for (j in 0 until n) {
                visibility[i][j] = if (i != j) 1.0 / distances[i][j] else 0.0
            }
        }
    }

    fun solve(): Result {
        var bestPath = listOf<Int>()
        var bestLength = Double.MAX_VALUE

        repeat(iterations) {

            val allPaths = mutableListOf<Pair<List<Int>, Double>>()

            repeat(antsCount) {
                val path = buildPath()
                val length = calculateLength(path)
                allPaths.add(path to length)

                if (length < bestLength) {
                    bestLength = length
                    bestPath = path
                }
            }

            updatePheromones(allPaths)
        }

        return Result(bestPath, bestLength)
    }

    fun buildPath(): List<Int> {
        val visited = mutableSetOf<Int>()
        val path = mutableListOf<Int>()

        var current = Random.nextInt(n)
        path.add(current)
        visited.add(current)

        while (visited.size < n) {
            val next = selectNextCity(current, visited)
            path.add(next)
            visited.add(next)
            current = next
        }

        return path
    }

    fun selectNextCity(current: Int, visited: Set<Int>): Int {
        val probabilities = DoubleArray(n)
        var sum = 0.0

        for (j in 0 until n) {
            if (j !in visited) {
                val value = pheromone[current][j].pow(alpha) *
                        visibility[current][j].pow(beta)
                probabilities[j] = value
                sum += value
            }
        }

        val rand = Random.nextDouble() * sum
        var cumulative = 0.0

        for (j in 0 until n) {
            if (j !in visited) {
                cumulative += probabilities[j]
                if (cumulative >= rand) return j
            }
        }

        return (0 until n).first { it !in visited }
    }

    fun calculateLength(path: List<Int>): Double {
        var length = 0.0
        for (i in 0 until path.size - 1) {
            length += distances[path[i]][path[i + 1]]
        }
        length += distances[path.last()][path.first()] // возврат
        return length
    }

    fun updatePheromones(paths: List<Pair<List<Int>, Double>>) {

        // Испарение
        for (i in 0 until n) {
            for (j in 0 until n) {
                pheromone[i][j] *= (1 - rho)
            }
        }

        // Добавление
        for ((path, length) in paths) {
            val delta = Q / length

            for (i in 0 until path.size - 1) {
                val a = path[i]
                val b = path[i + 1]
                pheromone[a][b] += delta
                pheromone[b][a] += delta
            }
        }
    }
}
class AcoRouteSolver(
    val points: List<Restourant>
) {

    fun solve(): RouteResult {
        val distances = buildDistanceMatrix(points)

        val colony = AntColony(distances)
        val result = colony.solve()

        val realPath = result.path.map { points[it] }

        // замыкаем маршрут
        val closedPath = realPath + realPath.first()

        return RouteResult(
            path = closedPath,
            length = result.length
        )
    }
}