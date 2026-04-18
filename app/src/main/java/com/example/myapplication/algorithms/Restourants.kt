package com.example.myapplication.ui
import com.example.myapplication.algorithms.Cell

class Restourant(
    val coordinates : Cell,
    val name : String) {

}
object RestourantRepository {
    val restourants = listOf(
        Restourant(Cell(8, 4, true), "Абрикос"),
        Restourant(Cell(154, 5, true), "Ярче"),
        Restourant(Cell(155, 46, true), "Наш гастроном"),
        Restourant(Cell(148, 66, true), "Бристоль"),
        Restourant(Cell(9, 4, true), "Безумно"),
        Restourant(Cell(10, 13, true), "Батина Шаурма"),
        Restourant(Cell(3, 60, true), "Цзисян"),
        Restourant(Cell(56, 64, true), "Кафе во втором корпусе"),
        Restourant(Cell(56, 63, true), "Торговый автомат во втором корпусе"),
        Restourant(Cell(71, 52, true), "Сибирские блины"),
        Restourant(Cell(72, 52, true), "Столовая №1"),
        Restourant(Cell(73, 52, true), "Кафе Минутка"),
        Restourant(Cell(105, 84, true), "Кафе Научка"),
        Restourant(Cell(129, 54, true), "Rostic's"),
        Restourant(Cell(145, 12, true), "Вечный зов"),
        Restourant(Cell(127, 12, true), "Baba Roma"),
        Restourant(Cell(130, 35, true), "Гербарий"),
        Restourant(Cell(66, 20, true), "Сыр-Бор")
    )
}