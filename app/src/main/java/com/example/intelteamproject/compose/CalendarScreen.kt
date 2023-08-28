package com.example.intelteamproject.compose


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun CalendarScreen(navController: NavController) {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    CalendarComposable(
        selectedDate = selectedDate.value,
        onDateSelected = { date, index -> /* Handle date selected */ },
        onPreviousMonthClick = {
            selectedDate.value = selectedDate.value.minusMonths(1)
        },
        onNextMonthClick = {
            selectedDate.value = selectedDate.value.plusMonths(1)
        }
    )
}

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
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPreviousMonthClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
            }

            val headerText = selectedDate.format(DateTimeFormatter.ofPattern("yyyy년 M월"))
            Text(
                text = headerText, style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.Bold, fontSize = 18.sp
                ), modifier = Modifier.padding(vertical = 8.dp)
            )

            IconButton(onClick = onNextMonthClick) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "일",
                modifier = Modifier.weight(1f),
                color = Color.Red,
                textAlign = TextAlign.Center
            )
            Text(
                text = "월", modifier = Modifier.weight(1f), textAlign = TextAlign.Center
            )
            Text(
                text = "화", modifier = Modifier.weight(1f), textAlign = TextAlign.Center
            )
            Text(
                text = "수", modifier = Modifier.weight(1f), textAlign = TextAlign.Center
            )
            Text(
                text = "목", modifier = Modifier.weight(1f), textAlign = TextAlign.Center
            )
            Text(
                text = "금", modifier = Modifier.weight(1f), textAlign = TextAlign.Center
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
                    date = date, isSelected = isSelected, onDateSelected = { selectedDate ->
                        selectedDayIndex = index
                        showDialog = true
                    }, memo = memo
                )
            }
        }
        if (showDialog) {
            MemoDialog(memo = "", // 여기서 초기 메모 값 설정
                onMemoChanged = { updatedMemo ->
                    if (updatedMemo.isNotBlank()) {
                        memoMap[selectedDate] = updatedMemo
                    } else {
                        memoMap.remove(selectedDate)
                    }
                    showDialog = false // 다이얼로그 닫기
                }, onDismiss = { showDialog = false } // 다이얼로그 닫기
            )
        }
    }
}

@Composable
fun CalendarDay(
    date: LocalDate, isSelected: Boolean, onDateSelected: (LocalDate) -> Unit, memo: String?
) {
    var showDialog by remember { mutableStateOf(false) }
    var isMemoDialogOpen by remember { mutableStateOf(false) }

    // 추가: 날짜별 메모 표시 여부를 결정하는 변수
    var showMemo by remember { mutableStateOf(false) }

    Box(modifier = Modifier
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
        contentAlignment = Alignment.TopCenter) {
        Text(
            text = date.dayOfMonth.toString(),
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.Black
        )
        val showMemoText = isSelected && showMemo // 메모를 표시할지 여부 결정
        if (showMemoText) {
            val memoText = memo ?: "메모 없음"
            Text(
                text = memoText,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 12.sp, color = Color.Gray
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
    memo: String, onMemoChanged: (String) -> Unit, onDismiss: () -> Unit
) {
    var updatedMemo by remember { mutableStateOf(memo) }

    AlertDialog(onDismissRequest = { onDismiss() }, title = { Text("메모") }, text = {
        TextField(value = updatedMemo,
            onValueChange = { updatedMemo = it },
            label = { Text("메모를 입력하세요") },
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = {
                onMemoChanged(updatedMemo)
                onDismiss()
            })
        )
    }, confirmButton = {
        TextButton(onClick = {
            onMemoChanged(updatedMemo)
            onDismiss()
        }) {
            Text("확인")
        }
    })
}


