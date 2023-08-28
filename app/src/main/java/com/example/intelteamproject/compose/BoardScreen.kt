package com.example.intelteamproject.compose

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(navController: NavController) {
    KanbanBoard()
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanbanBoard() {
    var todoList by remember { mutableStateOf(listOf("To Do")) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Kanban Board",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Divider(modifier = Modifier.height(3.dp))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "To Do",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "In Progress",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Done",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 드래그 가능한 박스

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    for (todo in todoList) {
                        KanbanColumn("To Do", todo)
                        Spacer(modifier = Modifier.height(2.dp))
                    }

                Button(
                    onClick = {
                        todoList = todoList + "To Do"
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize()

                ) {
                    Text("+")
                } }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanbanColumn(columnName: String, todo: String) {
    val density = LocalDensity.current

    val configuration = LocalConfiguration.current
    val metrics = LocalContext.current.resources.displayMetrics
    val screenWidthPx = metrics.widthPixels.toFloat()
    val cardWidthPx = with(LocalDensity.current) { 100.dp.toPx() }
    val maxOffset = screenWidthPx - cardWidthPx

    var offset by remember { mutableStateOf(0f) }
    var todoText by remember { mutableStateOf(todo) }

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
                orientation = Horizontal,
                state = rememberDraggableState { delta ->
                    offset = (offset + delta).coerceIn(0f, maxOffset)
                }
            )
    ) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .background(Color.LightGray)
        ) {
            TextField(
                value = todoText,
                onValueChange = { newValue ->
                    todoText = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
            )
        }
    }
}
