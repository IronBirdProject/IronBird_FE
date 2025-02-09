package com.example.greetingcard.presentation.ui.createplan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarView(onDateSelected: (LocalDate) -> Unit) {
    val currentMonth = remember { YearMonth.now() }
    val today = remember { LocalDate.now() }
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()

    // 날짜 계산
    val firstDayOffset = firstDayOfMonth.dayOfWeek.value % 7 // 첫 번째 요일의 오프셋
    val lastDayOffset = (7 - lastDayOfMonth.dayOfWeek.value % 7) % 7 // 마지막 요일의 남는 칸
    val days = List(firstDayOffset) { null } + // 첫 주 빈 칸
            (1..lastDayOfMonth.dayOfMonth).map { firstDayOfMonth.plusDays((it - 1).toLong()) } + // 날짜
            List(lastDayOffset) { null } // 마지막 주 빈 칸
    val groupedDates = days.chunked(7)

    // UI 구성
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp), // 상단 여백 추가
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 월과 연도 표시
        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )

        // 요일 헤더
        val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            daysOfWeek.forEach { dayOfWeek ->
                Text(
                    text = dayOfWeek,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        // 날짜 표시
        groupedDates.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly // 균등 정렬
            ) {
                week.forEach { date ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { date?.let { onDateSelected(it) } },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date?.dayOfMonth?.toString() ?: "",
                            color = if (date == today) androidx.compose.ui.graphics.Color.Red else androidx.compose.ui.graphics.Color.Black
                        )
                    }
                }
            }
        }
    }
}