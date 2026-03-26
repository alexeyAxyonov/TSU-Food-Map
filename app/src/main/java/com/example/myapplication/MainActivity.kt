package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme


const val TILESIZE : Int = 20 //TODO: сделать в пикселях?

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column() {
                        MapView(
                            modifier = Modifier
                                .padding(innerPadding)
                                .size(1000.dp, 1000.dp))
                    }
                }
            }
        }
    }
}

//@Preview
@Composable
fun MapView(modifier: Modifier = Modifier,
            path : List<MapPoint> = remember { mutableStateListOf<MapPoint>().apply {
                    val result = findMyPath()
                    addAll(result)
                }
            }
) {
    Image(
        painter = painterResource(R.drawable.tsu_map),
        contentDescription = "Карта",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { offset ->
                    val row = (offset.y / TILESIZE).toInt()
                    val col = (offset.x / TILESIZE).toInt()
                    onTileClicked(row, col)
                })
            }
            //создание холста
            .drawWithContent {
                //для определения квадратиков для каждого размера телефона
                val dynamicTileWidth = size.width / 300 // Всего 300 колонок
                val dynamicTileHeight = size.height / 3600 // Всего 3600 строк
                //создание непрерывной линии - пути
                drawContent()
                val myPath = Path().apply {
                    val startX = path.first().row * dynamicTileWidth + dynamicTileWidth/2
                    val startY = path.first().col * dynamicTileHeight + dynamicTileHeight/2
                    moveTo(startX.toFloat(), startY.toFloat())
                    path.forEach{
                        val X = it.row * dynamicTileWidth + dynamicTileWidth/2 //Половинка ячейки для того,
                        val Y = it.col * dynamicTileHeight + dynamicTileHeight/2 // чтобы не выходить на другие ячейки - пути
                        lineTo(X.toFloat(), Y.toFloat()) // moveTo,lineTo - требуют float
                    }
                }
                drawPath(
                    path = myPath,
                    color = Color.Green,
                    style = Stroke(width = 8f)

                )
            }
    )
}
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