import android.content.Context
import com.example.myapplication.R
import java.io.BufferedReader
import java.io.InputStreamReader

data class Cell (val row: Int, val col: Int, val road: Boolean)

class MapGrid() {
    lateinit var grid: List<List<Cell>>
    var rows: Int = 0
    var cols: Int = 0

    fun loadMatrixFromFile(context: Context) {
        val inputStream = context.resources.openRawResource(R.raw.mapmatrix)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val rowsList = mutableListOf<List<Cell>>()
        var currentRow = 0

        while (true) {
            val line = reader.readLine()
            if (line == null) {
                break  // конец файла
            }

            // если строка пустая, можно пропустить
            if (line.isBlank()) {
                continue
            }

            // список строк, где каждый элемент - одно число из файла
            val numbers = line.trim().split(" ")
            val cellsInRow = mutableListOf<Cell>() //изменяемый список для всех клеток одного ряда карты

            for ((col, numStr) in numbers.withIndex()) {//создаёт для каждого числа клетку с координатой
                val num = numStr.toInt()
                val road = num == 0
                val cell = Cell(currentRow, col, road)
                cellsInRow.add(cell)
            }

            rowsList.add(cellsInRow)//добавляем весь список клеток строки в общий список всех строк
            currentRow++
        }

        reader.close()

        grid = rowsList //готовая карта для работы алгоритма
        rows = rowsList.size //св-во rows = кол-ву строк в rowsList
        cols = if (rowsList.isNotEmpty()) {//если список не пустой - размер первой строки, если пустой - 0
            rowsList[0].size
        } else {
            0
        }
    }

    fun isWalkable(row: Int, col: Int):Boolean {
        if (row < 0 || row >= rows || col < 0 || col >= cols){
            return false
        }
        return grid[row][col].road

    }

    fun getNeighbors(row: Int, col: Int): List<Cell> {

        val neighbors = mutableListOf<Cell>()

        val upRow = row - 1
        val upCol = col
        if (upRow >= 0 && isWalkable(upRow, upCol)){
            neighbors.add(grid[upRow][upCol])
        }

        val downRow = row + 1
        val downCol = col
        if (downRow < rows && isWalkable(downRow, downCol)) {
            neighbors.add(grid[downRow][downCol])
        }

        val leftRow = row
        val leftCol = col - 1
        if (leftCol >= 0 && isWalkable(leftRow, leftCol)) {
            neighbors.add(grid[leftRow][leftCol])
        }

        val rightRow = row
        val rightCol = col + 1
        if (rightCol < cols && isWalkable(rightRow, rightCol)) {
            neighbors.add(grid[rightRow][rightCol])
        }

        return neighbors
    }

    fun heuristic(row1: Int, col1: Int, row2: Int, col2: Int): Int {
        return Math.abs(row1 - row2) + Math.abs(col1 - col2)
    }

    fun findPath(startRow: Int, startCol: Int, targetRow: Int, targetCol: Int): List<Cell>?{//Возвращает список клеток пути или null, если пути нет
        // Проверяем, что старт и цель внутри карты и проходимы
        if (!isWalkable(startRow, startCol) || !isWalkable(targetRow, targetCol)) {
            return null
        }

        // Стартовый узел
        val startCell = grid[startRow][startCol]
        val startNode = Node(
            cell = startCell,
            g = 0,
            h = heuristic(startRow, startCol, targetRow, targetCol),
            parent = null
        )

        val openSet = mutableListOf<Node>()//список с узлами, которые нужно проверить
        val closedSet = mutableSetOf<Node>()//список с узлами, которые уже проверили

        openSet.add(startNode)

        while (openSet.isNotEmpty()) {
            // Находим в openSet узел, у которого f минимальный
            var currentNode = openSet[0]
            for (node in openSet) {
                if (node.f < currentNode.f) {
                    currentNode = node
                }
            }

            // Если дошли до цели
            if (currentNode.cell.row == targetRow && currentNode.cell.col == targetCol) {//Если координаты текущего узла совпадают с целью — путь найден
                // Восстановить путь
                val path = mutableListOf<Cell>()//Идём от целевого узла по parent до null, собираем клетки, потом переворачиваем список (от старта к цели)
                var node: Node? = currentNode
                while (node != null) {
                    path.add(node.cell)
                    node = node.parent
                }
                return path.reversed()
            }

            // Переместить currentNode из openSet в closedSet
            openSet.remove(currentNode)
            closedSet.add(currentNode)

            // Обработка соседей
            for (neighbor in getNeighbors(currentNode.cell.row, currentNode.cell.col)) {
                // Если сосед уже в closedSet — пропустить
                if (closedSet.any { it.cell.row == neighbor.row && it.cell.col == neighbor.col }) {//Есть ли в closedSet такой узел, у которого координаты row и col совпадают с координатами соседа?
                    continue
                }

                val newG = currentNode.g + 1 //текущий g+1

                // Найти узел соседа в openSet
                val existingNode = openSet.find { it.cell.row == neighbor.row && it.cell.col == neighbor.col }

                if (existingNode == null) {
                    // Создать новый узел
                    val neighborNode = Node(
                        cell = neighbor,
                        g = newG,
                        h = heuristic(neighbor.row, neighbor.col, targetRow, targetCol),
                        parent = currentNode
                    )
                    openSet.add(neighborNode)
                } else if (newG < existingNode.g) {
                    // Обновляем существующий узел
                    existingNode.g = newG
                    existingNode.parent = currentNode
                }
            }
        }
        return null
    }
}

class Node (var cell: Cell, var g: Int, var h: Int, var parent: Node?){
    val f: Int //вычисляемое свойство
        get() = g+h
}