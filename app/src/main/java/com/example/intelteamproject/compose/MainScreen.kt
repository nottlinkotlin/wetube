package com.example.intelteamproject.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.intelteamproject.Screen

@Composable
fun MainScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Button(onClick = { navController.navigate(Screen.Board.route) }) {
                Text(text = "칸반 보드")
            }
            Button(onClick = { navController.navigate(Screen.Messenger.route) }) {
                Text(text = "메신저")
            }
            Button(onClick = { navController.navigate(Screen.Manage.route) }) {
                Text(text = "근태 관리")
            }
            Button(onClick = { navController.navigate(Screen.Signing.route)}) {
                Text(text = "전자 서명")

            }
        }
    }
}