package com.example.intelteamproject.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.Orientation.*
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(navController: NavController) {
    Column(
        modifier = Modifier.background(color = Color(0xFF333333))
    ) {
        Top(title = "칸반 보드")
        KanbanBoard()
    }
    Column(
        verticalArrangement = Arrangement.Bottom
    ) {
        Bottom(navController = navController)
    }
}

@Composable
fun KanbanBoard() {
    val todoList = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "To Do",
                style = TextStyle(
                    fontSize = 19.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF172B4D),
                )
            )
            Text(
                text = "In progress",
                style = TextStyle(
                    fontSize = 19.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF172B4D),
                )
            )
            Text(
                text = "Done",
                style = TextStyle(
                    fontSize = 19.sp,
//                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF172B4D),
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                for ((index, todo) in todoList.withIndex()) {  // 인덱스를 같이 사용하도록 수정
                    KanbanColumn("To Do", todo,
                        //TODO: recomposition
                        onChange = { newValue ->
                            todoList[index] = newValue
                        },
                        onDelete = {  // onDelete 람다를 추가
                        todoList.removeAt(index)
//                        todoList = todoList.filterIndexed { i, _ -> i != index }
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        Button(
            onClick = {
                todoList.add( "To Do")
//                todoList = todoList + "To Do"
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF75462),
            )
        ) {
            Text("+")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanbanColumn(columnName: String, todo: String, onChange: (String) -> Unit, onDelete: () -> Unit) {
    val density = LocalDensity.current

    val configuration = LocalConfiguration.current
    val metrics = LocalContext.current.resources.displayMetrics
    val screenWidthPx = metrics.widthPixels.toFloat()
    val cardWidthPx = with(LocalDensity.current) { 100.dp.toPx() }
    val maxOffset = screenWidthPx - cardWidthPx

    var offset by remember { mutableStateOf(0f) }
//    var todoText by remember { mutableStateOf(todo) }
    var showDeleteButton by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    offset
                        .roundToInt()
                        .coerceIn(0, maxOffset.roundToInt()), 0
                )
            }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offset = (offset + delta).coerceIn(0f, maxOffset)
                }
            )
    ) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .background(Color.White)
                .padding(10.dp)
        ) {
            OutlinedTextField(
                value = todo,
                onValueChange = { newValue ->
                    onChange(newValue)
                    showDeleteButton = newValue.isNotBlank()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                ),
            )
            if (showDeleteButton) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                              onDelete()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(color = Color.Transparent),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF75462),
                    )
                ) {
                    Text("-")
                }
            }
        }
    }
}