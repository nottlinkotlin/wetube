package com.example.intelteamproject.compose


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(navController: NavController) {

    KanbanBoard()
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun KanbanBoard() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(600.dp)
                .background(color = Color.White)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Kanban Board",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Divider(modifier = Modifier.height(3.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
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
            }
            Spacer(modifier = Modifier.height(10.dp))
            // Add the task columns for each tab
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                KanbanColumn()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanbanColumn() {
    val cellSize = 100.dp
    var offsetX by remember { mutableStateOf(0f) }
    val tasks = remember { mutableStateListOf<String>() } // Maintain a list of tasks

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(8.dp)
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX += delta
                }
            )
    ) {

        var taskText by remember { mutableStateOf(TextFieldValue()) }

        TextField(
            value = taskText,
            onValueChange = { taskText = it },
            modifier = Modifier
                .height(120.dp)
                .width(120.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(Color.Blue)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Add some spacing
        // Display the tasks below the TextField
//        Column {
//            tasks.forEachIndexed { index, task ->
//                Text(
//                    text = task,
//                    modifier = Modifier.padding(start = 4.dp),
//                    color = Color.Black
//                )
//            }
//        }
    }
}
