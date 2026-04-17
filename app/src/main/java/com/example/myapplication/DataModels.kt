package com.example.myapplication

import java.time.LocalTime

// ТИПЫ ПРОДУКТОВ
enum class ProductType {
    FOOD,       // Еда
    DRINK,      // Напиток
    UTENSIL     // Посуда / Приборы
}

// ПРОДУКТ
data class Product(
    val name: String,
    val type: ProductType
)

// ЗАВЕДЕНИЕ
data class Shop(
    val id: String,
    val name: String,
    val row: Int,
    val col: Int,
    val menu: List<Product>,
    val openTime: LocalTime,
    val closeTime: LocalTime,
    val isOpen24h: Boolean = false
)

fun Shop.isOpenNow(currentTime: LocalTime = LocalTime.now()): Boolean {
    if (isOpen24h) return true
    return currentTime.isAfter(openTime) && currentTime.isBefore(closeTime)
}

fun Shop.minutesUntilClose(currentTime: LocalTime = LocalTime.now()): Long {
    if (isOpen24h) return Long.MAX_VALUE
    return java.time.Duration.between(currentTime, closeTime).toMinutes()
}

// Список всех доступных продуктов (для отображения в UI)
val ALL_PRODUCTS = listOf(
    // Сибирские блины
    Product("Большой блин с маслом", ProductType.FOOD),
    Product("Блин с ветчиной и сыром", ProductType.FOOD),
    Product("Большой мясной блин", ProductType.FOOD),
    Product("Блин Цезарь", ProductType.FOOD),
    Product("Блин-бургер с курицей", ProductType.FOOD),
    Product("Блин шаурма", ProductType.FOOD),
    Product("Блин деревенский", ProductType.FOOD),
    Product("Блин по-венски", ProductType.FOOD),
    Product("Блин с шоколадом", ProductType.FOOD),
    Product("Чай чёрный", ProductType.DRINK),
    Product("Чай зелёный", ProductType.DRINK),
    Product("Капучино", ProductType.DRINK),
    Product("Латте", ProductType.DRINK),
    Product("Эспрессо", ProductType.DRINK),
    Product("Американо", ProductType.DRINK),
    Product("Кофе 3 в 1", ProductType.DRINK),
    Product("Морс облепиховый", ProductType.DRINK),
    Product("Морс смородина", ProductType.DRINK),
    Product("Кола", ProductType.DRINK),
    Product("Лимонад", ProductType.DRINK),
    Product("Горячий шоколад", ProductType.DRINK),
    Product("Холодный чай", ProductType.DRINK),
    Product("Фанта", ProductType.DRINK),
    Product("Спрайт", ProductType.DRINK),
    Product("Энергетик", ProductType.DRINK),

    // Батина Шаурма
    Product("Шаурма студенческая", ProductType.FOOD),
    Product("Шаурма грибная", ProductType.FOOD),
    Product("Шаурма сырная", ProductType.FOOD),
    Product("Шаурма кавказская", ProductType.FOOD),
    Product("Шаурма брусника", ProductType.FOOD),
    Product("Шаурма гранат", ProductType.FOOD),
    Product("Шаурма острая", ProductType.FOOD),
    Product("Шаурма пепперони", ProductType.FOOD),
    Product("Шаурма гавайская", ProductType.FOOD),
    Product("Молочный коктейль", ProductType.DRINK),

    // Кафе Научка
    Product("Каша овсяная", ProductType.FOOD),
    Product("Суп солянка", ProductType.FOOD),
    Product("Бульон куриный", ProductType.FOOD),
    Product("Шаурма веганская", ProductType.FOOD),
    Product("Шаурма классическая", ProductType.FOOD),
    Product("Шаурма с грибами", ProductType.FOOD),
    Product("Тортилья с творожным лососем", ProductType.FOOD),
    Product("Какао с маршмеллоу", ProductType.DRINK),
    Product("Мороженое", ProductType.FOOD),

    // Столовая №1
    Product("Чай с лимоном", ProductType.DRINK),
    Product("Компот", ProductType.DRINK),
    Product("Бисквит шоколадный", ProductType.FOOD),
    Product("Пирожное Баунти", ProductType.FOOD),
    Product("Корзинка с клубникой", ProductType.FOOD),
    Product("Бигус", ProductType.FOOD),
    Product("Котлета", ProductType.FOOD),
    Product("Тефтель", ProductType.FOOD),
    Product("Гуляш", ProductType.FOOD),
    Product("Фитнес салат", ProductType.FOOD),
    Product("Суп сырный", ProductType.FOOD),
    Product("Рис", ProductType.FOOD),
    Product("Гречка", ProductType.FOOD),
    Product("Пюре", ProductType.FOOD),
    Product("Макароны", ProductType.FOOD),

    // Rostic's
    Product("Чизбургер", ProductType.FOOD),
    Product("Шефбургер", ProductType.FOOD),
    Product("Картофель фри", ProductType.FOOD),
    Product("Наггетсы", ProductType.FOOD),
    Product("Куриная ножка", ProductType.FOOD),
    Product("Стрипсы", ProductType.FOOD),
    Product("Крылья", ProductType.FOOD),
    Product("Креветки", ProductType.FOOD),
    Product("Шефролл", ProductType.FOOD),
    Product("Байтсы", ProductType.FOOD),

    // Вечный зов
    Product("Аппетитный салат", ProductType.FOOD),
    Product("Гранатовый салат", ProductType.FOOD),
    Product("Окрошка", ProductType.FOOD),
    Product("Борщ", ProductType.FOOD),
    Product("Ассорти мясное", ProductType.FOOD),
    Product("Ассорти сырное", ProductType.FOOD),
    Product("Бефстроганов", ProductType.FOOD),
    Product("Бифштекс", ProductType.FOOD),
    Product("Вареники", ProductType.FOOD),
    Product("Чизкейк", ProductType.FOOD),
    Product("Тирамису", ProductType.FOOD),

    // Ярче
    Product("Самса", ProductType.FOOD),
    Product("Слойка с ветчиной и сыром", ProductType.FOOD),
    Product("Сосиска в тесте", ProductType.FOOD),
    Product("Эклеры", ProductType.FOOD),
    Product("Сырники", ProductType.FOOD),
    Product("Селёдка под шубой", ProductType.FOOD),
    Product("Салат зимний", ProductType.FOOD),
    Product("Вилка одноразовая", ProductType.UTENSIL),
    Product("Ложка одноразовая", ProductType.UTENSIL),
    Product("Салфетки", ProductType.UTENSIL),
    Product("Сахар", ProductType.FOOD),
    Product("Батончик", ProductType.FOOD),
    Product("Шоколадка", ProductType.FOOD),
    Product("Слойка с творогом", ProductType.FOOD),
    Product("Слойка с вареньем", ProductType.FOOD),

    // Бристоль
    Product("Вода 0.5л", ProductType.DRINK),
    Product("Чипсы Лэйс", ProductType.FOOD),
    Product("Жвачка", ProductType.FOOD),
    Product("Свэг", ProductType.FOOD),

    // Абрикос
    Product("Клаб-сэндвич с кофе", ProductType.FOOD),
    Product("Шаурма с кофе", ProductType.FOOD),
    Product("Хот-дог с кофе", ProductType.FOOD),
    Product("Суши", ProductType.FOOD),
    Product("Онигири", ProductType.FOOD),

    // Наш гастроном
    Product("Пельмени", ProductType.FOOD),
    Product("Смак бургер", ProductType.FOOD),
    Product("Колбаса", ProductType.FOOD),
    Product("Хлеб", ProductType.FOOD),
    Product("Сыр", ProductType.FOOD)
)

