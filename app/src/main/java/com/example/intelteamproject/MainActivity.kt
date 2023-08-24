package com.example.intelteamproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.intelteamproject.compose.BoardScreen
import com.example.intelteamproject.compose.MainScreen
import com.example.intelteamproject.compose.ManageScreen
import com.example.intelteamproject.compose.MessengerScreen
import com.example.intelteamproject.ui.theme.IntelTeamProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntelTeamProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "MainScreen") {
                        composable(Screen.Main.route) { MainScreen(navController) }
                        composable(Screen.Board.route) { BoardScreen(navController) }
                        composable(Screen.Messenger.route) { MessengerScreen(navController) }
                        composable(Screen.Manage.route) { ManageScreen(navController) }
                    }

                }
            }
        }
    }
}