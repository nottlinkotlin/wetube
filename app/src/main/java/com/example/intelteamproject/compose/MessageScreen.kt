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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(navController: NavController) {
    var messages = remember { mutableStateListOf("") }
    var newMessage by remember { mutableStateOf("") }
//    var printConversation by remember { mutableStateOf("") }
//    var messageList = remember { mutableStateListOf<Message>() }
    var displayedMessages by remember { mutableStateOf(emptyList<Message>()) }
//    var newMessages = remember { mutableStateOf<Message?>(null) }
    //메세지가 화면에 다 찼을 때, 새로운 메세지가 화면에 보일 수 있도록 위로 자동 스크롤 변수
    var scrollToIndex by remember { mutableStateOf<Int?>(null) }
    val scrollState = rememberLazyListState()
    //firebase database에 연결 관련 변수
    val database = Firebase.database
    //메세지 저장할 공간
//    val messageRef = database.getReference("messages")
    val messageRef = database.getReference("messages").child("message")
//    var isInitialDataLoaded by remember { mutableStateOf(false) }
    //messageList 안에 들어 있는 값을 Map으로 전환 후 저장
//    val messageMap = messageList.mapIndexed { index, message ->
//        index.toString() to message
//    }.toMap()
//    //메세지 불러오는 함수
    messageRef.addChildEventListener(object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            //실패 코드(list -> map -> list)
//            val messagesMap = snapshot.value as? Map<String, Any>
//            val newMessageList = messagesMap?.values?.map {it as? Map<String, Any> }
//                ?.mapNotNull { map ->
//                    val text = map?.get("text") as? String
//                    val sender = map?.get("sender") as? String
//                    val timestamp = map?.get("timestamp") as? Long
//                    if (text != null && sender != null && timestamp != null) {
//                        Message(text, sender, timestamp)
//                    } else {
//                        null
//                    }
//                } ?: emptyList()
//            messageList = rememberUpdatedState(newValue = newMessageList)
            //저장은 됐는데 출력이 안됨
//                val value = snapshot.getValue(Message::class.java)
//                if (value != null) {
//                    messageList.add(value)
//                }

            //일단 최종 코드
//            for (messageSnapshot in snapshot.children) {
//                val text = messageSnapshot.child("text").getValue(String::class.java)
//                val sender = messageSnapshot.child("sender").getValue(String::class.java)
//                val timestamp = messageSnapshot.child("timestamp").getValue(Any::class.java)
//
//                if (text != null && sender != null && timestamp != null) {
//                    val message = Message(text, sender, timestamp)
//                    messageList.add(message)
//                }
//            }
            //코드 수정 -> 위랑 똑같음(실패)
//            val newMessages = snapshot.children.mapNotNull { messageSnapshot ->
//                val text = messageSnapshot.child("text").getValue(String::class.java)
//                val sender = messageSnapshot.child("sender").getValue(String::class.java)
//                val timestamp = messageSnapshot.child("timestamp").getValue(Any::class.java)
//
//                if (text != null && sender != null && timestamp != null) {
//                    Message(text, sender, timestamp)
//                } else {
//                    null
//                }
//            }
//            messageList += newMessages
            //한번말 실행되게 한 후 새로 저장되는 메세지만 출력
//            val messageList = mutableListOf<Message>()
//            if (!isInitialDataLoaded) {
//                isInitialDataLoaded = true
//                messageList.clear()
//
//                for (messageSnapshot in snapshot.children) {
//                    val text = messageSnapshot.child("text").getValue(String::class.java)
//                    val sender = messageSnapshot.child("sender").getValue(String::class.java)
//                    val timestamp = messageSnapshot.child("timestamp").getValue(Any::class.java)
//
//                    if (text != null && sender != null && timestamp != null) {
//                        val message = Message(text, sender, timestamp)
//                        messageList.add(message)
////                        displayedMessages = ArrayList(messageList)
//                    }
//                }
//                displayedMessages = messageList.toMutableStateList()
//            } else {
////                val messageList = mutableListOf<Message>()
//                for (messageSnapshot in snapshot.children) {
//                    val text = messageSnapshot.child("text").getValue(String::class.java)
//                    val sender = messageSnapshot.child("sender").getValue(String::class.java)
//                    val timestamp = messageSnapshot.child("timestamp").getValue(Any::class.java)
//
//                    if (text != null && sender != null && timestamp != null) {
//                        val message = Message(text, sender, timestamp)
//                        messageList.add(message)
//                    }
//                }
//                val newMessages = messageList - displayedMessages.toSet()
//                displayedMessages = (displayedMessages + newMessages).toMutableStateList()
//            }
            //다시 수정(저장된 메세지 중복 출력 이슈 => 원인은 자동 스크롤로 추정)
            val text = snapshot.child("text").getValue(String::class.java)
            val sender = snapshot.child("sender").getValue(String::class.java)
            val timestamp = snapshot.child("timestamp").getValue(Long::class.java)

            if (text != null && sender != null && timestamp != null) {
                val message = Message(text, sender, timestamp)
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
//    //채팅방 들어갔을 때 자동으로 밑으로 스크롤되는 런처
//    LaunchedEffect(displayedMessages) {
//        if (displayedMessages.isNotEmpty()) {
//            scrollState.animateScrollToItem(displayedMessages.size)
//        }
//    }
    //자동 스크롤되는 런처
    LaunchedEffect(displayedMessages.size) {
//        scrollToIndex = displayedMessages.size

//        scrollToIndex?.let { index ->
//            if (index >= 0 && index < displayedMessages.size) {
                scrollState.animateScrollToItem(displayedMessages.size)
//            }
//            scrollToIndex = null
//        }
    }


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
                Row(
                    modifier = Modifier
                        .width(350.dp)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "상대방 이름", fontSize = 18.sp, color = Color.Black)
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
                                tint = Color(0xFF1B1D1F)
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
                        IconButton(
                            onClick = {
                                if (newMessage.isNotBlank()) {
//                                    printConversation = inputConversation
                                    val messageData = mapOf(
                                        "text" to newMessage,
                                        "sender" to "너",
                                        "timestamp" to ServerValue.TIMESTAMP
                                    )
                                    messageRef.push().setValue(messageData)

//                                    messageList.add(inputConversation)
                                    newMessage = ""

//                                    scrollToIndex = displayedMessages.size - 1
//                                    scrollToIndex = displayedMessages.size
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(Color.White),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "대화 보내기",
                                tint = Color(0xFF1B1D1F)
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ConversationBox(index: Int?, message: Message?) {
    var timestampShow = SimpleDateFormat("yyyy년 MM월 dd일 E요일 hh:mm", Locale.getDefault()).format(Date(message?.timestamp as Long))


    message.text?.let {
        if (index != null) {
            if (index % 2 == 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Column {
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
                                color = Color.White
                            )
                        }
                        Text(text = timestampShow)
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
                                modifier = Modifier.wrapContentSize()
                            )
                        }
                        Text(text = timestampShow)
                    }
                }
            }
        }
    }
}

data class Message(
    val text: String? = null,
    val sender: String = "",
    val timestamp: Any? = null
)

