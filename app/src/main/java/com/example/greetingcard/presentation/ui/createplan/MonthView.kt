package com.example.greetingcard.presentation.ui.createplan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.ui.theme.Purple40
import com.example.greetingcard.ui.theme.PurpleGrey80
import com.example.greetingcard.presentation.viewModel.createplan.SelectedDates
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthView(
    yearMonth: YearMonth,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    selectedDates: SelectedDates
) {
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value % 7 // 일요일 시작 기준
    val days = (1..daysInMonth).map { yearMonth.atDay(it) }

    Column(modifier = Modifier.padding(8.dp)) {
        // 연도 및 월 표시
        Text(
            text = "${yearMonth.year}.${yearMonth.monthValue.toString().padStart(2, '0')}",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp), // 폰트 크기 키움
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        // 요일 헤더 표시 (일 ~ 토)
        Row {
            listOf("일", "월", "화", "수", "목", "금", "토").forEachIndexed { index, day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = when (index) {
                        0 -> Color.Red // 일요일 빨간색
                        6 -> Color.Blue // 토요일 파란색
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp), // 요일 글씨 키움
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 날짜 렌더링
        val rows = (days.size + firstDayOfWeek - 1) / 7 + 1
        for (row in 0 until rows) {
            Spacer(modifier = Modifier.height(4.dp)) // 각 주 간격
            Row {
                for (col in 0..6) {
                    val dayIndex = row * 7 + col - firstDayOfWeek
                    val date = days.getOrNull(dayIndex)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f) // 정사각형
                            .clickable(
                                enabled = selectedDates.startDate == null ||
                                        selectedDates.endDate != null ||
                                        (selectedDates.startDate != null && date?.isAfter(selectedDates.startDate) == true) ?: false,
                                onClick = { date?.let(onDateSelected) }
                            ),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (date != null) {
                            Column(
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // 날짜 숫자 렌더링
                                Box(
                                    modifier = Modifier
                                        .background(
                                            // color = when (date) {
                                            //     selectedDates.startDate, selectedDates.endDate -> Purple40
                                            //     else -> Color.Transparent // 선택되지 않은 날짜는 투명
                                            // },
                                            color = if (date in setOf(selectedDates.startDate, selectedDates.endDate)) {
                                                PurpleGrey80
                                            } else if (
                                                selectedDates.startDate != null
                                                && selectedDates.endDate != null
                                                && date.isAfter(selectedDates.startDate)
                                                && date.isBefore(selectedDates.endDate)
                                            ) {
                                                PurpleGrey80
                                            } else {
                                                Color.Transparent
                                            },
                                            shape = when {
                                                date == selectedDates.startDate -> {
                                                    when {
                                                        selectedDates.endDate == null -> RoundedCornerShape(
                                                            topStartPercent = 100,
                                                            topEndPercent = 100,
                                                            bottomStartPercent = 100,
                                                            bottomEndPercent = 100
                                                        )
                                                        else -> RoundedCornerShape(
                                                            topStartPercent = 100,
                                                            topEndPercent = 0,
                                                            bottomStartPercent = 100,
                                                            bottomEndPercent = 0
                                                        )
                                                    }
                                                }
                                                date == selectedDates.endDate -> RoundedCornerShape(topStartPercent = 0, topEndPercent = 100, bottomStartPercent = 0, bottomEndPercent = 100)
                                                else -> RoundedCornerShape(0)
                                            }
                                        )
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        color = when {
                                            date == today -> Purple40 // 오늘 날짜
                                            (selectedDates.startDate != null && date.isBefore(selectedDates.startDate)) || (selectedDates.endDate != null && date.isAfter(selectedDates.endDate)) -> {
                                                when (date.dayOfWeek) {
                                                    DayOfWeek.SUNDAY -> Color(0xFFFFCDD2) // 옅은 빨간색
                                                    DayOfWeek.SATURDAY -> Color(0xFFBBDEFB) // 옅은 파란색
                                                    else -> Color.Gray
                                                }
                                            }

                                            date.dayOfWeek == DayOfWeek.SUNDAY -> Color.Red
                                            date.dayOfWeek == DayOfWeek.SATURDAY -> Color.Blue
                                            else -> MaterialTheme.colorScheme.onSurface
                                        },
                                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // "출발", "도착", "오늘" 표시
                                when (date) {
                                    selectedDates.startDate -> {
                                        Text(
                                            text = "출발",
                                            color = Purple40,
                                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    selectedDates.endDate -> {
                                        Text(
                                            text = "도착",
                                            color = Purple40,
                                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    today -> {
                                        Text(
                                            text = "오늘",
                                            color = Purple40,
                                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}