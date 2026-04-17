package com.example.myapplication.ui.components

import android.content.Context
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
import com.example.myapplication.utils.addDrawerSlot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

//TODO: пофиксить баг с айдишниками. Сделать до 11.04.2026. Сделает Аксенов

data class DataNavItem (
    val title: String,
    //Для А*.
    val coordsX: Int,
    val coordsY: Int,

    val type: String // Допустимые значения: shop, restaurant, attraction. Пиши в lowercase
)

@OptIn(ExperimentalUuidApi::class)
data class NavItem (
    val id : Uuid,
    val data : DataNavItem,
)
/*
TODO: добавить все достопримечательности.
Константа с "точками интереса", не трогайте, я сам потрогаю!!!
*/

// Да, некрасиво. Да, костыль. Зато работает!
@OptIn(ExperimentalUuidApi::class)
val DEFAULTDATANAVITEM: DataNavItem = DataNavItem(
    title = "Выберите место",
    coordsX = 0,
    coordsY = 0,
    type = "default"
)

object PlacesData {
    lateinit var PLACES: List<NavItem>

    @OptIn(ExperimentalUuidApi::class)
    fun load(context: Context) {
        val gson = Gson()
        val allTemplates = mutableListOf<DataNavItem>()

        listOf(R.raw.restaurants, R.raw.shops, R.raw.attractions).forEach { resId ->
            val jsonString = context.resources.openRawResource(resId)
                .bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<DataNavItem>>() {}.type
            allTemplates.addAll(gson.fromJson(jsonString, type))
        }

        PLACES = allTemplates.map { template ->
            NavItem(
                id = Uuid.random(),
                data = template
            )
        }.toMutableList()
            //.apply {
           // addDrawerSlot() // Осторожно! Тут дефолтный слот!
        //}
    }
}

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
                Text(selectedItem.data.title)
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
                        PlacesData.PLACES.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.data.title) },
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