package com.example.intelteamproject.compose

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.intelteamproject.Screen
import com.example.intelteamproject.data.User
import com.example.intelteamproject.database.FirebaseAuthenticationManager
import com.example.intelteamproject.database.FirestoreManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(navController: NavController) {
    // 구글 연동 이름, 이메일 가져오기
    val authManager = FirebaseAuthenticationManager()
    val firestoreManager = FirestoreManager()

//    Firebase.auth.addAuthStateListener {  }
    val currentUser = authManager.getCurrentUser()

    if (currentUser != null) {
        val uid = currentUser.uid
        val googleName = currentUser.displayName
        val googleEmail = currentUser.email
        val profileImageUrl = currentUser.photoUrl.toString()

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

                OutlinedTextField(value = name, onValueChange = { name = it },
                    label = { Text(text = "이름") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Face, contentDescription = null)
                    })
                OutlinedTextField(value = email, onValueChange = { email = it },
                    label = { Text(text = "이메일") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Email, contentDescription = null)
                    })
                OutlinedTextField(value = phone, onValueChange = { phone = it },
                    label = { Text(text = "전화번호") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Phone, contentDescription = null)
                    })
                OutlinedTextField(value = position, onValueChange = { position = it },
                    label = { Text(text = "직무") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
                    })

                val saveButtonEnabled =
                    name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && position.isNotBlank()
                Button(
                    onClick = {
                        val user = User(
                            uid = uid,
                            photoUrl = profileImageUrl,
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


