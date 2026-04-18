package com.example.myapplication.algorithms

import com.example.myapplication.Shop
import com.example.myapplication.isOpenNow
import com.example.myapplication.minutesUntilClose
import java.time.LocalTime

data class Individual(
    val genes: List<Shop>,
    val fitness: Double//приспособленность
)

class GeneticAlgorithm(
    private val shopsToVisit: List<Shop>,//список мест, которые нужно посетить
    private val distanceMatrix: Map<Pair<Shop, Shop>, Double>,//матрица расстояний, хранит время в пути между любыми двумя магазинами
    private val currentTime: LocalTime,//текущее время
    private val populationSize: Int = 50,//размер популяции
    private val mutationRate: Double = 0.1//вероятность мутации
) {

    // ФИТНЕС-ФУНКЦИЯ
    private fun calculateFitness(route: List<Shop>): Double {
        // Если маршрут пустой — возвращаем "бесконечность" (очень плохо)
        if (route.isEmpty()) return Double.MAX_VALUE

        var totalTime = 0.0  // Общее время в минутах (накапливаем)

        // Проходим по маршруту от первого до последнего магазина
        for (i in 0 until route.size - 1) {
            val currentShop = route[i]      // Текущий магазин
            val nextShop = route[i + 1]     // Следующий магазин

            // Достаём время в пути из матрицы расстояний
            val travelTime = distanceMatrix[Pair(currentShop, nextShop)]

            // Если пути нет — большой штраф
            if (travelTime == null) {
                totalTime += 1000.0
            } else {
                totalTime += travelTime  // Прибавляем реальное время
            }
        }

        // ШТРАФЫ И БОНУСЫ
        var penalty = 0.0

        for (shop in route) {
            // ШТРАФ: Магазин закрыт сейчас
            if (!shop.isOpenNow(currentTime)) {
                penalty += 500.0  // +500 минут — огромный штраф
            }

            // БОНУС: До закрытия меньше 30 минут — приоритет
            val minutesToClose = shop.minutesUntilClose(currentTime)
            if (minutesToClose in 1..30) {
                penalty -= 100.0
            }
        }

        // Итоговая оценка = время в пути + штрафы
        return totalTime + penalty
    }

    // НАЧАЛЬНАЯ ПОПУЛЯЦИЯ
    fun createInitialPopulation(): List<Individual> {
        return List(populationSize) {
            //для каждой особи перемешиваем магазины в случайном порядке
            val shuffledGenes = shopsToVisit.shuffled()
            //считаем фитнес для этого порядка
            val fitness = calculateFitness(shuffledGenes)
            //создаём особь
            Individual(shuffledGenes, fitness)
        }
    }

    // ТУРНИРНАЯ СЕЛЕКЦИЯ
    fun tournamentSelection(population: List<Individual>): Individual {
        // Размер турнира — сколько особей соревнуются
        val tournamentSize = 3

        // Выбираем случайную особь как начального "чемпиона"
        var best = population.random()

        // Повторяем для остальных участников турнира
        repeat(tournamentSize - 1) {
            // Выбираем случайного соперника
            val contender = population.random()
            // Если у соперника фитнес МЕНЬШЕ (лучше) — он чемпион
            if (contender.fitness < best.fitness) {
                best = contender
            }
        }
        // Возвращаем победителя
        return best
    }

    // ORDERED CROSSOVER
    private fun crossover(parent1: Individual, parent2: Individual): Individual {
        val size = parent1.genes.size

        // Если маршрут состоит из 1 магазина — скрещивать нечего, возвращаем копию родителя
        if (size <= 1) {
            return parent1
        }

        //Выбираем случайный отрезок
        val start = (0 until size).random()  // случайный начальный индекс
        val end = (start until size).random() // случайный конечный индекс (не меньше start)

        //Создаём массив для генов ребёнка
        val childGenes = MutableList<Shop?>(size) { null }

        //Копируем отрезок от Родителя 1
        for (i in start..end) {
            childGenes[i] = parent1.genes[i]
        }

        //Заполняем пустые места генами Родителя 2 по порядку
        var parent2Index = 0
        for (i in 0 until size) {
            // Если место уже занято — пропускаем
            if (childGenes[i] != null) continue

            // Ищем следующий ген из parent2, которого ещё нет в ребёнке
            while (parent2.genes[parent2Index] in childGenes) {
                parent2Index++
            }

            //Заполняем пустое место
            childGenes[i] = parent2.genes[parent2Index]
            parent2Index++
        }

        //Преобразуем List<Shop?> в List<Shop>
        val finalGenes = childGenes.filterNotNull()

        //Создаём и возвращаем ребёнка с посчитанным фитнесом
        return Individual(finalGenes, calculateFitness(finalGenes))
    }

    //МУТАЦИЯ
    private fun mutate(individual: Individual): Individual {
        //Проверяем, произойдёт ли мутация
        //Возвращаем число от 0.0 до 1.0
        //Если число БОЛЬШЕ — мутация НЕ происходит
        if (kotlin.random.Random.nextDouble() > mutationRate) {
            return individual  // Возвращаем особь без изменений
        }

        //Если маршрут состоит из 1 или 0 магазинов — мутировать нечего
        if (individual.genes.size <= 1) {
            return individual
        }

        //Создаём изменяемую копию генов
        val mutatedGenes = individual.genes.toMutableList()

        //Выбираем два случайных индекса
        val index1 = (0 until mutatedGenes.size).random()
        var index2 = (0 until mutatedGenes.size).random()

        //Индексы дб разные
        while (index2 == index1) {
            index2 = (0 until mutatedGenes.size).random()
        }

        // Меняем местами элементы
        val temp = mutatedGenes[index1]
        mutatedGenes[index1] = mutatedGenes[index2]
        mutatedGenes[index2] = temp

        // Создаём новую особь с мутированными генами и пересчитанным фитнесом
        return Individual(mutatedGenes, calculateFitness(mutatedGenes))
    }

    // ЭВОЛЮЦИЯ
    fun evolve(generations: Int = 100): List<Individual> {
        // Начальная популяция
        var population = createInitialPopulation()

        // Список для хранения лучшей особи каждого поколения
        val history = mutableListOf<Individual>()

        // Цикл по поколениям
        repeat(generations) { generation ->
            // Находим лучшую особь текущего поколения
            val bestOfGeneration = population.minBy { it.fitness }
            history.add(bestOfGeneration)

            // Создаём новую популяцию
            val newPopulation = mutableListOf<Individual>()

            //ЭЛИТИЗМ
            // Сохраняем 2 лучшие особи без изменений
            // Они гарантированно переходят в следующее поколение
            val elites = population.sortedBy { it.fitness }.take(2)
            newPopulation.addAll(elites)

            //Заполнение остальных мест
            while (newPopulation.size < populationSize) {
                // Отбираем двух родителей через турнирную селекцию
                val parent1 = tournamentSelection(population)
                val parent2 = tournamentSelection(population)

                // Скрещиваем родителей и получаем ребёнка
                var child = crossover(parent1, parent2)

                // Применяем мутацию к ребёнку
                child = mutate(child)

                // Добавляем ребёнка в новую популяцию
                newPopulation.add(child)
            }

            // Заменяем старую популяцию новой
            population = newPopulation
        }

        // Добавляем лучшего из последнего поколения
        val finalBest = population.minBy { it.fitness }
        history.add(finalBest)

        // Возвращаем историю лучших особей
        return history
    }

    // Одно поколение эволюции
    fun evolveOneGeneration(population: List<Individual>): List<Individual> {
        val newPopulation = mutableListOf<Individual>()

        // Элитизм — сохраняем 2 лучшие особи
        val elites = population.sortedBy { it.fitness }.take(2)
        newPopulation.addAll(elites)

        // Заполняем остальные места
        while (newPopulation.size < populationSize) {
            val parent1 = tournamentSelection(population)
            val parent2 = tournamentSelection(population)
            var child = crossover(parent1, parent2)
            child = mutate(child)
            newPopulation.add(child)
        }

        return newPopulation
    }
}