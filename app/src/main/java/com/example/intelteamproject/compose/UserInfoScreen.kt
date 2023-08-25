package com.example.intelteamproject.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.intelteamproject.Screen
import com.example.intelteamproject.data.User
import com.example.intelteamproject.database.FirebaseAuthenticationManager
import com.example.intelteamproject.database.FirestoreManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(navController: NavController) {
    // 구글 연동 이름, 이메일 가져오기
    val authManager = FirebaseAuthenticationManager()
    val firestoreManager = FirestoreManager()

    val currentUser = authManager.getCurrentUser()

    if (currentUser != null) {
        val uid = currentUser.uid
        val googleName = currentUser.displayName
        val googleEmail = currentUser.email

        var name by remember { mutableStateOf(googleName.toString()) }
        var email by remember { mutableStateOf(googleEmail.toString()) }
        var phone by remember { mutableStateOf("") }
        var position by remember { mutableStateOf("") }

        Column {
            OutlinedTextField(value = name, onValueChange = { name = it })
            OutlinedTextField(value = email, onValueChange = { email = it })
            OutlinedTextField(value = phone, onValueChange = { phone = it })
            OutlinedTextField(value = position, onValueChange = { position = it })
            
            Button(onClick = {
                val user = User(uid = uid, name = name, email = email, phone = phone, position = position)
                firestoreManager.saveUser(user)
                navController.popBackStack()
                navController.navigate(Screen.Main.route)
            }) {
                Text(text = "저장")
            }
        }
    }
}


