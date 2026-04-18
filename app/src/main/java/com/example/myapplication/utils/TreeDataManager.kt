package com.example.myapplication.utils

import com.example.myapplication.algorithms.DecisionTreeModel
import com.example.myapplication.algorithms.Instance


object TreeDataManager {
    val decisionTree = DecisionTreeModel()

    val initialTrainingData = listOf(
        mapOf(
            "location" to "main_building",
            "budget" to "low",
            "time_available" to "medium",
            "food_type" to "full_meal",
            "queue_tolerance" to "medium",
            "weather" to "good",
            "recommended_places" to "Main_Cafeteria"
        ),
        mapOf(
            "location" to "main_building",
            "budget" to "low",
            "time_available" to "short",
            "food_type" to "snack",
            "queue_tolerance" to "low",
            "weather" to "good",
            "recommended_places" to "Yarche"
        ),
        mapOf(
            "location" to "main_building",
            "budget" to "medium",
            "time_available" to "short",
            "food_type" to "coffee",
            "queue_tolerance" to "low",
            "weather" to "good",
            "recommended_places" to "Bus_Stop_Coffee"
        ),
        mapOf(
            "location" to "main_building",
            "budget" to "high",
            "time_available" to "medium",
            "food_type" to "coffee",
            "queue_tolerance" to "medium",
            "weather" to "good",
            "recommended_places" to "Starbooks"
        ),
        mapOf(
            "location" to "second_building",
            "budget" to "low",
            "time_available" to "very_short",
            "food_type" to "snack",
            "queue_tolerance" to "low",
            "weather" to "good",
            "recommended_places" to "Vending_Machine"
        ),
        mapOf(
            "location" to "second_building",
            "budget" to "medium",
            "time_available" to "short",
            "food_type" to "coffee",
            "queue_tolerance" to "medium",
            "weather" to "good",
            "recommended_places" to "Second_Building_Cafe"
        ),
        mapOf(
            "location" to "second_building",
            "budget" to "medium",
            "time_available" to "medium",
            "food_type" to "full_meal",
            "queue_tolerance" to "medium",
            "weather" to "good",
            "recommended_places" to "Main_Cafeteria"
        ),
        mapOf(
            "location" to "second_building",
            "budget" to "low",
            "time_available" to "short",
            "food_type" to "snack",
            "queue_tolerance" to "low",
            "weather" to "bad",
            "recommended_places" to "Vending_Machine"
        ),
        mapOf(
            "location" to "campus_centre",
            "budget" to "medium",
            "time_available" to "short",
            "food_type" to "pancakes",
            "queue_tolerance" to "medium",
            "weather" to "good",
            "recommended_places" to "Siberian_Pancakes"
        )
    )

    val attributes = listOf("location", "budget", "time_available", "food_type", "queue_tolerance", "weather")
    val targetAttribute = "recommended_places"

    init {
        decisionTree.train(initialTrainingData, attributes, targetAttribute)
    }

    fun addTrainingInstance(instance: Instance) {
        decisionTree.addTrainingInstance(instance)
    }

    fun getTrainingData(): List<Instance> {
        return decisionTree.trainingData
    }

    fun resetToDefault() {
        decisionTree.train(initialTrainingData, attributes, targetAttribute)
    }
}