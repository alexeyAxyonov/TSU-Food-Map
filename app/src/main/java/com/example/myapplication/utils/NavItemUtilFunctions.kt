package com.example.myapplication.utils

import com.example.myapplication.ui.components.DEFAULTNAVITEM
import com.example.myapplication.ui.components.NavItem

fun MutableList<NavItem>.addDrawerSlot() {
    this.add(DEFAULTNAVITEM)
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
