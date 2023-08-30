package com.example.intelteamproject.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.intelteamproject.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun CalendarScreen(navController: NavController) {
    ScheduleScreen()
    Box() {
        Image(
            painter = painterResource(id = R.drawable.wetube_back),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )


    }
    Column(
        verticalArrangement = Arrangement.Bottom

    ) {
        Bottom(navController = navController)
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {
    Scaffold(
        modifier = Modifier
            .background(Color(0xFFF5F5F5))
            .padding(top = 40.dp, bottom = 70.dp),
        topBar = {
            // MyTopBar("스케줄") // MyTopBar 구현에 맞게 추가
        },
        bottomBar = {
            // MyBottomBara(navController) // MyBottomBara 구현에 맞게 추가
        }
    ) { innerPadding ->
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        var selectedDayIndex by remember { mutableStateOf(-1) }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))

        ) {
            CalendarComposable(
                modifier = Modifier.fillMaxWidth(),
                selectedDate = selectedDate,
                onDateSelected = { newSelectedDate, index ->
                    selectedDate = newSelectedDate
                    selectedDayIndex = index
                },
                onPreviousMonthClick = {
                    selectedDate = selectedDate.minusMonths(1)
                },
                onNextMonthClick = {
                    selectedDate = selectedDate.plusMonths(1)
                }
            )
//            if (selectedDayIndex != -1) {
//                MemoDialog(
//                    memo = "", // 여기서 초기 메모 값 설정
//                    onMemoChanged = { updatedMemo ->
//                        // 업데이트된 메모 처리
//                    },
//                    onDismiss = {
//                        selectedDayIndex = -1
//                    }
//                )
//            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
            val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
            val isDarkMode = isSystemInDarkTheme()

            daysOfWeek.forEachIndexed { index, day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    color = when (index) {
                        0 -> if (isDarkMode) Color.Red else Color.Red
                        6 -> if (isDarkMode) Color.Blue else Color.Blue
                        else -> if (isDarkMode && index in 1..5) Color.White else Color.Black
                    },
                    textAlign = TextAlign.Center
                )
            }
        }

        val firstDayOfMonth = selectedDate.withDayOfMonth(1)
        val lastDayOfMonth = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth())
        val daysInMonth = (1..lastDayOfMonth.dayOfMonth).toList()
        val emptyDaysBefore = (0 until firstDayOfMonth.dayOfWeek.value).toList()

        LazyVerticalGrid(
            GridCells.Fixed(7),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
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
                    onDateSelected = { selectedDate, index ->
                        onDateSelected(selectedDate, index)
                        showDialog = true
                    },
                    showDialog = showDialog,
                    memo = memo,
                    isFirstInRow = date.dayOfWeek.value == 7,
                    isLastInRow = date.dayOfWeek.value == 6
                )
            }
        }

        if (showDialog) {
            val selectedMemo = memoMap[selectedDate] ?: ""
            MemoDialog(
                memo = selectedMemo,
                onMemoChanged = { updatedMemo ->
                    if (updatedMemo.isNotBlank()) {
                        memoMap[selectedDate] = updatedMemo
                    } else {
                        memoMap.remove(selectedDate)
                    }
                    showDialog = false
                },
                onDismiss = { showDialog = false }
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

@Composable
fun CalendarDay(
    date: LocalDate,
    isSelected: Boolean,
    onDateSelected: (LocalDate, Int) -> Unit,
    showDialog: Boolean,
    memo: String?,
    isFirstInRow: Boolean,
    isLastInRow: Boolean
) {
    val textColor = when {
        date.dayOfWeek.value in 1..5 -> {
            if (isSystemInDarkTheme() && isSelected) Color.Black
            else if (isSystemInDarkTheme()) Color.White
            else Color.Black
        }
        date.dayOfWeek.value == 7 -> Color.Red // 일요일은 빨간색
        date.dayOfWeek.value == 6 -> Color.Blue // 토요일은 파란색
        else -> Color.Black
    }
    Box(
        modifier = Modifier
            .size(width = 28.dp, height = 90.dp)
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (isSelected) Modifier.border(
                    1.dp,
                    Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                )
                else Modifier
            )
            .clickable {
                onDateSelected(date, 0)
            },
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = textColor, // 수정된 부분
            modifier = Modifier
                .background(if (isSelected) Color.White else Color.Transparent)
                .padding(vertical = 2.dp, horizontal = 4.dp)
        )

        if (memo != null && memo.isNotEmpty()) {
            Text(
                text = memo,
                color = Color.White,
                fontSize = 8.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFFC4302B))
                    .padding(4.dp)
            )
        }
    }
}









