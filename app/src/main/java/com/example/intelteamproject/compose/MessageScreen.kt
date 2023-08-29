package com.example.intelteamproject.compose

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.intelteamproject.data.MessengerUser
import com.example.intelteamproject.database.FirebaseAuthenticationManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(userUid: String, navController: NavController) {
    var messages = remember { mutableStateListOf("") }
    var newMessage by remember { mutableStateOf("") }
//    var messagesMap = remember { mutableStateMapOf<String, MutableList<Message>>() }
//    var messageList = remember { mutableStateListOf<Message>() }
    var displayedMessages by remember { mutableStateOf(emptyList<Message>()) }
    //메세지가 화면에 다 찼을 때, 새로운 메세지가 화면에 보일 수 있도록 위로 자동 스크롤 변수
//    var scrollToIndex by remember { mutableStateOf<Int?>(null) }
    val scrollState = rememberLazyListState()
    //firebase database에 연결 관련 변수
//    val database = Firebase.database
    val authManager = FirebaseAuthenticationManager()
    val currentUser = authManager.getCurrentUser()
    val currentUid = currentUser?.uid
    val usersCollection = Firebase.firestore.collection("users")
    val userList = remember { mutableStateListOf<MessengerUser>() }
    val roomNameList = remember { mutableStateListOf("","") }
    var roomName = ""
    roomNameList[0] = currentUid!!
    roomNameList[1] = userUid
    roomNameList.sortDescending()
    for (name in roomNameList) {
        roomName += name
    }
    //메세지 저장할 공간
    val messageRef =
        remember { Firebase.database.getReference("messages").child("$roomName") }
//    val newMessageRef = remember {messageRef.push()}

//    val messageRef = remember { Firebase.database.getReference("messages").child("message") }
//    //메세지 불러오는 함수
    LaunchedEffect(Unit) {
        messageRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val text = snapshot.child("text").getValue(String::class.java)
                val sender = snapshot.child("sender").getValue(String::class.java)
                val senderUid = snapshot.child("senderUid").getValue(String::class.java)
                val timestamp = snapshot.child("timestamp").getValue(Long::class.java)

                if (text != null && sender != null && senderUid != null && timestamp != null) {
                    val message = Message(text, sender, senderUid, timestamp)
//                    val roomMessages = messagesMap.getOrPut(newMessageRef.key!!) { mutableListOf() }
                    if (!displayedMessages.contains(message)) {
                        displayedMessages += message
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
//            TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//            TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
    //자동 스크롤되는 런처
    LaunchedEffect(displayedMessages.size) {
//        scrollToIndex = displayedMessages.size

        scrollState.animateScrollToItem(displayedMessages.size)

//        }
    }

    LaunchedEffect(Unit) {
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
                        userList.add(messengerUser)
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    var currentUserFirestore = userList.filter { currentUser.uid == it.uid }
    var otherUserFirestore = userList.filter { userUid == it.uid }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            state = scrollState,
            modifier = Modifier
                .background(Color.White)
                .padding(top = 60.dp, bottom = 55.dp)
        ) {
            itemsIndexed(displayedMessages) { index, message ->
                ConversationBox(index = index, message = message)
            }
        }
        Box(
            modifier = Modifier
                .height(60.dp)
                .align(Alignment.TopCenter)
                .fillMaxSize()
        ) {
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
                if (otherUserFirestore.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .width(350.dp)
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = otherUserFirestore[0].name,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    modifier = Modifier.background(Color.White),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(50.dp, 80.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
                            colors = IconButtonDefaults.iconButtonColors(Color.White),
                            modifier = Modifier.padding(bottom = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "기능 더 보기",
                                tint = Color.Red
                            )
                        }
                        TextField(
                            value = newMessage,
                            onValueChange = { newMessage = it },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                unfocusedIndicatorColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                disabledIndicatorColor = Color.White,
                                textColor = Color.Black,
                            ),
                            modifier = Modifier.width(300.dp)
                        )
                        if (currentUserFirestore.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    if (newMessage.isNotBlank()) {
//                                    printConversation = inputConversation
                                        val messageData = mapOf(
                                            "text" to newMessage,
                                            "sender" to currentUserFirestore[0].name,
                                            "senderUid" to currentUid,
                                            "timestamp" to ServerValue.TIMESTAMP
                                        )

                                        messageRef.push().setValue(messageData)


//                                    messageList.add(inputConversation)
                                        newMessage = ""

                                    }
                                },
                                colors = IconButtonDefaults.iconButtonColors(Color.White),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "대화 보내기",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ConversationBox(index: Int, message: Message?) {
    val authManager = FirebaseAuthenticationManager()
    val currentUser = authManager.getCurrentUser()
    val currentUid = currentUser?.uid
    //메세지와 같이 띄울 시간 포멧
    val timestampShow = SimpleDateFormat(
        "yyyy년 MM월 dd일 E요일 hh:mm",
        Locale.getDefault()
    ).format(Date(message?.timestamp as Long))

    message.text?.let {

            if (currentUid == message.senderUid) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(Color.LightGray),
                            shape = RoundedCornerShape(topStart = 25.dp, bottomStart = 5.dp),
                            contentPadding = PaddingValues(8.dp),
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Text(
                                text = message.text,
                                textAlign = TextAlign.Start,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                        Text(
                            text = timestampShow,
                            fontWeight = FontWeight.Light,
                            fontSize = 8.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Column {
                        Text(
                            text = message.sender,
                            textAlign = TextAlign.Start,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                        Button(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(Color.Gray),
                            shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 25.dp),
                            contentPadding = PaddingValues(8.dp),
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Text(
                                text = message.text,
                                textAlign = TextAlign.Start,
                                color = Color.White,
                                modifier = Modifier.wrapContentSize(),
                                fontSize = 16.sp
                            )
                        }
                        Text(
                            text = timestampShow,
                            fontWeight = FontWeight.Light,
                            fontSize = 8.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }


data class Message(
    val text: String? = null,
    val sender: String = "",
    val senderUid: String = "",
    val timestamp: Any? = null
)

