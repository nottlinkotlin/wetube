package com.example.intelteamproject.compose

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.intelteamproject.R

@Composable
fun FeedbackScreen(navController: NavController) {
    Column(
        modifier = Modifier.background(color = Color(0xFF333333))
    ) {
        Top(title = "반응 및 피드백 관리")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Youtube(
                link = "https://www.youtube.com/watch?v=flTns9o8OWA",
                title = "남매 반드시 발생하는 상황 월드컵 (with 악뮤)",
                date = "날짜: 2023-08-26",
                likes = 820,
                dislikes = 4000,
                comments = 438
            )
            Youtube(
                link = "https://www.youtube.com/watch?v=cC8EolQTzbE",
                title = "악동뮤지션과 악질뮤지션",
                date = "날짜: 2023-08-25",
                likes = 1200,
                dislikes = 2000,
                comments = 736
            )
            Youtube(
                link = "https://www.youtube.com/watch?v=KiXR2UNDSxE",
                title = "내 돈 주고 사긴 애매하지만 갖고 싶은 전자제품 월드컵",
                date = "날짜: 2023-08-24",
                likes = 490,
                dislikes = 20,
                comments = 242
            )
            Youtube(
                link = "https://www.youtube.com/watch?v=nbZctrV7zXk",
                title = "문서작업용 노트북 쇼핑",
                date = "날짜: 2023-08-23",
                likes = 790,
                dislikes = 420,
                comments = 489
            )
            Youtube(
                link = "https://www.youtube.com/watch?v=CCAHa6Yv6bI",
                title = "다른 제목",
                date = "날짜: 2023-08-22",
                likes = 690,
                dislikes = 12,
                comments = 412
            )
            Youtube(
                link = "https://www.youtube.com/watch?v=7aFnrppCmrk",
                title = "지구의 운명이 외계인의 손에 달려있다면",
                date = "날짜: 2023-08-21",
                likes = 340,
                dislikes = 120,
                comments = 331
            )
        }
    }
    Column(
        verticalArrangement = Arrangement.Bottom
    ) {
        Bottom(navController = navController)
    }
}

@Composable
fun Youtube(
    link: String,
    title: String,
    date: String,
    likes: Int,
    dislikes: Int,
    comments: Int
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(330.dp, 300.dp)
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 20.dp))
//            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(size = 20.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF75462),
                        contentColor = Color.White
                    ),
                    onClick = {
                        Log.d("Button", "Button clicked") // 로그 출력
                        OpenLinkInBrowser(context, link)
                    }) {
                    Text(text = "Play")
                }
                Column {
                    Text(text = title)
                    Text(text = date)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val reactions = listOf(
                    Reaction("likes", likes),
                    Reaction("dislikes", dislikes),
                    Reaction("comments", comments)
                )
                Column {
                    reactions.forEach { reaction ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${reaction.name}:",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(100.dp)
                            )
                            // 막대 그래프 표시
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .background(Color.LightGray)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(10.dp)
                                        .width((reaction.value / 20).dp) // 임의로 설정한 값
                                        .background(Color.Black)
                                )
                            }
                            Spacer(modifier = Modifier.width(1.dp))
                            Text(text = reaction.value.toString())
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}


fun OpenLinkInBrowser(context: Context, link: String) {
    val intent = Intent(Intent.ACTION_VIEW, link.toUri())
    ContextCompat.startActivity(context, intent, null)
}

data class Reaction(val name: String, val value: Int)


@Composable
fun Top(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
//            .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(250.dp)
                    .height(32.dp)
            ) {
                Text(
                    text = title,
                    // Header 1
                    style = TextStyle(
                        fontSize = 26.sp,
                        lineHeight = 32.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF000000),
                    )
                )
            }
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null
                )
            }
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.dots),
                    contentDescription = null
                )
            }
        }
    }
}


@Composable
fun Bottom(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = Color.White)
//            .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Button(
                onClick = {
                    navController.navigate("main")

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home), contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
            }

            Button(
                onClick = {
                    navController.navigate("board")

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.heart), contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
            }
            Button(
                onClick = {
                    navController.navigate("manage")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pin), contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
            }
            Button(
                onClick = {
                    navController.navigate("logout")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.menu), contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
            }
            Button(
                onClick = {
                    navController.navigate("info")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user2), contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
            }
        }
    }
}