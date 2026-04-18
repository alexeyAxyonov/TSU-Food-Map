package com.example.myapplication

import AcoRouteSolver
import RouteResult
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.geometry.Offset
import com.example.myapplication.algorithms.Cell
import com.example.myapplication.algorithms.ClusteringResult
import com.example.myapplication.algorithms.findClusters
import com.example.myapplication.algorithms.metric
import com.example.myapplication.algorithms.numberOfCluster
import com.example.myapplication.ui.RestourantRepository
import com.example.myapplication.ui.components.NavDrawerCustomItem
import com.example.myapplication.ui.components.DEFAULTDATANAVITEM
import com.example.myapplication.ui.components.NavItem
import com.example.myapplication.utils.addDrawerSlot
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import com.example.myapplication.ui.components.PlacesData
import com.example.myapplication.ui.screens.NeuralNetworkScreen
import kotlinx.serialization.Serializable
import kotlin.math.sqrt
import kotlin.text.get
import kotlin.text.toFloat
import kotlin.times
import kotlin.uuid.Uuid

@Serializable
object Home
@Serializable
object NeuralNetwork
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalUuidApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        PlacesData.load(this)
        setContent {
            MyApplicationTheme {
                var clustersRestourants by remember { mutableStateOf<ClusteringResult?>(null)}
                var showClusters by remember { mutableStateOf(false) }
                val restaurants = RestourantRepository.restourants
                val numberOfCluster = 2

                var antPath by remember { mutableStateOf<RouteResult?>(null) }
                var pathAntResult by remember { mutableStateOf<RouteResult?>(null) }
                var showPath by remember { mutableStateOf(false) }
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
                                            ShowClustersButton(
                                                modifier = Modifier,
                                                showClusters = showClusters,
                                                onToggle = {
                                                    showClusters = !showClusters
                                                    if (showClusters) {
                                                        val cells = restaurants.map { it.coordinates }
                                                        clustersRestourants = findClusters(numberOfCluster, cells)
                                                    }
                                                }
                                            )
                                            ShowPathThroughPlacesButton (
                                                modifier = Modifier,
                                                showPath = showPath,
                                                onToggle = {
                                                    showPath = !showPath

                                                    if (showPath) {
                                                        val selected = restaurants.filter { it.coordinates.road }

                                                        val solver = AcoRouteSolver(selected)
                                                        pathAntResult = solver.solve()
                                                    }
                                                }
                                            )
                                        }
                                    )
                                }) { innerPadding ->
                                Column() {
                                    //modifier = Modifier
                                    MapView(
                                        modifier = Modifier
                                            .padding(innerPadding)
                                            .fillMaxSize(),
                                        clustersRestourants = if (showClusters) clustersRestourants else null,
                                        pathAnt = if (showPath) pathAntResult?.toCells() else null
                                    )
                                }
                            }
                        }
                    }
                    composable<NeuralNetwork>{
                        NeuralNetworkScreen(navController)
                    }
                }
            }
        }
    }
}

