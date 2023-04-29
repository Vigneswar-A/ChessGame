package com.game.chessgame.ui.theme


import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.game.chessgame.R
import com.game.chessgame.data.PieceColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


var undoEnabled by mutableStateOf(true)
suspend fun timeOut(){
    delay(1000)
    undoEnabled = true
}

val scope = CoroutineScope(AndroidUiDispatcher.Main)

fun toTime(seconds: Int) : String{
    val (min, sec) = seconds/60 to seconds%60
    return "$min:%02d".format(sec)
}

@Composable
fun GameScreen(viewModel: BoardViewModel, mainMenu: () -> Unit = {}){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.rotate(180f),
            text = toTime(viewModel.blackTime),
            style = Typography.button,
            color = if (viewModel.blackTime < 60) Color.Red else Color.Black
        )
        Spacer(modifier = Modifier.height(24.dp))
        Board(viewModel)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = toTime(viewModel.whiteTime),
            style = Typography.button,
            color = if (viewModel.whiteTime < 60) Color.Red else Color.Black
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row() {
            Button(
                onClick = {
                    undoEnabled = false
                    scope.launch {
                        timeOut()
                    }
                    viewModel.undo()
                },
                enabled = viewModel.undoEnabled and undoEnabled,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = buttonBackGround,
                    disabledBackgroundColor = buttonDisabledBackGround
                )
            ) {
                Text(text = "UNDO")
            }
            Spacer(modifier = Modifier.width(44.dp))
            Button(
                onClick = {viewModel.reset()},
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = buttonBackGround,
                    disabledBackgroundColor = buttonDisabledBackGround
                )
            ) {
                Text(text = "RESET")
            }
        }
    }
}