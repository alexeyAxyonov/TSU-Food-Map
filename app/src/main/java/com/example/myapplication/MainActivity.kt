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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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

@Preview
@Composable
fun MapView(modifier: Modifier = Modifier) {
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
    )
}

fun onTileClicked(row:Int, col:Int){
    Log.d("onTileSelectedMessage", "pressed on the " + row + "row and " + col + "column")
}