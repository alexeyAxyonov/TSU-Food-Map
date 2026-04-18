package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.myapplication.Home
import com.example.myapplication.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.myapplication.AddTrainingData
import com.example.myapplication.algorithms.DecisionTreeModel
import com.example.myapplication.utils.TreeDataManager

val TRAINING_DATA = listOf(
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

val ATTRIBUTES = listOf("location", "budget", "time_available", "food_type", "queue_tolerance", "weather")

/*
object DecisionTreePredictor {
    private val tree: DecisionTreeModel by lazy {
        DecisionTreeModel().apply {
            train(TRAINING_DATA, ATTRIBUTES, "recommended_places")
        }
    }

    fun predict(selections: Map<String, String>): String? {
        return tree.predict(selections)
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeScreen(
    navController: NavController
) {
    /*
    val decisionTree = remember {
        DecisionTreeModel().apply {
            train(TRAINING_DATA, ATTRIBUTES, "recommended_places")
        }
    }
     */

    var selectedLocation by remember { mutableStateOf("") }
    var selectedBudget by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var selectedFoodType by remember { mutableStateOf("") }
    var selectedQueueTolerance by remember { mutableStateOf("") }
    var selectedWeather by remember { mutableStateOf("") }

    var predictionResult by remember { mutableStateOf<String?>(null) }
    var showResultDialog by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Дерево решений")
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Home)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_24px),
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(AddTrainingData)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.add_24px),
                            contentDescription = null
                        )
                    }
                }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            QuestionSection(
                title = "Место:",
                options = listOf(
                    "main_building" to "Основное здание",
                    "second_building" to "Второй корпус",
                    "campus_centre" to "Первый корпус"
                ),
                selectedValue = selectedLocation,
                onValueChange = { selectedLocation = it }
            )

            QuestionSection(
                title = "Бюджет:",
                options = listOf(
                    "low" to "Низкий",
                    "medium" to "Средний",
                    "high" to "Высокий"
                ),
                selectedValue = selectedBudget,
                onValueChange = { selectedBudget = it }
            )

            QuestionSection(
                title = "Доступное время:",
                options = listOf(
                    "very_short" to "Очень малое",
                    "short" to "Малое",
                    "medium" to "Среднее"
                ),
                selectedValue = selectedTime,
                onValueChange = { selectedTime = it }
            )

            QuestionSection(
                title = "Тип еды:",
                options = listOf(
                    "snack" to "Перекус",
                    "coffee" to "Кофе",
                    "pancakes" to "Блины",
                    "full_meal" to "Полноценный обед"
                ),
                selectedValue = selectedFoodType,
                onValueChange = { selectedFoodType = it }
            )

            QuestionSection(
                title = "Терпимость к очередям:",
                options = listOf(
                    "low" to "Низкая",
                    "medium" to "Средняя"
                ),
                selectedValue = selectedQueueTolerance,
                onValueChange = { selectedQueueTolerance = it }
            )

            QuestionSection(
                title = "Погода:",
                options = listOf(
                    "good" to "Хорошая",
                    "bad" to "Плохая"
                ),
                selectedValue = selectedWeather,
                onValueChange = { selectedWeather = it }
            )

            Button(
                onClick = {
                    val selections = mapOf(
                        "location" to selectedLocation,
                        "budget" to selectedBudget,
                        "time_available" to selectedTime,
                        "food_type" to selectedFoodType,
                        "queue_tolerance" to selectedQueueTolerance,
                        "weather" to selectedWeather
                    )
                    val result = TreeDataManager.decisionTree.predict(selections)
                    showResultDialog = true
                    predictionResult = result

                    Log.d("TreeScreen", "Selections: $selections")
                    Log.d("TreeScreen", "Prediction: $result")
                    Log.d("TreeScreen", "showResultDialog: $showResultDialog")
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedLocation.isNotEmpty() &&
                        selectedBudget.isNotEmpty() &&
                        selectedTime.isNotEmpty() &&
                        selectedFoodType.isNotEmpty() &&
                        selectedQueueTolerance.isNotEmpty() &&
                        selectedWeather.isNotEmpty()
            ) {
                Text("Найти место")
            }
            if (showResultDialog && predictionResult != null) {
                AlertDialog(
                    onDismissRequest = { showResultDialog = false },
                    title = { Text("Рекомендация") },
                    text = {
                        Text(
                            when (predictionResult) {
                                "Main_Cafeteria" -> "Главная столовая"
                                "Yarche" -> "Ярче"
                                "Bus_Stop_Coffee" -> "Кофе на автобусной остановке"
                                "Starbooks" -> "Старбукс"
                                "Vending_Machine" -> "Вендинговый автомат"
                                "Second_Building_Cafe" -> "Кафе во втором корпусе"
                                "Siberian_Pancakes" -> "Сибирские блины"
                                else -> predictionResult ?: "Неизвестно"
                            }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { showResultDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun QuestionSection(
    title: String,
    options: List<Pair<String, String>>,
    selectedValue: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(title)
        options.forEach { (value, label) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = selectedValue == value,
                    onClick = { onValueChange(value) }
                )
                Text(label)
            }
        }
    }
}