package com.example.intelteamproject.compose

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.intelteamproject.R
import com.example.intelteamproject.Screen
import com.example.intelteamproject.database.FirestoreManager
import com.example.intelteamproject.ui.theme.IntelTeamProjectTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


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
        val firestoreManager = FirestoreManager()
        val auth = FirebaseAuth.getInstance()
        val currentUser= auth.currentUser
        val db = Firebase.firestore
        val userList = remember { mutableListOf<QueryDocumentSnapshot>() }
        db.collection("users").get()
            .addOnSuccessListener { result ->
                for (user in result) {
                    userList.add(user)
                }
            }
            .addOnFailureListener {
                Log.w(TAG, "Error getting documents.", it)
            }

//        if (user != null) {
//        val name = user!!.displayName
//        val email = user!!.email
//        val photoUrl = user!!.photoUrl
//        } else {
//            null
//        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //상단 바
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(color = Color.White, shape = RectangleShape),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
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
                    verticalAlignment = Alignment.CenterVertically
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
            //상단 아이콘이 눌렸을 때 각각 해당하는 창을 띄움
            if (clickContact) {
                ContactView(firestoreManager, currentUser!!, userList, navController)
            }
            if (clickDm) {
                MessengerView(navController)
            }
        }
    }
}

//연락처 창(처음 화면에 나올 창)
@Composable
fun ContactView(firestoreManager: FirestoreManager, currentUser: FirebaseUser, userList: MutableList<QueryDocumentSnapshot>, navController: NavController) {
//    val name = currentUser.displayName
    val myInfo = firestoreManager.getUser(currentUser.uid)
    var clickUser by remember { mutableStateOf<QueryDocumentSnapshot?>(null) }

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
            verticalAlignment = Alignment.CenterVertically,
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
                    Text(
                        text = myInfo?.name ?: "정보 없음",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
//                        Text(text = "현재 상태", fontSize = 15.sp)
                }
                Text(
                    text = "소속", fontSize = 18.sp, color = Color.Black
                )
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

//    userList.add(user.providerData)
    LazyColumn {
        items(userList) { user ->
            ContactCard(user) {
                clickUser = it
            }
        }
    }
    clickUser?.let { user ->
        SendMessage(user, navController) {
            clickUser = null
        }
    }
}

//연락처 창에 띄울 다른 사용자들의 목록의 틀
@Composable
fun ContactCard(user: QueryDocumentSnapshot, onClick: (QueryDocumentSnapshot) -> Unit) {
//    val name = user!!.displayName
//    val email = user!!.email
//    val photoUrl = user!!.photoUrl

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick(user) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(15.dp))
            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(20.dp),
            ) {
//                photoUrl?.let { imageUri ->
//                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                        decodeBitmap(
//                            ImageDecoder.createSource(
//                                LocalContext.current.contentResolver,
//                                photoUrl
//                            )
//                        )
//                    } else {
//                        null
//                    }
                Image(
//                        bitmap = bitmap!!.asImageBitmap(),
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "프로필 사진",
                    contentScale = ContentScale.Crop,
                )
//                }
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
//                    if (name != null) {
                    Text(
                        text = user.id,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
//                    }
//                        Text(text = "현재 상태", fontSize = 15.sp)
                }
                Text(text = "소속", fontSize = 15.sp, color = Color.Black)
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


//메세지 모여있는 창
@Composable
fun MessengerView(navController: NavController) {
    LazyColumn {
        items(10) { message ->
            MessageList(message = message, navController)
        }

    }
}

//메세지 창에 띄울 메세지 목록의 틀
@Composable
fun MessageList(message: Int, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            //메세지 목록 중 하나를 눌렀을 때 해당 목록의 메세지 창으로 전환
            .clickable { navController.navigate("message") },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RectangleShape,
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
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
            Spacer(modifier = Modifier.width(3.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(268.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "이름${message}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(25.dp),
                        color = Color.Black
                    )
//                        Text(text = "현재 상태", fontSize = 15.sp)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "가장 최근 메세지",
                        fontSize = 15.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}

//@Preview
@Composable
fun SendMessage(user: QueryDocumentSnapshot, navController: NavController, onDissmiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDissmiss() },
        confirmButton = { /*TODO*/ },
        title = {
            Text(
                text = user.id,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
        },
        text = {
            Button(
                onClick = { navController.navigate(Screen.Message.route) },
                colors = ButtonDefaults.buttonColors(Color.White),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "메세지 보내기",
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = Color.White
    )
}


