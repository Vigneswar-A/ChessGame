package com.game.chessgame.ui.theme


import androidx.compose.foundation.layout.*
import androidx.compose.material.Button

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun GameScreen(viewModel: BoardViewModel){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Board(viewModel)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {/*TODO*/}){
            Text(text="UNDO")
        }
    }
}