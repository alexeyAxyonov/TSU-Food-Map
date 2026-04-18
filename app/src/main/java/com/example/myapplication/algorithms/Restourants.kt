package com.example.myapplication.ui
import com.example.myapplication.algorithms.Cell

data class Restourant(
    val coordinates : Cell,
    val name : String) {

}

object RestourantRepository {
    val restourants = listOf(
        Restourant(Cell(20,20, true), "StarBucks"),
        Restourant(Cell(40,40,true), "Doner"),
        Restourant(Cell(30,37,true), "Doner2"),
        Restourant(Cell(60,30,true), "Doner3"),
        Restourant(Cell(60,45,true), "Doner4")

        //и так далее
    )
}