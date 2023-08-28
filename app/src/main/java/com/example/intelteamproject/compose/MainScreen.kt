package com.example.intelteamproject.compose

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.intelteamproject.Screen
import com.example.intelteamproject.database.FirebaseAuthenticationManager

@Composable
fun MainScreen(navController: NavController, onSignOutClicked: () -> Unit) {
    val authManager = FirebaseAuthenticationManager()
    val currentUser = authManager.getCurrentUser()
    val profileImageUrl = currentUser?.photoUrl.toString()

    val cardList = listOf(
        GridItemData(Screen.Board.route, Icons.Default.DateRange, "칸반 보드"),
        GridItemData(Screen.FeedBack.route, Icons.Default.Send, "피드백"),
        GridItemData(Screen.Messenger.route, Icons.Default.MailOutline, "메신저"),
        GridItemData(Screen.Message.route, Icons.Default.Email, "메시지"),
        GridItemData(Screen.Manage.route, Icons.Default.AccountBox, "근태 관리"),
        GridItemData(Screen.UserInfo.route, Icons.Default.Face, "사용자 정보 수정"),
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Box {
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { navController.navigate(Screen.UserInfo.route) }
                        .clip(shape = CircleShape)
                )
            }
            Column {
                Text(
                    text = "HOME",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
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
                                .size(150.dp)
                                .padding(2.dp)
                                .clickable { onSignOutClicked() },
                            shape = RectangleShape,
                            colors = CardDefaults.cardColors(containerColor = Color.White)
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
                                        imageVector = Icons.Default.ExitToApp,
                                        contentDescription = "SignOut",
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Text(text = "로그 아웃")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class GridItemData(val screen: String, val icon: ImageVector, val text: String)

@Composable
fun GridItem(data: GridItemData, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .size(150.dp)
            .padding(2.dp)
            .clickable { navController.navigate(data.screen) },
        shape = RectangleShape
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
                    modifier = Modifier.size(32.dp)
                )
                Text(text = data.text)
            }
        }
    }
}