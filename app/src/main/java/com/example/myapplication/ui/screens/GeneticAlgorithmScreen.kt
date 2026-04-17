package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.Product
import com.example.myapplication.TSPViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneticAlgorithmScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: TSPViewModel = viewModel()

    val allProducts by viewModel.allProducts.collectAsState()
    val selectedProducts by viewModel.selectedProducts.collectAsState()
    val bestRoute by viewModel.bestRoute.collectAsState()
    //val totalTime by viewModel.totalTime.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentGeneration by viewModel.currentGeneration.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Показываем Toast при ошибке
    if (errorMessage != null) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        viewModel.clearError()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Выбор продуктов") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // СПИСОК ПРОДУКТОВ
            Text(
                text = "Что вы хотите купить?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Выбрано: ${selectedProducts.size}",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(allProducts) { product ->
                    ProductItem(
                        product = product,
                        isSelected = selectedProducts.contains(product),
                        onToggle = { viewModel.toggleProductSelection(product) }
                    )
                }
            }

            HorizontalDivider()

            //КНОПКИ И РЕЗУЛЬТАТ
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Прогресс алгоритма
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Поколение: $currentGeneration / 100")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Кнопка запуска
                Button(
                    onClick = { viewModel.startAlgorithm(context) },
                    enabled = !isLoading && selectedProducts.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isLoading) "Идёт поиск..." else "Найти маршрут")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Кнопка очистки
                Button(
                    onClick = { viewModel.clearSelection() },
                    enabled = !isLoading && selectedProducts.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Очистить выбор")
                }

                // РЕЗУЛЬТАТ
                if (bestRoute.isNotEmpty() && !isLoading) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Найденный маршрут",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            /*Text(
                                text = "Общее время: ${"%.1f".format(totalTime)} мин",
                                fontSize = 14.sp,
                                color = Color(0xFF006400)
                            )*/

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Порядок посещения:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )

                            bestRoute.forEachIndexed { index, shop ->
                                Text(
                                    text = "${index + 1}. ${shop.name}",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    fontSize = 16.sp
                )
                Text(
                    text = when (product.type) {
                        com.example.myapplication.ProductType.FOOD -> "🍔 Еда"
                        com.example.myapplication.ProductType.DRINK -> "🥤 Напиток"
                        com.example.myapplication.ProductType.UTENSIL -> "🍴 Посуда"
                    },
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}