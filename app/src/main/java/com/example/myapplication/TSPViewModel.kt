package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.algorithms.GeneticAlgorithm
import com.example.myapplication.algorithms.MapGrid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

class TSPViewModel : ViewModel() {

    // Список всех доступных продуктов (из DataModels.kt)
    private val _allProducts = MutableStateFlow(ALL_PRODUCTS)
    val allProducts: StateFlow<List<Product>> = _allProducts.asStateFlow()

    // Выбранные пользователем продукты
    private val _selectedProducts = MutableStateFlow<Set<Product>>(emptySet())
    val selectedProducts: StateFlow<Set<Product>> = _selectedProducts.asStateFlow()

    // Лучший найденный маршрут
    private val _bestRoute = MutableStateFlow<List<Shop>>(emptyList())
    val bestRoute: StateFlow<List<Shop>> = _bestRoute.asStateFlow()

    // Общее время маршрута (в мин)
    private val _totalTime = MutableStateFlow(0.0)
    val totalTime: StateFlow<Double> = _totalTime.asStateFlow()

    // Загружается ли алгоритм сейчас
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Текущее поколение
    private val _currentGeneration = MutableStateFlow(0)
    val currentGeneration: StateFlow<Int> = _currentGeneration.asStateFlow()

    // Сообщение об ошибке
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Список всех заведений
    private val allShops = createTestShops()

    // Карта для A*
    private val mapGrid = MapGrid()

    //Выбрать или снять выбор с продукта
    fun toggleProductSelection(product: Product) {
        val currentSet = _selectedProducts.value.toMutableSet()
        if (currentSet.contains(product)) {
            currentSet.remove(product)
        } else {
            currentSet.add(product)
        }
        _selectedProducts.value = currentSet
        Log.d("TSPViewModel", "Выбрано продуктов: ${currentSet.size}")
    }

    //Очистить все выбранные продукты
    fun clearSelection() {
        _selectedProducts.value = emptySet()
    }

    //Очистить сообщение об ошибке
    fun clearError() {
        _errorMessage.value = null
    }

    //Запустить генетический алгоритм
    fun startAlgorithm(context: Context) {
        val selected = _selectedProducts.value

        // Выбраны ли продукты
        if (selected.isEmpty()) {
            _errorMessage.value = "Выберите хотя бы один продукт"
            return
        }

        // Очищение предыдущей ошибки
        _errorMessage.value = null
        _isLoading.value = true
        _bestRoute.value = emptyList()
        _currentGeneration.value = 0

        viewModelScope.launch {
            try {
                // Поиск заведений, где есть выбранные продукты
                val requiredShops = findShopsWithProducts(selected)

                Log.d("TSPViewModel", "Нужно посетить заведений: ${requiredShops.size}")

                // Если все продукты есть в одном заведении
                if (requiredShops.size == 1) {
                    _bestRoute.value = requiredShops
                    _totalTime.value = 0.0
                    _isLoading.value = false
                    return@launch
                }

                // Если заведений несколько — запускаем алгоритм
                if (requiredShops.size > 1) {
                    // Загрузка карты (если ещё не загружена)
                    if (!mapGrid.isLoaded) {
                        withContext(Dispatchers.IO) {
                            mapGrid.loadMatrixFromFile(context)
                        }
                    }

                    // Рассчёт матрицы расстояний
                    val distanceMatrix = calculateDistanceMatrix(requiredShops)

                    // Создание и запуск ген алгоритма
                    val ga = GeneticAlgorithm(
                        shopsToVisit = requiredShops,
                        distanceMatrix = distanceMatrix,
                        currentTime = LocalTime.now(),
                        populationSize = 50,
                        mutationRate = 0.1
                    )

                    // Запуск эволюции с отображением прогресса
                    var population = ga.createInitialPopulation()

                    repeat(100) { generation ->
                        _currentGeneration.value = generation + 1

                        // Находим лучшего в текущем поколении
                        val best = population.minBy { it.fitness }
                        _bestRoute.value = best.genes
                        _totalTime.value = best.fitness

                        // Эволюция (одно поколение)
                        population = ga.evolveOneGeneration(population)

                        // Небольшая задержка, чтобы UI успевал обновляться
                        delay(50)
                    }

                    // Финальный лучший маршрут
                    val finalBest = population.minBy { it.fitness }
                    _bestRoute.value = finalBest.genes
                    _totalTime.value = finalBest.fitness

                    Log.d("TSPViewModel", "Алгоритм завершён. Лучший фитнес: ${finalBest.fitness}")
                }

            } catch (e: Exception) {
                Log.e("TSPViewModel", "Ошибка: ${e.message}", e)
                _errorMessage.value = "Ошибка: ${e.message}"
            } finally {
                _isLoading.value = false
                _currentGeneration.value = 0
            }
        }
    }

    //Находит заведения, в которых есть хотя бы один из выбранных продуктов
    private fun findShopsWithProducts(selectedProducts: Set<Product>): List<Shop> {
        return allShops.filter { shop ->
            // Заведение подходит, если в его меню есть ХОТЯ БЫ ОДИН выбранный продукт
            shop.menu.any { menuProduct ->
                selectedProducts.any { selectedProduct ->
                    menuProduct.name == selectedProduct.name
                }
            }
        }.distinctBy { it.id }  // Убираем дубликаты по ID
    }

    //Рассчёт матрицы времени в пути между всеми парами заведений
    //Время в минутах, исходя из скорости 5 км/ч
    private suspend fun calculateDistanceMatrix(shops: List<Shop>): Map<Pair<Shop, Shop>, Double> {
        val matrix = mutableMapOf<Pair<Shop, Shop>, Double>()

        // 1 клетка примерно 1 метр
        // Скорость 5 км/ч = 5000 м/ч = 83.33 м/мин
        val metersPerMinute = 83.33

        for (shopA in shops) {
            for (shopB in shops) {
                if (shopA.id == shopB.id) {
                    matrix[Pair(shopA, shopB)] = 0.0
                    continue
                }

                // Поиск пути через A*
                val path = withContext(Dispatchers.IO) {
                    mapGrid.findPath(shopA.row, shopA.col, shopB.row, shopB.col)
                }

                // Длина пути в клетках (~м)
                val distanceMeters = (path?.size ?: 1000).toDouble()

                // Перевод в мин
                val timeMinutes = distanceMeters / metersPerMinute

                matrix[Pair(shopA, shopB)] = timeMinutes
            }
        }

        return matrix
    }
}