package com.example.intelteamproject.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


data class Task(val id: Int, val name: String)

@Preview(showBackground = true)
@Composable
fun KanbanBoard() {
    val tasks = remember {
        mutableStateOf(
            listOf(
                Task(1, "Task 1"),
                Task(2, "Task 2"),
                Task(3, "Task 3")
            )
        )
    }
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .horizontalScroll(scrollState) // 수평 스크롤을 추가 합니다.
            .background(Color.Gray),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val columnNames = listOf(
            "To Do",
            "In Progress",
            "Done",
            "Extra 1",
            "Extra 2"
        ) // 열을 늘려 스크롤을 확인할 수 있도록 합니다.
        val tasks = listOf("Task 1", "Task 2", "Task 3")
        columnNames.forEach { columnName ->
            KanbanColumn(columnName, tasks)
        }
    }
}

@Composable
fun KanbanColumn(columnName: String, tasks: List<String>) {
    var offset by remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .padding(8.dp)
            .offset { IntOffset(offset.roundToInt(), 0) }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offset += delta
                }
            )
    ) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .background(Color.LightGray)
        ) {
            Text(text = columnName, color = MaterialTheme.colorScheme.onPrimary)
            tasks.forEach { task ->
                Text(text = task, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}