//@Preview
@Composable
fun MapView(modifier: Modifier = Modifier, clustersRestourants : ClusteringResult? = null,
            pathAnt: List<Cell>? = null ) {
    //создание индивидуального painter, чтобы при добавлении нового фото не было ошибок при обращении к разным painter со своими именами
    val painterMap = painterResource(R.drawable.tsu_map)

    val mapHeight = painterMap.intrinsicSize.height
    val mapWidth = painterMap.intrinsicSize.width //ширина оригинальной карты не визуальной части, которую видим
    //val TILESIZE = mapWidth/160f //сторона квадратика у размера оригинальной карты

    //для муравьиного алгоритма
    var pathResult by remember { mutableStateOf<RouteResult?>(null) }
    var showPath by remember { mutableStateOf(false) }

    val context = LocalContext.current //чтобы читать файл mapmatrix.txt
    val mapGrid = remember { MapGrid() }//чтобы потом вызывать findPath() для поиска пути
    var isMapLoaded by remember { mutableStateOf(false) }//Переменная, которая запоминает, загрузилась ли матрица

    LaunchedEffect(Unit) {//Загружаем матрицу из файла в фоне (не блокируя интерфейс)
        mapGrid.loadMatrixFromFile(context)
        isMapLoaded = true
    }
    var TILESIZEWIDTH by remember { mutableFloatStateOf(0f) }
    var TILESIZEHEIGHT by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(mapGrid.cols) {
        if (mapGrid.cols > 0) {
            TILESIZEWIDTH = mapWidth.toFloat() / mapGrid.cols.toFloat()
            TILESIZEHEIGHT = mapHeight.toFloat() / mapGrid.rows.toFloat()
        }
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
    var Xchange = rememberScrollState()
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
                    val originalX = (offset.x - offsetX) / zoom //+ 80f + 180f
                    val originalY = (offset.y - offsetY) / zoom //+ 200f

                    // Переводим в номера клеток
                    val col = (originalX / TILESIZEWIDTH).toInt()
                    val row = (originalY / TILESIZEHEIGHT).toInt()

                    Log.d("MapView", "mapWidth = $col, mapHeight = $row")
                    Log.d("MapView", "TILESIZEWIDTH = $TILESIZEWIDTH, TILESIZEHEIGHT = $TILESIZEHEIGHT")
                    Log.d("MapView", "path points: ${path.map { "(${it.row},${it.col})" }}")
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
                //рисуем обход по всем достопримечательностям
                pathAnt?.let { pathAnt ->
                    if (pathAnt.isNotEmpty()) {

                        val myPathAnt = Path().apply {
                            val startX = pathAnt.first().col * TILESIZEWIDTH + TILESIZEWIDTH / 2
                            val startY = pathAnt.first().row * TILESIZEHEIGHT + TILESIZEHEIGHT / 2

                            moveTo(startX, startY)

                            pathAnt.forEach { r ->
                                val x = r.col * TILESIZEWIDTH + TILESIZEWIDTH / 2
                                val y = r.row * TILESIZEHEIGHT + TILESIZEHEIGHT / 2

                                lineTo(x, y) //
                            }
                        }

                        drawPath(
                            path = myPathAnt,
                            color = Color.Green,
                            style = Stroke(width = 6f),
                            alpha = 0.35f
                        )

                        // точки поверх
                        pathAnt.forEachIndexed { index, r ->
                            val x = r.col * TILESIZEWIDTH + TILESIZEWIDTH / 2
                            val y = r.row * TILESIZEHEIGHT + TILESIZEHEIGHT / 2

                            drawCircle(
                                color = if (index == 0) Color.Yellow else Color.Blue,
                                radius = TILESIZEWIDTH / 3f,
                                center = Offset(x, y)
                            )
                        }
                    }
                }
                // рисуем кластеры
                val colors =
                    listOf(Color.Blue, Color.Cyan, Color.Yellow, Color.White)
                // Рисуем кластеры
                clustersRestourants?.let { allowedClusters ->
                    allowedClusters.clusters.forEachIndexed { index, cluster ->
                        val color = colors[index]
                        cluster.forEach { cell ->
                            val x = cell.row * TILESIZEWIDTH + TILESIZEWIDTH / 2
                            val y = cell.col * TILESIZEWIDTH + TILESIZEWIDTH / 2

                            drawCircle(
                                color = color.copy(alpha = 0.7f),
                                radius = TILESIZEWIDTH / 2f,
                                center = Offset(x.toFloat(), y.toFloat())
                            )
                        }
                        val radiusCentroid = cluster.maxOf { cell ->
                            metric(cell, allowedClusters.centroids[index]).toFloat()
                        }
                        // Отрисовка всей области одного кластера
                        val centroidCoordX =
                            allowedClusters.centroids[index].row.toFloat() * TILESIZEWIDTH
                        val centroidCoordY =
                            allowedClusters.centroids[index].col.toFloat() * TILESIZEWIDTH
                        drawCircle(
                            color = color.copy(alpha = 0.09f),
                            radius = sqrt(radiusCentroid) * TILESIZEWIDTH,
                            center = Offset(centroidCoordX, centroidCoordY),
                            // style = Stroke(width = 4f)
                        )
                    }
                }
                // Рисуем путь только если он не пустой
                if (path.isNotEmpty()) {
                    val myPath = Path().apply {
                        val startX = path.first().col * TILESIZEWIDTH + TILESIZEWIDTH / 2
                        val startY = path.first().row * TILESIZEWIDTH + TILESIZEWIDTH / 2
                        moveTo(startX.toFloat()  +300f, startY.toFloat()+100f)
                        path.forEach {
                            val X = it.col * TILESIZEWIDTH + TILESIZEWIDTH / 2
                            val Y = it.row * TILESIZEHEIGHT + TILESIZEHEIGHT / 2
                            lineTo(X.toFloat() + 220f , Y.toFloat() +100f)
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


@Composable
fun ShowClustersButton(modifier : Modifier = Modifier, showClusters: Boolean, onToggle: () -> Unit) {

    Button(
        modifier = modifier
            .height(50.dp)
            .width(60.dp),
        onClick = onToggle) {
        Text(if (showClusters) "Скрыть кластеры" else "Показать кластеры")
    }
}
@Composable
fun ShowPathThroughPlacesButton(
        modifier: Modifier = Modifier,
        showPath: Boolean,
        onToggle: () -> Unit)
{
        Button(
            modifier = modifier
                .height(50.dp)
                .width(80.dp),
            onClick = onToggle
        ) {
            Text(if (showPath) "Скрыть путь" else "Показать путь")
        }
}
fun RouteResult.toCells(): List<Cell> {
    return path.map { it.coordinates }
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