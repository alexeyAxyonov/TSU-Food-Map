package com.example.myapplication

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.algorithms.MapGrid
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import com.example.myapplication.ui.components.NavDrawerCustomItem
import com.example.myapplication.ui.components.DEFAULTDATANAVITEM
import com.example.myapplication.ui.components.NavItem
import com.example.myapplication.utils.addDrawerSlot
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import com.example.myapplication.ui.components.PlacesData
import com.example.myapplication.ui.screens.AddTrainingDataScreen
import com.example.myapplication.ui.screens.NeuralNetworkScreen
import com.example.myapplication.ui.screens.TreeScreen
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
object Home
@Serializable
object NeuralNetwork
@Serializable
object SolutionTreeScr
@Serializable
object AddTrainingData
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalUuidApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        PlacesData.load(this)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Home) {
                    composable<Home> {
                        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                        val scope = rememberCoroutineScope()
                        var navDataItems = remember {
                            mutableStateListOf(DEFAULTDATANAVITEM, DEFAULTDATANAVITEM)
                        }
                        var navItems = remember {
                            mutableStateListOf(
                                NavItem(id = Uuid.random(), data = DEFAULTDATANAVITEM),
                                NavItem(id = Uuid.random(), data = DEFAULTDATANAVITEM),
                            )
                        }
                        var imageWidth by remember { mutableFloatStateOf(0f) }
                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                ModalDrawerSheet {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    ) {
                                        Spacer(Modifier.height(12.dp))

                                        Text(
                                            "Навигация", modifier = Modifier.padding(12.dp),
                                            style = MaterialTheme.typography.titleMedium
                                        )

                                        HorizontalDivider()

                                        LazyColumn() {
                                            itemsIndexed(navItems) { index, item ->
                                                NavDrawerCustomItem(
                                                    selectedItem = item,
                                                    onItemSelected = { option ->
                                                        //Log.d("Navigation",
                                                        //    "До обновления: ${navItems[index].title}")
                                                        navItems[index] = NavItem(
                                                            id = Uuid.random(), data = option.data
                                                        )
                                                        //Log.d("Navigation",
                                                        //    "После обновления: ${navItems[index].title}")
                                                    },
                                                    onItemDeleted = {
                                                        //Log.d("Navigation",
                                                        //    "До удаления: ${navItems[index].title}")
                                                        navItems.removeAll { it.id == item.id }
                                                        //Log.d("Navigation",
                                                        //    "После удаления: ${navItems[index].title}")
                                                    }
                                                )
                                            }
                                            item {
                                                Button(
                                                    onClick = {
                                                        navItems.addDrawerSlot()
                                                    },
                                                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.add_24px),
                                                        contentDescription = "Иконка кнопки добавления точки",
                                                        modifier = Modifier.size(ButtonDefaults.IconSize)
                                                    )
                                                    Text(text = "Добавить место")
                                                }
                                            }
                                            item {
                                                Button(
                                                    onClick = {
                                                        TODO()
                                                    },
                                                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                                                ){
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.conversion_path_24px),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(ButtonDefaults.IconSize)
                                                    )
                                                    Text(text = "Показать путь")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        ) {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                topBar = {
                                    TopAppBar(
                                        title = {
                                            Text("Карта")
                                        },
                                        actions = {
                                            IconButton(onClick = {
                                                scope.launch {
                                                    if (drawerState.isClosed) {
                                                        drawerState.open()
                                                    } else {
                                                        drawerState.close()
                                                    }
                                                }
                                            }) {
                                                Icon(
                                                    painter =
                                                        painterResource(R.drawable.arrow_outward_24px),
                                                    contentDescription = "Иконка пути"
                                                )
                                            }
                                            IconButton(onClick = {
                                                navController.navigate(NeuralNetwork)
                                            }){
                                                Icon(
                                                    painter = painterResource(R.drawable.network_intelligence_24px),
                                                    contentDescription = null
                                                )
                                            }
                                            IconButton(onClick = {
                                                navController.navigate(SolutionTreeScr)
                                            }){
                                                Icon(
                                                    painter = painterResource(R.drawable.flowchart_24px),
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    )
                                }) { innerPadding ->
                                Column() {
                                    //modifier = Modifier
                                    MapView(
                                        modifier = Modifier
                                            .padding(innerPadding)
                                            .fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                    composable<NeuralNetwork>{
                        NeuralNetworkScreen(navController)
                    }
                    composable<SolutionTreeScr>{
                        TreeScreen(navController)
                    }
                    composable<AddTrainingData>{
                        AddTrainingDataScreen(navController)
                    }
                }
            }
        }
    }
}

//@Preview
@Composable
fun MapView(modifier: Modifier = Modifier) {
    //создание индивидуального painter, чтобы при добавлении нового фото не было ошибок при обращении к разным painter со своими именами
    val painterMap = painterResource(R.drawable.tsu_map)

    val mapWidth = painterMap.intrinsicSize.width //ширина оригинальной карты не визуальной части, которую видим
    val TILESIZE = mapWidth/300f //сторона квадратика у размера оригинальной карты

    val context = LocalContext.current //чтобы читать файл mapmatrix.txt
    val mapGrid = remember { MapGrid() }//чтобы потом вызывать findPath() для поиска пути
    var isMapLoaded by remember { mutableStateOf(false) }//Переменная, которая запоминает, загрузилась ли матрица

    LaunchedEffect(Unit) {//Загружаем матрицу из файла в фоне (не блокируя интерфейс)
        mapGrid.loadMatrixFromFile(context)
        isMapLoaded = true
    }

    var startPoint by remember { mutableStateOf<MapPoint?>(null) }// сохраняет значения между перерисовками экрана
    var targetPoint by remember { mutableStateOf<MapPoint?>(null) }

    //создание отслеживающего списка координат пути
    var path by remember {mutableStateOf<List<MapPoint>>(emptyList())}

    //var imageWidth by remember {mutableFloatStateOf(0f)}
    //val TILESIZE = imageWidth / 300f

    //создание отслеживающих переменных который будут показывать смещенение и зум и вращение
    var offsetX by remember {mutableStateOf(0f)}
    var offsetY by remember {mutableStateOf(0f)}
    var zoom by remember {mutableStateOf(1.2f)}
    var rotationAngle by remember {mutableStateOf(0f)}

    //создание отслеживающих переменных которые будут показывать координаты последнего нажатия на
    // видимой части карты (видимой на телефоне в данный момент), т.е координаты отн-но не начала карты
    // а начала которую может видеть пользователь(по простому левый верхний экран - точка начало 0px,0px)
    var lastTapX by remember {mutableStateOf(0f)}
    var lastTapY by remember {mutableStateOf(0f)}

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    Image(
        painter = painterMap,//painterResource(R.drawable.tsu_map),
        contentDescription = "Карта",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { offset ->
                    lastTapY = offset.y
                    lastTapX = offset.x

                    // Преобразуем координаты нажатия в координаты на оригинальной карте
                    val originalX = (offset.x - offsetX) / zoom
                    val originalY = (offset.y - offsetY) / zoom

                    // Переводим в номера клеток
                    val row = (originalX / TILESIZE).toInt()
                    val col = (originalY / TILESIZE).toInt()

                    // Проверяем, что координаты в пределах карты
                    if (row in 0 until mapGrid.rows && col in 0 until mapGrid.cols) {
                        // координаты корректны — обрабатываем
                        when {
                            startPoint == null -> {
                                startPoint = MapPoint(row, col)// первый клик — старт
                                Log.d("PathFinder", "Start point set: ($row, $col)")
                            }

                            targetPoint == null -> {
                                targetPoint = MapPoint(row, col)// второй клик — цель
                                Log.d("PathFinder", "Target point set: ($row, $col)")
                                // Вызываем алгоритм поиска пути
                                val result = mapGrid.findPath(
                                    startRow = startPoint!!.row,
                                    startCol = startPoint!!.col,
                                    targetRow = targetPoint!!.row,
                                    targetCol = targetPoint!!.col
                                )

                                if (result != null) {
                                    // Преобразуем List<Cell> в List<MapPoint>
                                    val newPath =
                                        result.map { cell -> MapPoint(cell.row, cell.col) }
                                    path = newPath
                                    Log.d("PathFinder", "Path found! Size: ${newPath.size}")
                                } else {
                                    Log.d("PathFinder", "Path not found!")
                                    path = emptyList()
                                }
                            }

                            else -> {
                                startPoint =
                                    MapPoint(row, col)// третий клик — очищаем и начинаем заново
                                targetPoint = null
                                Log.d("PathFinder", "Reset: start point set to ($row, $col)")
                            }
                        }
                    } else {
                        // нажали за пределами карты
                        Log.d("PathFinder", "Clicked outside map bounds: ($row, $col)")
                    }
                })
            }
            //создание модификатора, который будет отслеживать параметры скрола(сдвига по карте) и зума
            .graphicsLayer {
                translationX = offsetX
                translationY = offsetY
                scaleX = zoom
                scaleY = zoom
                rotationZ = rotationAngle

                //сделать позже: улучшение качество при приближении
                //renderEffect = RenderEffect.createBitmapFilterEffect(FilterQuality.High)
            }
            //создание холста
            .drawWithContent {
                drawContent()

                // Рисуем путь только если он не пустой
                if (path.isNotEmpty()) {
                    val myPath = Path().apply {
                        val startX = path.first().row * TILESIZE + TILESIZE / 2
                        val startY = path.first().col * TILESIZE + TILESIZE / 2
                        moveTo(startX.toFloat(), startY.toFloat())
                        path.forEach {
                            val X = it.row * TILESIZE + TILESIZE / 2
                            val Y = it.col * TILESIZE + TILESIZE / 2
                            lineTo(X.toFloat(), Y.toFloat())
                        }
                    }
                    drawPath(
                        path = myPath,
                        color = Color.Green,
                        style = Stroke(width = 8f)
                    )
                }
            }
    )
}
            /*
fun onTileClicked(row:Int, col:Int){
    Log.d("onTileSelectedMessage", "pressed on the " + row + "row and " + col + "column")
}
/*
@Composable
fun ClearStartAndEndPoint(modifier: Modifier = Modifier,) {
    Button (
        onClick = {

        }
        modifier = Modifier
    ) {
        Text (text = "Clear Start Point")
    }
}*/