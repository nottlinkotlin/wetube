package com.example.intelteamproject.compose

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController

@Composable
fun FeedbackScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(600.dp)
                .background(color = Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "반응 및 피드백 관리",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Youtube(
                    link = "https://www.youtube.com/watch?v=flTns9o8OWA",
                    title = "남매 반드시 발생하는 상황 월드컵 (with 악뮤)",
                    date = "날짜: 2023-08-26",
                    likes = 8200,
                    dislikes = 4000,
                    comments = 438
                )
                Youtube(
                    link = "https://www.youtube.com/watch?v=cC8EolQTzbE",
                    title = "악동뮤지션과 악질뮤지션",
                    date = "날짜: 2023-08-25",
                    likes = 12000,
                    dislikes = 2000,
                    comments = 736
                )
                Youtube(
                    link = "https://www.youtube.com/watch?v=KiXR2UNDSxE",
                    title = "내 돈 주고 사긴 애매하지만 갖고 싶은 전자제품 월드컵",
                    date = "날짜: 2023-08-24",
                    likes = 4900,
                    dislikes = 20,
                    comments = 242
                )
                Youtube(
                    link = "https://www.youtube.com/watch?v=nbZctrV7zXk",
                    title = "문서작업용 노트북 쇼핑",
                    date = "날짜: 2023-08-23",
                    likes = 7900,
                    dislikes = 420,
                    comments = 489
                )
                Youtube(
                    link = "https://www.youtube.com/watch?v=CCAHa6Yv6bI",
                    title = "다른 제목",
                    date = "날짜: 2023-08-22",
                    likes = 6900,
                    dislikes = 12,
                    comments = 412
                )
                Youtube(
                    link = "https://www.youtube.com/watch?v=7aFnrppCmrk",
                    title = "지구의 운명이 외계인의 손에 달려있다면",
                    date = "날짜: 2023-08-21",
                    likes = 3400,
                    dislikes = 120,
                    comments = 331
                )
            }
        }
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
            .wrapContentSize()
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Button(
                        onClick = {
                            Log.d("Button", "Button clicked") // 로그 출력
                            OpenLinkInBrowser(context, link)
                        }) {
                        Text(text = "유투브 영상으로 가기")
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
                        Reaction("좋아요", likes),
                        Reaction("싫어요", dislikes),
                        Reaction("댓글 수", comments)
                    )
                    Column {
                        reactions.forEach { reaction ->
                            Spacer(modifier = Modifier.height(8.dp))

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
                                        .height(20.dp)
                                        .background(Color.LightGray)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width((reaction.value / 2).dp) // 임의로 설정한 값
                                            .background(Color.Blue)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = reaction.value.toString())
                            }
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
