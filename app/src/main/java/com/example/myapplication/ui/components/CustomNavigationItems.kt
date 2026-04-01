package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.R
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class NavItem (
    val id: Uuid = Uuid.random(),
    val title: String,
    //Для А*.
    val coordsX: Int,
    val coordsY: Int,

    val type: String // Допустимые значения: shop, restaurant, attraction. Пиши в lowercase
)

/*
TODO: добавить все достопримечательности. Почитать про JSON
Константа с "точками интереса", не трогайте!!!
*/
@OptIn(ExperimentalUuidApi::class)
val PLACES = listOf(
    NavItem(
        title = "Абрикос",
        coordsX = 8,
        coordsY = 4,
        type = "shop"
    ),
    NavItem(
        title = "Ярче",
        coordsX = 154,
        coordsY = 5,
        type = "shop"
    ),
    NavItem(
        title = "Наш гастроном",
        coordsX = 155,
        coordsY = 46,
        type = "shop"
    ),
    NavItem(
        title = "Бристоль",
        coordsX = 148,
        coordsY = 66,
        type = "shop"
    ),
    NavItem(
        title = "Безумно",
        coordsX = 9,
        coordsY = 4,
        type = "restaurant"
    ),
    NavItem(
        title = "Батина Шаурма",
        coordsX = 10,
        coordsY = 13,
        type = "restaurant"
    ),
    NavItem(
        title = "Цзисян",
        coordsX = 3,
        coordsY = 60,
        type = "restaurant"
    ),
    NavItem(
        title = "Кафе во втором корпусе",
        coordsX = 56,
        coordsY = 64,
        type = "restaurant"
    ),
    NavItem(
        title = "Торговый автомат во втором корпусе",
        coordsX = 56,
        coordsY = 63,
        type = "restaurant"
    ),
    NavItem(
        title = "Сибирские блины",
        coordsX = 71,
        coordsY = 52,
        type = "restaurant"
    ),
    NavItem(
        title = "Столовая №1",
        coordsX = 72,
        coordsY = 52,
        type = "restaurant"
    ),
    NavItem(
        title = "Кафе Минутка",
        coordsX = 73,
        coordsY = 52,
        type = "restaurant"
    ),
    NavItem(
        title = "Кафе Научка",
        coordsX = 105,
        coordsY = 84,
        type = "restaurant"
    ),
    NavItem(
        title = "Rostic's",
        coordsX = 129,
        coordsY = 54,
        type = "restaurant"
    ),
    NavItem(
        title = "Вечный зов",
        coordsX = 145,
        coordsY = 12,
        type = "restaurant"
    ),
    NavItem(
        title = "Baba Roma",
        coordsX = 127,
        coordsY = 12,
        type = "restaurant"
    ),
    NavItem(
        title = "Гербарий",
        coordsX = 130,
        coordsY = 35,
        type = "restaurant"
    ),
    NavItem(
        title = "Сыр-Бор",
        coordsX = 66,
        coordsY = 20,
        type = "restaurant"
    )
)

@Composable
fun NavDrawerCustomItem(
    modifier: Modifier = Modifier,
    navItem: NavItem
){
    var expanded by remember { mutableStateOf(false) }
    // TODO: стиль ещё не выбранного элемента
    var selectedItem by remember { mutableStateOf("Добавить место") }
    NavigationDrawerItem(
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(selectedItem)
                Icon(
                    painter = if (expanded) painterResource(R.drawable.arrow_drop_up_24px)
                    else painterResource(R.drawable.arrow_drop_down_24px),
                    contentDescription = null
                )
            }
        },
        selected = TODO(),
        onClick = TODO(),
        modifier = TODO(),
        icon = TODO(),
        badge = TODO(),
        shape = TODO(),
        colors = TODO(),
        interactionSource = TODO(),
    )
}