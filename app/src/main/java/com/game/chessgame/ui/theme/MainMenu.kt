package com.game.chessgame.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainMenu(viewModel: BoardViewModel, gameTrigger: () -> Unit = {}){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(108.dp))
        Text(
            text = "Chess Game",
            style = Typography.h3,
            color = buttonBackGround
        )
        Spacer(modifier = Modifier.height(108.dp))
        Button(
            onClick = {
                gameTrigger()
                viewModel.reset() },
            shape = RoundedCornerShape(size = 15.dp),
            modifier = Modifier
                .width(250.dp)
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = buttonBackGround),
        ) {
            Text(text = "Play")
        }
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier
                .height(54.dp)
                .width(250.dp)
                .padding(5.dp)
                .background(buttonBackGround, RoundedCornerShape(15.dp)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Time",
                modifier = Modifier.height(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Slider(
                value = viewModel.totalTime,
                modifier = Modifier
                    .width(100.dp)
                    .height(24.dp),
                valueRange = 1f..12f,
                onValueChange = { viewModel.totalTime = it },
                steps = 12
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "${5 * viewModel.totalTime.toInt()} minutes",
                modifier = Modifier.height(24.dp)
            )
        }
    }
}
