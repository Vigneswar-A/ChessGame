package com.game.chessgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.game.chessgame.ui.theme.BoardViewModel
import com.game.chessgame.ui.theme.GameScreen
import com.game.chessgame.ui.theme.MainMenu

val model = BoardViewModel()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameScreen(model)
        }
    }
}

