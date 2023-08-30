package com.example.intelteamproject.compose

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.intelteamproject.R
import kotlinx.coroutines.delay

@Composable
fun FeedbackScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(bottom = 70.dp)
    ) {
        Top(title = "피드백 관리")
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Youtube(
                link = "https://www.youtube.com/watch?v=flTns9o8OWA",
                title = "남매 반드시 발생하는 상황 월드컵 (with 악뮤)",
                date = "Date : 2023-08-26",
                likes = 1100,
                dislikes = 200,
                comments = 641
            )
            Youtube(
                link = "https://www.youtube.com/watch?v=cC8EolQTzbE",
                title = "악동뮤지션과 악질뮤지션                                   ",
                date = "Date : 2023-08-25",
                likes = 1300,
                dislikes = 200,
                comments = 792
            )
            Youtube(
                link = "https://www.youtube.com/watch?v=KiXR2UNDSxE",
                title = "내 돈 주고 사긴 애매하지만 갖고 싶은 전자제품 월드컵",
                date = "Date : 2023-08-24",
                likes = 530,
                dislikes = 20,
                comments = 255
            )
            Youtube(
                link = "https://www.youtube.com/watch?v=nbZctrV7zXk",
                title = "문서작업용 노트북 쇼핑                                    ",
                date = "Date : 2023-08-23",
                likes = 850,
                dislikes = 40,
                comments = 489
            )
            Youtube(
                link = "https://www.youtube.com/watch?v=CCAHa6Yv6bI",
                title = "구글 애플 출신 한국인들이 전세계 유일무이한 번역 엔진을 만들기까지                                                       ",
                date = "Date : 2023-06-30",
                likes = 485,
                dislikes = 12,
                comments = 23
            )
            Youtube(
                link = "https://www.youtube.com/watch?v=7aFnrppCmrk",
                title = "지구의 운명이 외계인의 손에 달려있다면                        ",
                date = "Date : 2023-08-21",
                likes = 350,
                dislikes = 100,
                comments = 331
            )
        }
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .padding(start = 24.dp)
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
            .width(350.dp)
            .height(260.dp)
            .background(Color(0xFFF5F5F5))
            .padding(start = 20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
//            Spacer(modifier = Modifier.height(20.dp))
            Text(text = title, color = Color(0xFF404041))
            Text(text = date, color = Color(0xFFADADAE))
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                val reactions = listOf(
                    Reaction("likes", likes),
                    Reaction("dislikes", dislikes),
                    Reaction("comments", comments)
                )
                Column {
                    Spacer(modifier = Modifier.height(6.dp))

                    reactions.forEach { reaction ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            Text(
                                text = "${reaction.name}:",
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.width(100.dp),
                                color = Color(0xFFADADAE),

                                )
                            // 막대 그래프 표시
                            Box(
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(10.dp)
                                    .background(Color.LightGray)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(10.dp)
                                        .width((reaction.value / 6).dp) // 임의로 설정한 값
                                        .background(Color(0xFFC4302B))
                                )
                            }
//                            Text(text = reaction.value.toString())
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC4302B),
                    contentColor = Color.White
                ),
                onClick = {
                    Log.d("Button", "Button clicked") // 로그 출력
                    OpenLinkInBrowser(context, link)
                }) {
                Text(text = "Youtube")
            }
        }
    }
}


fun OpenLinkInBrowser(context: Context, link: String) {
    val intent = Intent(Intent.ACTION_VIEW, link.toUri())
    ContextCompat.startActivity(context, intent, null)
}

data class Reaction(val name: String, val value: Int)


@Composable
fun Top(title: String) {
    Spacer(
        modifier = Modifier
            .height(16.dp)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .padding(start = 16.dp)
            .background(Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(40.dp)
            ) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = title,
                    // Header 1
                    fontSize = 30.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF404041),
                )
            }
            var shouldAnimate by remember { mutableStateOf(true) }

            LaunchedEffect(shouldAnimate) {
                if (shouldAnimate) {
                    delay(300)
                    shouldAnimate = false
                }
            }

            AnimatedVisibility(
                visible = !shouldAnimate,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 1000, delayMillis = 300)
                ),
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Image(
                    painter = painterResource(id = R.drawable.wetube_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .size(width = 600.dp, height = 250.dp)
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
            .background(Color(0xFFF5F5F5))
//            .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),  // 변경된 부분
            horizontalArrangement = Arrangement.SpaceEvenly,
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
                    navController.navigate("signature")

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .width(70.dp)
                    .width(70.dp)
                    .height(70.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pen), contentDescription = null,
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
                    painter = painterResource(id = R.drawable.qrrr), contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
            }
            Button(
                onClick = {
                    navController.navigate("messenger")
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
                    painter = painterResource(id = R.drawable.send), contentDescription = null,
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
                    painter = painterResource(id = R.drawable.person), contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
            }
        }
    }
}