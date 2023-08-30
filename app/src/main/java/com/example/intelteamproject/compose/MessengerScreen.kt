package com.example.intelteamproject.compose

import android.content.ContentValues
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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import com.example.intelteamproject.R
import com.example.intelteamproject.Screen
import com.example.intelteamproject.data.Message
import com.example.intelteamproject.data.MessengerUser
import com.example.intelteamproject.database.FirebaseAuthenticationManager
import com.example.intelteamproject.ui.theme.IntelTeamProjectTheme
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

data class MyInfo(
    val name: String,
    val phone: String,
    val photoUrl: String,
    val position: String
)

@Composable
fun MessengerScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        var clickContact by remember { mutableStateOf(true) }
        var clickDm by remember { mutableStateOf(false) }
        val authManager = FirebaseAuthenticationManager()
        val currentUser = authManager.getCurrentUser()
        val currentUid = currentUser?.uid

        val db = Firebase.firestore
        val usersCollection = db.collection("users")
        val userList = remember { mutableStateListOf<MessengerUser>() }

//        LaunchedEffect(Unit) {
            usersCollection.get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        val userName = document.getString("name")
                        val userPhone = document.getString("phone")
                        val userPhotoUrl = document.getString("photoUrl")
                        val userPosition = document.getString("position")
                        val userUid = document.getString("uid")

                        if (userName != null) {
                            val messengerUser = MessengerUser(
                                name = userName,
                                phone = userPhone!!,
                                photoUrl = userPhotoUrl!!,
                                position = userPosition!!,
                                uid = userUid!!

                            )
                            if (!userList.contains(messengerUser)) {
                                userList.add(messengerUser)
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                }
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
                            tint = if (clickContact) Color.Red else Color.Black
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
                            tint = if (clickDm) Color.Red else Color.Black
                        )
                    }
                }
            }
            //상단 아이콘이 눌렸을 때 각각 해당하는 창을 띄움
            if (clickContact) {
                ContactView(currentUser!!, userList, navController)
            }
            if (clickDm) {
                MessengerView(currentUid, userList, navController)
            }
        }
    }
}

