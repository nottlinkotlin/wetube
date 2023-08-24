package com.example.intelteamproject.compose

import android.text.Layout.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.intelteamproject.R
import com.example.intelteamproject.ui.theme.IntelTeamProjectTheme


//@Preview(showBackground = true)
@Composable
fun MessengerScreenView() {
    IntelTeamProjectTheme {
        val navController = rememberNavController()
        MessengerScreen(navController)
    }
}


@Composable
fun MessengerScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        var clickContact by remember { mutableStateOf(true) }
        var clickDm by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(color = Color.White, shape = RectangleShape),
                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    colors = IconButtonDefaults.iconButtonColors(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "뒤로 가기",
                        tint = Color(0xFF1B1D1F)
                    )
                }
                Row(
                    modifier = Modifier
                        .width(350.dp)
                        .fillMaxHeight(1f),
                    horizontalArrangement = Arrangement.Start,
//                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            clickContact = true
                            clickDm = false
                        },
                        colors = IconButtonDefaults.iconButtonColors(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "연락처",
                            tint = if (clickContact) Color(0xff7FFFD4) else Color.Black
                        )
                    }
                    IconButton(
                        onClick = {
                            clickContact = false
                            clickDm = true
                        },
                        colors = IconButtonDefaults.iconButtonColors(Color.White)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.dm),
                            contentDescription = "메세지",
                            tint = if (clickDm) Color(0xff7FFFD4) else Color.Black
                        )
                    }
                }
            }
            if (clickContact) {
                ContactView()
            }
            if (clickDm) {
                MessageView()
            }
        }
    }
}

@Composable
fun ContactView() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RectangleShape,
    ) {
        Row(
            Modifier.fillMaxSize(),
//            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(15.dp))
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(20.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "프로필 사진",
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Row(

                ) {
                    Text(text = "이름", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//                        Text(text = "현재 상태", fontSize = 15.sp)
                }
                Text(text = "소속", fontSize = 18.sp)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(130.dp), verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "내선 번호",
                    color = Color(0xff74787D),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .height(30.dp)
                        .width(115.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
    LazyColumn {
        items(10) { contact ->
            UserInfo(contact = contact)
        }

    }
}

@Composable
fun UserInfo(contact: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RectangleShape,
    ) {
        Row(
            Modifier.fillMaxSize(),
//            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(15.dp))
            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(20.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "프로필 사진",
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Row(

                ) {
                    Text(text = "이름${contact}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                        Text(text = "현재 상태", fontSize = 15.sp)
                }
                Text(text = "소속", fontSize = 15.sp)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp), verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "내선 번호",
                    color = Color(0xff74787D),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .height(30.dp)
                        .width(135.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}

@Composable
fun MessageView() {
    LazyColumn {
        items(10) { message ->
            MessageList(message = message)
        }

    }
}

@Composable
fun MessageList(message: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RectangleShape,
    ) {
        Row(
            Modifier.fillMaxSize(),
//            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(15.dp))
            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(20.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "프로필 사진",
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(268.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Row(

                ) {
                    Text(text = "이름${message}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                        Text(text = "현재 상태", fontSize = 15.sp)
                }
                Text(text = "가장 최근 메세지", fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}