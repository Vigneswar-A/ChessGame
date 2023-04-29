package com.game.chessgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.game.chessgame.ui.theme.BoardViewModel
import com.game.chessgame.ui.theme.GameScreen
import com.game.chessgame.ui.theme.MainMenu

val model = BoardViewModel()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main-menu") {
                composable("main-menu") {
                    MainMenu(model) {
                        navController.navigate("game-screen")
                    }
                }
                composable("game-screen"){
                    GameScreen(model) {
                        navController.popBackStack("main-menu", true)
                        navController.navigate("main-menu")
                    }
                }
            }
        }
    }
}

