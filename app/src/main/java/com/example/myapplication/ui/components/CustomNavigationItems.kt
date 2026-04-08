package com.example.myapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

//TODO: пофиксить баг с айдишниками. Сделать до 11.04.2026. Сделает Аксенов

@OptIn(ExperimentalUuidApi::class)
data class NavItem (
    val id : Uuid,
    val title: String,
    //Для А*.
    val coordsX: Int,
    val coordsY: Int,

    val type: String // Допустимые значения: shop, restaurant, attraction. Пиши в lowercase
)

/*
TODO: добавить все достопримечательности. Почитать про JSON
Константа с "точками интереса", не трогайте, я сам потрогаю!!!
*/
@OptIn(ExperimentalUuidApi::class)
val PLACES = listOf(
    NavItem(
        id = Uuid.random(),
        title = "Абрикос",
        coordsX = 8,
        coordsY = 4,
        type = "shop"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Ярче",
        coordsX = 154,
        coordsY = 5,
        type = "shop"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Наш гастроном",
        coordsX = 155,
        coordsY = 46,
        type = "shop"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Бристоль",
        coordsX = 148,
        coordsY = 66,
        type = "shop"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Безумно",
        coordsX = 9,
        coordsY = 4,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Батина Шаурма",
        coordsX = 10,
        coordsY = 13,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Цзисян",
        coordsX = 3,
        coordsY = 60,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Кафе во втором корпусе",
        coordsX = 56,
        coordsY = 64,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Торговый автомат во втором корпусе",
        coordsX = 56,
        coordsY = 63,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Сибирские блины",
        coordsX = 71,
        coordsY = 52,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Столовая №1",
        coordsX = 72,
        coordsY = 52,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Кафе Минутка",
        coordsX = 73,
        coordsY = 52,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Кафе Научка",
        coordsX = 105,
        coordsY = 84,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Rostic's",
        coordsX = 129,
        coordsY = 54,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Вечный зов",
        coordsX = 145,
        coordsY = 12,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Baba Roma",
        coordsX = 127,
        coordsY = 12,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Гербарий",
        coordsX = 130,
        coordsY = 35,
        type = "restaurant"
    ),
    NavItem(
        id = Uuid.random(),
        title = "Сыр-Бор",
        coordsX = 66,
        coordsY = 20,
        type = "restaurant"
    )
)

// Да, некрасиво. Да, костыль. Зато работает!
@OptIn(ExperimentalUuidApi::class)
val DEFAULTNAVITEM: NavItem = NavItem(
    id = Uuid.random(),
    title = "Выберите место",
    coordsX = 66,
    coordsY = 20,
    type = "default"
)

@Composable
fun NavDrawerCustomItem(
    selectedItem: NavItem,
    onItemSelected: (NavItem) -> Unit,
    onItemDeleted: () -> Unit,
    modifier: Modifier = Modifier
){
    var expanded by remember { mutableStateOf(false) }
    //var selectedItem: NavItem by remember { mutableStateOf(DEFAULTNAVITEM) }
    NavigationDrawerItem(
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Icon(
                    painter = painterResource(R.drawable.delete_24px),
                    contentDescription = null,
                    modifier = Modifier.clickable{onItemDeleted()}
                )
                Text(selectedItem.title)
                Box {
                    Icon(
                        painter = if (expanded) painterResource(R.drawable.arrow_drop_up_24px)
                        else painterResource(R.drawable.arrow_drop_down_24px),
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.height(300.dp)
                    ) {
                        PLACES.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.title) },
                                onClick = {
                                    onItemSelected(option)
                                    expanded = false;
                                }
                            )
                        }
                    }
                }
            }
        },
        selected = false,
        onClick = { expanded = !expanded },
        shape = MaterialTheme.shapes.medium,
        colors = NavigationDrawerItemDefaults.colors(),
        interactionSource = remember { MutableInteractionSource() }
    )
}