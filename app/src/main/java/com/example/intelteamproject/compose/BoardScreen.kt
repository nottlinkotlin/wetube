package com.example.intelteamproject.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(navController: NavController) {
    CalendarComposable(
        selectedDate = LocalDate.now(), // 초기 선택된 날짜 설정
        onDateSelected = { date, _ -> /* 날짜 선택 시 동작 수행 */ },
        onPreviousMonthClick = { /* 이전 달로 이동 버튼 클릭 시 동작 수행 */ },
        onNextMonthClick = { /* 다음 달로 이동 버튼 클릭 시 동작 수행 */ }
    )

}


// 달력구성 컴포저블
@Composable
fun CalendarComposable(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate, Int) -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    var memoMap by remember { mutableStateOf(mutableMapOf<LocalDate, String>()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedDayIndex by remember { mutableStateOf(-1) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPreviousMonthClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
            }

            val headerText =
                selectedDate.format(DateTimeFormatter.ofPattern("yyyy년 M월"))
            Text(
                text = headerText,
                style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            IconButton(onClick = onNextMonthClick) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "일",
                modifier = Modifier.weight(1f),
                color = Color.Red,
                textAlign = TextAlign.Center
            )
            Text(
                text = "월",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = "화",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = "수",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = "목",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = "금",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = "토",
                modifier = Modifier.weight(1f),
                color = Color.Blue,
                textAlign = TextAlign.Center
            )
        }
        val firstDayOfMonth = selectedDate.withDayOfMonth(1)
        val lastDayOfMonth = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth())

        val daysInMonth = (1..lastDayOfMonth.dayOfMonth).toList()
        val emptyDaysBefore = (1 until firstDayOfMonth.dayOfWeek.value).toList()

        LazyVerticalGrid(
            GridCells.Fixed(7), // 각 행당 7개의 열을 가지도록 설정
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Display empty boxes for days before the first day of the month
            items(emptyDaysBefore) {
                Spacer(modifier = Modifier.size(30.dp))
            }
            itemsIndexed(daysInMonth) { index, day ->
                val date = selectedDate.withDayOfMonth(day)
                val isSelected = date == selectedDate
                val memo = memoMap[date] ?: ""

                Divider()
                CalendarDay(
                    date = date,
                    isSelected = isSelected,
                    onDateSelected = { selectedDate ->
                        selectedDayIndex = index
                        showDialog = true
                    },
                    memo = memo
                )
            }
        }
        if (showDialog) {
            MemoDialog(
                memo = "", // 여기서 초기 메모 값 설정
                onMemoChanged = { updatedMemo ->
                    if (updatedMemo.isNotBlank()) {
                        memoMap[selectedDate] = updatedMemo
                    } else {
                        memoMap.remove(selectedDate)
                    }
                    showDialog = false // 다이얼로그 닫기
                },
                onDismiss = { showDialog = false } // 다이얼로그 닫기
            )
        }
    }
}


@Composable
fun CalendarDay(
    date: LocalDate,
    isSelected: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    memo: String?
) {
    var showDialog by remember { mutableStateOf(false) }
//    var memoMap by remember { mutableStateOf(mutableMapOf<LocalDate, String>()) }
    var isMemoDialogOpen by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(width = 30.dp, height = 110.dp)
            .clip(RectangleShape)
            .clickable {
                if (isSelected) {
                    // 선택된 날짜 클릭 시 메모 다이얼로그 열기
                    isMemoDialogOpen = true
                } else {
                    // 선택되지 않은 날짜 클릭 시 날짜 선택 동작 수행
                    onDateSelected(date)
                }
            }
            .background(if (isSelected) Color.Gray else Color.Transparent),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.Black
        )
        val showMemo = isSelected && showDialog
        if (showMemo) {
            val memoText = memo ?: "메모 없음"
            Text(
                text = memoText,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoDialog(
    memo: String,
    onMemoChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var updatedMemo by remember { mutableStateOf(memo) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("메모") },
        text = {
            TextField(
                value = updatedMemo,
                onValueChange = { updatedMemo = it },
                label = { Text("메모를 입력하세요") },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        onMemoChanged(updatedMemo)
                        onDismiss()
                    }
                )
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onMemoChanged(updatedMemo)
                onDismiss()
            }) {
                Text("확인")
            }
        }
    )
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
                KanbanColumn()
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
        Box(
            modifier = Modifier
                .size(cellSize)
                .background(Color.White)
                .padding(4.dp)
                .offset { IntOffset((offsetX).roundToInt(), 0) }
        ) {
            var taskText by remember { mutableStateOf(TextFieldValue()) }

            TextField(
                value = taskText,
                onValueChange = { taskText = it },
                modifier = Modifier.fillMaxSize(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Do something with the task text
                    }
                ),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(Color.Blue)
            )
        }
    }
}



