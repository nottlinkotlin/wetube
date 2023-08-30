package com.example.intelteamproject.compose

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.intelteamproject.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Composable
fun AttendanceScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .background(Color(0xFFF5F5F5))
            .padding(bottom = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Top(title = "근태 관리")

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
    Column(
        verticalArrangement = Arrangement.Bottom
    ) {
        Bottom(navController = navController)
    }
}

@Composable
fun ListItem(user: User) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        modifier = Modifier
            .height(100.dp)
            .width(380.dp)
            .padding(start = 15.dp, end = 15.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageRes = if (user.checked) {
                R.drawable.checked3
            } else {
                R.drawable.checked1
            }
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp)
            )
            Column (
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ){
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "이름: ${user.name}")
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "출근 유무: ${if (user.checked) "출석" else "미출석"}")
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "출근 시간: ${user.checkedTime}")

            }


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

