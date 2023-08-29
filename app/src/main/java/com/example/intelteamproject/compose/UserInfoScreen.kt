package com.example.intelteamproject.compose

import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.intelteamproject.Screen
import com.example.intelteamproject.data.User
import com.example.intelteamproject.database.FirebaseAuthenticationManager
import com.example.intelteamproject.database.FirestoreManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(navController: NavController, onSignOutClicked: () -> Unit) {
    // 구글 연동 이름, 이메일 가져오기
    val authManager = FirebaseAuthenticationManager()
    val firestoreManager = FirestoreManager()
    val currentUser = authManager.getCurrentUser()

    if (currentUser != null) {
        val uid = currentUser.uid
        val googleName = currentUser.displayName
        val googleEmail = currentUser.email
        val profileImageUrl = currentUser.photoUrl.toString()

        var name by remember { mutableStateOf(googleName.toString()) }
        var email by remember { mutableStateOf(googleEmail.toString()) }
        var phone by remember { mutableStateOf("") }
        var positionList: List<String> = listOf("PD", "기획팀", "편집팀", "출연팀", "촬영팀")
        var position by remember { mutableStateOf(positionList[0]) }
        var selectedPosition by remember { mutableStateOf(position) }

        val existingUser = firestoreManager.getUser(uid)
        if (existingUser != null) {
            name = existingUser.name
            email = existingUser.email
            phone = existingUser.phone
            position = existingUser.position
        }

        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "사용자 정보",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = name, onValueChange = { name = it },
                    label = { Text(text = "이름") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Face, contentDescription = null)
                    })
                OutlinedTextField(value = email, onValueChange = { email = it },
                    label = { Text(text = "이메일") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Email, contentDescription = null)
                    })
                OutlinedTextField(value = phone, onValueChange = { phone = it },
                    label = { Text(text = "전화번호") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Phone, contentDescription = null)
                    })

                Box(
                    modifier = Modifier.clickable {
                        expanded = true
                    }
                ) {
                    OutlinedTextField(
                        value = selectedPosition,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { Icons.Default.KeyboardArrowDown },
                        label = { Text(text = "직무") },
                        modifier = Modifier.clickable {
                            expanded = true
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        positionList.forEach { pos ->
                            DropdownMenuItem(
                                text = { Text(pos) },
                                onClick = {
                                    selectedPosition = pos
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
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
            Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                Button(onClick = { onSignOutClicked() }) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "SignOut",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 4.dp)
                    )
                    Text(text = "로그 아웃")
                }
            }
        }
    }
}