// Функция для создания заведений с меню
fun createTestShops(): List<Shop> {
    return listOf(
        // 1. Сибирские блины
        Shop(
            id = "bliny_1",
            name = "Сибирские блины",
            row = 71, col = 52,
            menu = listOf(
                Product("Большой блин с маслом", ProductType.FOOD),
                Product("Блин с ветчиной и сыром", ProductType.FOOD),
                Product("Большой мясной блин", ProductType.FOOD),
                Product("Блин Цезарь", ProductType.FOOD),
                Product("Блин-бургер с курицей", ProductType.FOOD),
                Product("Блин шаурма", ProductType.FOOD),
                Product("Блин деревенский", ProductType.FOOD),
                Product("Блин по-венски", ProductType.FOOD),
                Product("Блин с шоколадом", ProductType.FOOD),
                Product("Чай чёрный", ProductType.DRINK),
                Product("Чай зелёный", ProductType.DRINK),
                Product("Капучино", ProductType.DRINK),
                Product("Латте", ProductType.DRINK),
                Product("Эспрессо", ProductType.DRINK),
                Product("Американо", ProductType.DRINK),
                Product("Кофе 3 в 1", ProductType.DRINK),
                Product("Морс облепиховый", ProductType.DRINK),
                Product("Кола", ProductType.DRINK),
                Product("Лимонад", ProductType.DRINK),
                Product("Горячий шоколад", ProductType.DRINK),
                Product("Холодный чай", ProductType.DRINK)
            ),
            openTime = LocalTime.of(8, 30),
            closeTime = LocalTime.of(21, 0)
        ),

        // 2. Батина Шаурма
        Shop(
            id = "shaurma_1",
            name = "Батина Шаурма",
            row = 10, col = 13,
            menu = listOf(
                Product("Шаурма студенческая", ProductType.FOOD),
                Product("Шаурма грибная", ProductType.FOOD),
                Product("Шаурма сырная", ProductType.FOOD),
                Product("Шаурма кавказская", ProductType.FOOD),
                Product("Шаурма брусника", ProductType.FOOD),
                Product("Шаурма гранат", ProductType.FOOD),
                Product("Шаурма острая", ProductType.FOOD),
                Product("Шаурма пепперони", ProductType.FOOD),
                Product("Шаурма гавайская", ProductType.FOOD),
                Product("Молочный коктейль", ProductType.DRINK),
                Product("Капучино", ProductType.DRINK),
                Product("Латте", ProductType.DRINK),
                Product("Эспрессо", ProductType.DRINK),
                Product("Американо", ProductType.DRINK),
                Product("Лимонад", ProductType.DRINK),
                Product("Кола", ProductType.DRINK),
                Product("Фанта", ProductType.DRINK),
                Product("Спрайт", ProductType.DRINK),
                Product("Холодный чай", ProductType.DRINK),
                Product("Энергетик", ProductType.DRINK)
            ),
            openTime = LocalTime.of(10, 0),
            closeTime = LocalTime.of(22, 0)
        ),

        // 3. Кафе Научка
        Shop(
            id = "nauchka_1",
            name = "Кафе Научка",
            row = 105, col = 84,
            menu = listOf(
                Product("Каша овсяная", ProductType.FOOD),
                Product("Суп солянка", ProductType.FOOD),
                Product("Бульон куриный", ProductType.FOOD),
                Product("Шаурма веганская", ProductType.FOOD),
                Product("Шаурма классическая", ProductType.FOOD),
                Product("Шаурма с грибами", ProductType.FOOD),
                Product("Тортилья с творожным лососем", ProductType.FOOD),
                Product("Какао с маршмеллоу", ProductType.DRINK),
                Product("Мороженое", ProductType.FOOD),
                Product("Чай чёрный", ProductType.DRINK),
                Product("Чай зелёный", ProductType.DRINK),
                Product("Горячий шоколад", ProductType.DRINK),
                Product("Капучино", ProductType.DRINK),
                Product("Латте", ProductType.DRINK),
                Product("Эспрессо", ProductType.DRINK),
                Product("Американо", ProductType.DRINK)
            ),
            openTime = LocalTime.of(8, 0),
            closeTime = LocalTime.of(19, 0)
        ),

        // 4. Столовая №1
        Shop(
            id = "stolovaya_1",
            name = "Столовая №1",
            row = 72, col = 52,
            menu = listOf(
                Product("Чай с лимоном", ProductType.DRINK),
                Product("Чай чёрный", ProductType.DRINK),
                Product("Чай зелёный", ProductType.DRINK),
                Product("Кофе 3 в 1", ProductType.DRINK),
                Product("Горячий шоколад", ProductType.DRINK),
                Product("Компот", ProductType.DRINK),
                Product("Морс смородина", ProductType.DRINK),
                Product("Бисквит шоколадный", ProductType.FOOD),
                Product("Пирожное Баунти", ProductType.FOOD),
                Product("Корзинка с клубникой", ProductType.FOOD),
                Product("Бигус", ProductType.FOOD),
                Product("Котлета", ProductType.FOOD),
                Product("Тефтель", ProductType.FOOD),
                Product("Гуляш", ProductType.FOOD),
                Product("Фитнес салат", ProductType.FOOD),
                Product("Суп сырный", ProductType.FOOD),
                Product("Рис", ProductType.FOOD),
                Product("Гречка", ProductType.FOOD),
                Product("Пюре", ProductType.FOOD),
                Product("Макароны", ProductType.FOOD)
            ),
            openTime = LocalTime.of(9, 0),
            closeTime = LocalTime.of(17, 0)
        ),

        // 5. Rostic's
        Shop(
            id = "rostics_1",
            name = "Rostic's",
            row = 129, col = 54,
            menu = listOf(
                Product("Чизбургер", ProductType.FOOD),
                Product("Шефбургер", ProductType.FOOD),
                Product("Картофель фри", ProductType.FOOD),
                Product("Наггетсы", ProductType.FOOD),
                Product("Куриная ножка", ProductType.FOOD),
                Product("Стрипсы", ProductType.FOOD),
                Product("Крылья", ProductType.FOOD),
                Product("Креветки", ProductType.FOOD),
                Product("Шефролл", ProductType.FOOD),
                Product("Мороженое", ProductType.FOOD),
                Product("Лимонад", ProductType.DRINK),
                Product("Кола", ProductType.DRINK),
                Product("Байтсы", ProductType.FOOD),
                Product("Капучино", ProductType.DRINK),
                Product("Латте", ProductType.DRINK),
                Product("Эспрессо", ProductType.DRINK),
                Product("Американо", ProductType.DRINK)
            ),
            openTime = LocalTime.of(10, 0),
            closeTime = LocalTime.of(22, 0)
        ),

        // 6. Вечный зов
        Shop(
            id = "vechny_zov_1",
            name = "Вечный зов",
            row = 145, col = 12,
            menu = listOf(
                Product("Аппетитный салат", ProductType.FOOD),
                Product("Гранатовый салат", ProductType.FOOD),
                Product("Окрошка", ProductType.FOOD),
                Product("Борщ", ProductType.FOOD),
                Product("Ассорти мясное", ProductType.FOOD),
                Product("Ассорти сырное", ProductType.FOOD),
                Product("Бефстроганов", ProductType.FOOD),
                Product("Бифштекс", ProductType.FOOD),
                Product("Вареники", ProductType.FOOD),
                Product("Чизкейк", ProductType.FOOD),
                Product("Тирамису", ProductType.FOOD)
            ),
            openTime = LocalTime.of(11, 0),
            closeTime = LocalTime.of(23, 0)
        ),

        // 7. Ярче
        Shop(
            id = "yarche_1",
            name = "Ярче",
            row = 154, col = 5,
            menu = listOf(
                Product("Капучино", ProductType.DRINK),
                Product("Латте", ProductType.DRINK),
                Product("Эспрессо", ProductType.DRINK),
                Product("Американо", ProductType.DRINK),
                Product("Самса", ProductType.FOOD),
                Product("Слойка с ветчиной и сыром", ProductType.FOOD),
                Product("Сосиска в тесте", ProductType.FOOD),
                Product("Эклеры", ProductType.FOOD),
                Product("Сырники", ProductType.FOOD),
                Product("Селёдка под шубой", ProductType.FOOD),
                Product("Салат зимний", ProductType.FOOD),
                Product("Вилка одноразовая", ProductType.UTENSIL),
                Product("Ложка одноразовая", ProductType.UTENSIL),
                Product("Салфетки", ProductType.UTENSIL),
                Product("Сахар", ProductType.FOOD),
                Product("Батончик", ProductType.FOOD),
                Product("Шоколадка", ProductType.FOOD),
                Product("Слойка с творогом", ProductType.FOOD),
                Product("Слойка с вареньем", ProductType.FOOD),
                Product("Чай чёрный", ProductType.DRINK),
                Product("Чай зелёный", ProductType.DRINK)
            ),
            openTime = LocalTime.of(8, 0),
            closeTime = LocalTime.of(23, 0)
        ),

        // 8. Бристоль
        Shop(
            id = "bristol_1",
            name = "Бристоль",
            row = 148, col = 66,
            menu = listOf(
                Product("Вода 0.5л", ProductType.DRINK),
                Product("Чипсы Лэйс", ProductType.FOOD),
                Product("Жвачка", ProductType.FOOD),
                Product("Мороженое", ProductType.FOOD),
                Product("Лимонад", ProductType.DRINK),
                Product("Кола", ProductType.DRINK),
                Product("Фанта", ProductType.DRINK),
                Product("Свэг", ProductType.FOOD),
                Product("Батончик", ProductType.FOOD),
                Product("Шоколадка", ProductType.FOOD),
                Product("Холодный чай", ProductType.DRINK)
            ),
            openTime = LocalTime.of(8, 0),
            closeTime = LocalTime.of(22, 0)
        ),

        // 9. Абрикос
        Shop(
            id = "abrikos_1",
            name = "Абрикос",
            row = 8, col = 4,
            menu = listOf(
                Product("Клаб-сэндвич с кофе", ProductType.FOOD),
                Product("Шаурма с кофе", ProductType.FOOD),
                Product("Хот-дог с кофе", ProductType.FOOD),
                Product("Сосиска в тесте", ProductType.FOOD),
                Product("Суши", ProductType.FOOD),
                Product("Онигири", ProductType.FOOD),
                Product("Батончик", ProductType.FOOD),
                Product("Шоколадка", ProductType.FOOD),
                Product("Самса", ProductType.FOOD),
                Product("Слойка с ветчиной и сыром", ProductType.FOOD),
                Product("Капучино", ProductType.DRINK),
                Product("Латте", ProductType.DRINK),
                Product("Эспрессо", ProductType.DRINK),
                Product("Американо", ProductType.DRINK),
                Product("Чай чёрный", ProductType.DRINK),
                Product("Чай зелёный", ProductType.DRINK)
            ),
            openTime = LocalTime.of(9, 0),
            closeTime = LocalTime.of(21, 0)
        ),

        // 10. Наш гастроном
        Shop(
            id = "gastronom_1",
            name = "Наш гастроном",
            row = 155, col = 46,
            menu = listOf(
                Product("Пельмени", ProductType.FOOD),
                Product("Смак бургер", ProductType.FOOD),
                Product("Колбаса", ProductType.FOOD),
                Product("Хлеб", ProductType.FOOD),
                Product("Сыр", ProductType.FOOD),
                Product("Холодный чай", ProductType.DRINK),
                Product("Лимонад", ProductType.DRINK)
            ),
            openTime = LocalTime.of(8, 0),
            closeTime = LocalTime.of(22, 0)
        )
    )
}