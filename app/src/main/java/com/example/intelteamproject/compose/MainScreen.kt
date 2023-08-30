package com.example.intelteamproject.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.intelteamproject.Screen
import com.example.intelteamproject.database.FirebaseAuthenticationManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Composable
fun MainScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(Color(0xFFF5F5F5))
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Top(title = "HOME")
            val authManager = FirebaseAuthenticationManager()
            val currentUser = authManager.getCurrentUser()
            val profileImageUrl = currentUser?.photoUrl.toString()

            val db = Firebase.firestore
            val uid = currentUser?.uid
            val docRef = uid?.let { db.collection("users").document(it) }
            var phone by remember { mutableStateOf("") }
            LaunchedEffect(Unit) {
                val documentSnapshot = docRef?.get()?.await()
                phone = documentSnapshot?.getString("phone") ?: ""
            }

            val cardList = listOf(
                GridItemData(Screen.Board.route, Icons.Default.List, "칸반 보드"),
                GridItemData(Screen.FeedBack.route, Icons.Default.ThumbUp, "피드백"),
                GridItemData(Screen.Manage.route, Icons.Default.Check, "출근 QR"),
                //            GridItemData(Screen.Manage.route, painterResource(id = R.drawable.qrrr), "qr"),
                GridItemData(Screen.Attendance.route, Icons.Default.AccountBox, "근태 관리"),
                GridItemData(Screen.Messenger.route, Icons.Default.Send, "메신저"),
                GridItemData(Screen.Signature.route, Icons.Default.Edit, "전자 서명"),
                GridItemData(Screen.Calendar.route, Icons.Default.DateRange, "캘린더")
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 24.dp, end = 24.dp)

            ) {
                Column {
                    //                Text(
                    //                    text = "HOME",
                    //                    fontSize = 24.sp,
                    //                    fontWeight = FontWeight.ExtraBold,
                    //                    modifier = Modifier
                    //                        .fillMaxWidth(),
                    //                    textAlign = TextAlign.Center
                    //                )
                    Spacer(modifier = Modifier.height(40.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2), // 그리드 열의 개수를 설정
                        modifier = Modifier.fillMaxSize() // 화면을 꽉 채우도록 설정
                    ) {
                        items(cardList.size) { index ->
                            GridItem(cardList[index], navController)
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .size(width = 150.dp, height = 110.dp)
                                    //                                .border(
                                    //                                    2.dp,
                                    //                                    color = Color(0xFFC4302B),
                                    //                                    shape = RoundedCornerShape(24.dp)
                                    //                                )
                                    .padding(4.dp)
                                    .background(color = Color(0xFFF5F5F5))
                                    .clickable { },
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(Color(0xFFC4302B)),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            modifier = Modifier,

                                            ) {
                                            AsyncImage(
                                                model = profileImageUrl,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .clickable { navController.navigate(Screen.UserInfo.route) }
                                                    .clip(shape = CircleShape)
                                            )
                                            if (phone.isBlank()) {
                                                Icon(
                                                    imageVector = Icons.Default.Notifications,
                                                    contentDescription = "edit",
                                                    tint = Color.White,
                                                    modifier = Modifier
                                                        .align(Alignment.TopEnd)
                                                        .zIndex(1f) // 이미지 위에 표시
                                                )
                                            }
                                        }
                                        Text(
                                            text = "My Profile",
                                            color = Color(0xFFEEEBD9),
                                            //                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.Bottom
    ) {
        Bottom(navController = navController)
    }

}

data class GridItemData(val screen: String, val icon: ImageVector, val text: String)

@Composable
fun GridItem(data: GridItemData, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .size(width = 150.dp, height = 110.dp)
            .padding(4.dp)
            .clickable { navController.navigate(data.screen) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(Color(0xFFC4302B)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = data.icon, contentDescription = "$data.icon",
                    tint = Color(0xFFEEEBD9),
                    modifier = Modifier.size(38.dp)
                )
                Text(
                    text = data.text,
                    color = Color(0xFFEEEBD9),
//                    fontSize = 12.sp
                )
            }
        }
    }
}
