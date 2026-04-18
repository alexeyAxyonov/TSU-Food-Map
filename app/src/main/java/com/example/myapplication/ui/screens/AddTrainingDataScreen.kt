package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.utils.TreeDataManager
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrainingDataScreen(
    navController: NavController
) {
    var selectedLocation by remember { mutableStateOf("") }
    var selectedBudget by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var selectedFoodType by remember { mutableStateOf("") }
    var selectedQueueTolerance by remember { mutableStateOf("") }
    var selectedWeather by remember { mutableStateOf("") }

    var recommendation by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Добавить обучающий пример") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_24px),
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
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

            HorizontalDivider()

            Text("Куда пошли?")

            OutlinedTextField(
                value = recommendation,
                onValueChange = { recommendation = it },
                label = {  },
                placeholder = { Text("Введите название") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val newInstance = mapOf(
                            "location" to selectedLocation,
                            "budget" to selectedBudget,
                            "time_available" to selectedTime,
                            "food_type" to selectedFoodType,
                            "queue_tolerance" to selectedQueueTolerance,
                            "weather" to selectedWeather,
                            "recommended_places" to recommendation
                        )

                        TreeDataManager.addTrainingInstance(newInstance)
                        navController.navigateUp()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = selectedLocation.isNotEmpty() &&
                            selectedBudget.isNotEmpty() &&
                            selectedTime.isNotEmpty() &&
                            selectedFoodType.isNotEmpty() &&
                            selectedQueueTolerance.isNotEmpty() &&
                            selectedWeather.isNotEmpty() &&
                            recommendation.isNotBlank()
                ) {
                    Text("Добавить")
                }

                OutlinedButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Отмена")
                }
            }
        }
    }
}
