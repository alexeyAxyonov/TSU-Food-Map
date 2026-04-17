package com.example.myapplication.utils

import com.example.myapplication.ui.components.DEFAULTDATANAVITEM
import com.example.myapplication.ui.components.NavItem
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun MutableList<NavItem>.addDrawerSlot() {
    this.add(NavItem(
        id = Uuid.random(),
        data = DEFAULTDATANAVITEM
    ))
}

fun MutableList<NavItem>.removeDrawerSlot(index: Int) {
    this.removeAt(index)
}
/*
fun MutableList<NavItem>.updateSelection(index: Int, newPlace: NavItem): MutableList<NavItem> {
    this[index] = newPlace старая имплементация, не работает
    val newList = this.toMutableList()
    newList[index] = newPlace
    return newList
}
*/