//연락처 창(처음 화면에 나올 창)
@Composable
fun ContactView(
    currentUser: FirebaseUser,
    userList: MutableList<MessengerUser>,
    navController: NavController
) {
    var clickUser by remember { mutableStateOf<MessengerUser?>(null) }
    val myCard = userList.filter { currentUser.uid == it.uid }
    if (myCard.isNotEmpty()) {
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
                    AsyncImage(
                        model = currentUser.photoUrl.toString(), contentDescription = null,
                        contentScale = ContentScale.Crop
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
                            text = myCard[0].name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
//                        Text(text = "현재 상태", fontSize = 15.sp)
                    }
                    Text(
                        text = myCard[0].position, fontSize = 18.sp, color = Color.Black
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(130.dp), verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = myCard[0].phone,
                        color = Color(0xff74787D),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .height(30.dp)
                            .width(135.dp)
                            .padding(end = 5.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                Spacer(modifier = Modifier.width(15.dp))
            }
        }
    }

//    userList.add(user.providerData)
    LazyColumn {
        items(userList) { user ->
            if (user.uid != currentUser.uid) {
                ContactCard(user) {
                    clickUser = it
                }
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
fun ContactCard(user: MessengerUser, onClick: (MessengerUser) -> Unit) {
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
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = "프로필 사진",
                    contentScale = ContentScale.Crop
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
//                    if (name != null) {
                    Text(
                        text = user.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
//                    }
//                        Text(text = "현재 상태", fontSize = 15.sp)
                }
                Text(text = user.position, fontSize = 15.sp, color = Color.Black)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp), verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = user.phone,
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
fun MessengerView(currentUid: String?, userList: MutableList<MessengerUser>, navController: NavController) {
    var displayedMessageList by remember { mutableStateOf(emptyList<Message>()) }
    val databaseReference = FirebaseDatabase.getInstance().getReference("messages")
//    var useruid = userList.
//    val roomNameList = remember { mutableStateListOf("", "") }
//    var roomName = ""
//    var userUid = ""
//    for (user in userList) {
//        userUid = user.uid
//        roomNameList[0] = currentUid!!
//        roomNameList[1] = userUid
//        roomNameList.sortDescending()
//        for (name in roomNameList) {
//            roomName += name
//        }
//        //메세지 저장할 공간
//        val messageRef =
//            remember { Firebase.database.getReference("messages").child("$roomName") }
//        val messageKeyName by remember { mutableStateOf<String?>(null) }
//        val allRoomsWithMessages = remember { mutableStateOf<List<Room>>(emptyList()) }

        //방 정보 불러오는 함수
//    var allRoomsFlow = flow {
//        val allRooms = getAllRoomAndMessages()
//        emit(allRooms)
//    }.collectAsState(initial = emptyList())
//    LaunchedEffect(Unit) {
//         val allRooms = getAllRoomAndMessages()
//        allRoomsFlow.value = allRooms
//    }
//    val messageListener = object : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            val room = snapshot.getValue<Message>()
//
//            displayedMessageList?.let {
//                if (!displayedMessageList.contains(room)) {
//                    displayedMessageList += room
//                }
//            }
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
//        }
//    }
//    databaseReference.addValueEventListener(messageListener)
    databaseReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
//            val allMessages = mutableListOf<Message>()

            for (roomSnapShot in snapshot.children) {
                val roomName = roomSnapShot.key

                for (messageSnapshot in roomSnapShot.children) {
                    val text = snapshot.child("text").getValue(String::class.java)
                    val sender = snapshot.child("sender").getValue(String::class.java)
                    val senderUid = snapshot.child("senderUid").getValue(String::class.java)
                    val receiverPhotoUrl =
                        snapshot.child("receiverPhotoUrl").getValue(String::class.java)
                    val receiverUid = snapshot.child("receiverUid").getValue(String::class.java)
                    val timestamp = snapshot.child("timestamp").getValue(Long::class.java)

                    if (text != null && sender != null && senderUid != null && receiverPhotoUrl != null && receiverUid != null && timestamp != null) {
                        val message =
                            Message(
                                text,
                                sender,
                                senderUid,
                                receiverPhotoUrl,
                                receiverUid,
                                timestamp,
                            )
                        if (!displayedMessageList.contains(message) && (currentUid == senderUid || currentUid == receiverUid)) {
                            displayedMessageList += message
                        }
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(ContentValues.TAG, "Failed to read value.", error.toException())

        }
    })

        //메세지 불러오는 함수
//        LaunchedEffect(Unit) {
//            messageRef.addChildEventListener(object : ChildEventListener {
//                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    val text = snapshot.child("text").getValue(String::class.java)
//                    val sender = snapshot.child("sender").getValue(String::class.java)
//                    val senderUid = snapshot.child("senderUid").getValue(String::class.java)
//                    val receiverPhotoUrl =
//                        snapshot.child("receiverPhotoUrl").getValue(String::class.java)
//                    val receiverUid = snapshot.child("receiverUid").getValue(String::class.java)
//                    val timestamp = snapshot.child("timestamp").getValue(Long::class.java)
//
//                    if (text != null && sender != null && senderUid != null && receiverPhotoUrl != null && receiverUid != null && timestamp != null) {
//                        val message =
//                            Message(
//                                text,
//                                sender,
//                                senderUid,
//                                receiverPhotoUrl,
//                                receiverUid,
//                                timestamp
//                            )
////                    val roomMessages = messagesMap.getOrPut(newMessageRef.key!!) { mutableListOf() }
//                        if (!displayedMessageList.contains(message)) {
//                            displayedMessageList += message
//                        }
//                    }
//                }
//
//                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
////            TODO("Not yet implemented")
//                }
//
//                override fun onChildRemoved(snapshot: DataSnapshot) {
////            TODO("Not yet implemented")
//                }
//
//                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
////            TODO("Not yet implemented")
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
//                }
//            })
//        }
//    }
//    val selectedRoom = allRoomsFlow.value.filter { room ->
//        room.message?.any{ message ->
//            message.senderUid == currentUid && message.receiverUid == currentUid
//        } == true
//    }
//    val selectedRoom = displayedMessageList.filter { message ->
//        message.senderUid == currentUid && message.receiverUid == currentUid
//    }
//    val allRoomsState by allRoomsFlow.collectAsState(initial = emptyList())
    LazyColumn {
        items(displayedMessageList) { message ->
            MessageList(message, navController)
        }

    }
}

//메세지 창에 띄울 메세지 목록의 틀
@Composable
fun MessageList(message: Message, navController: NavController) {
    Text(text = "$message", color = Color.Black)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            //메세지 목록 중 하나를 눌렀을 때 해당 목록의 메세지 창으로 전환
            .clickable { navController.navigate(Screen.Message.route) },
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
//                    for (message in room.message ?: emptyList()) {
                        Text(
                            text = message.sender,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(25.dp),
                            color = Color.Black
                        )
//                    }
//                        Text(text = "현재 상태", fontSize = 15.sp)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                        .fillMaxWidth()
                ) {
//                    val latestMessage = room.message?.lastOrNull()?.text
//                    latestMessage?.let {
                        Text(
                            text = "가장 최근 메세지",
                            fontSize = 15.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Black
                        )
//                    }
                }
            }
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}


//@Preview
@Composable
fun SendMessage(user: MessengerUser, navController: NavController, onDissmiss: () -> Unit) {
    val userUid = user.uid
    AlertDialog(
        onDismissRequest = { onDissmiss() },
        confirmButton = { /*TODO*/ },
        title = {
            Text(
                text = user.name,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
        },
        text = {
            Button(
                onClick = { navController.navigate("message/$userUid") },
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


//suspend fun getAllRoomAndMessages(): List<Room> {
//    val roomsCollection = Firebase.firestore.collection("rooms")
//    val roomList = mutableListOf<Room>()
//    val querySnapshot = roomsCollection.get().await()
//
//    for (document in querySnapshot) {
//        val room = document.toObject(Room::class.java)
//        val messagesCollection = roomsCollection.document(document.id)
//            .collection("messages")
//            .get()
//            .await()
//
//        val messageList = mutableListOf<Message>()
//        for (messageDoc in messagesCollection) {
//            val message = messageDoc.toObject(Message::class.java)
//            messageList.add(message)
//        }
//        roomList.add(room.copy(message = messageList))
//    }
//    return roomList
//}

