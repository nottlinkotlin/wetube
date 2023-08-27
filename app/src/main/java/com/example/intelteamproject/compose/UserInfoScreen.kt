package com.example.intelteamproject.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.intelteamproject.Screen
import com.example.intelteamproject.data.User
import com.example.intelteamproject.database.FirebaseAuthenticationManager
import com.example.intelteamproject.database.FirestoreManager
import com.google.firebase.auth.FirebaseUser


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(navController: NavController) {
    Text(text = "dddddddddddddd")
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

        val existingUser = firestoreManager.getUser(uid)
        if (existingUser != null) {
            name = existingUser.name
            email = existingUser.email
            phone = existingUser.phone
            position = existingUser.position
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column {
                Text(text = "사용자 정보를 입력해 주세요")

                OutlinedTextField(value = name, onValueChange = { name = it })
                OutlinedTextField(value = email, onValueChange = { email = it })
                OutlinedTextField(value = phone, onValueChange = { phone = it })
                OutlinedTextField(value = position, onValueChange = { position = it })

                val saveButtonEnabled =
                    name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && position.isNotBlank()
                Button(
                    onClick = {
                        val user = User(
                            uid = uid,
                            name = name,
                            email = email,
                            phone = phone,
                            position = position
                        )
                        firestoreManager.saveUser(user)
                        navController.popBackStack()
                        navController.navigate(Screen.Main.route)
                    },
                    enabled = saveButtonEnabled
                ) {
                    Text(text = "저장")
                }
            }
        }
    }
}


