package com.example.intelteamproject.compose

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.intelteamproject.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Composable
fun AttendanceScreen(navController: NavController) {

    //name, checked, checked Time을 모두 불러 와서 보여 주는 화면 만들기

    val context = LocalContext.current

    val db = Firebase.firestore
    val usersCollectionRef = db.collection("users")

    var usersData by remember { mutableStateOf(listOf<User>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val snapshot = usersCollectionRef.get().await()
            val users = snapshot.documents.mapNotNull { document ->
                document.toObject(User::class.java)
            }
            usersData = users
            isLoading = false
        } catch (e: Exception) {
            Toast.makeText(context, "데이터 로드 중 오류 발생", Toast.LENGTH_SHORT).show()
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.wetube_large),
            contentDescription = null,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .size(width = 180.dp, height = 120.dp)
        )

        if (isLoading) {
            Text("로딩 중...")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                items(usersData) { user ->

                    ListItem(user)
                }
            }
        }

    }

}

@Composable
fun ListItem(user: User) {
    val painter = rememberImagePainter(
        data = user.photoUrl,
        builder = {
            crossfade(true)
        }
    )

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        modifier = Modifier
            .height(100.dp)
            .width(380.dp)

    ) {

        Row( modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .padding(start = 4.dp, top = 4.dp)
                    .clip(CircleShape)
            )

            Column {
                Text(text = "이름: ${user.name}",
                    modifier=Modifier
                        .padding(top=16.dp))
                Text(text = "출근 유무: ${if (user.checked) "출근" else "미출근"}")
                Text(text = "출근 시간: ${user.checkedTime}")

            }

            val imageRes = if (user.checked) {
                R.drawable.checked5
            } else {
                R.drawable.checked4
            }
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(start = 4.dp, end = 4.dp, top = 20.dp, bottom = 4.dp)
            )


        }

    }

    Spacer(modifier = Modifier.padding(12.dp))
}

data class User(
    val photoUrl: String="",
    val name: String = "",
    val checked: Boolean = false,
    val checkedTime: String = ""
)

