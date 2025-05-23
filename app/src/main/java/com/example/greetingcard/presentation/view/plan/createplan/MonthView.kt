package com.example.greetingcard.presentation.view.plan.createplan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.presentation.viewModel.plan.createplan.SelectedDates
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

    Column(modifier = Modifier.padding(16.dp)) {
        // 연도 및 월 표시
        Text(
            text = "${yearMonth.year}.${yearMonth.monthValue.toString().padStart(2, '0')}",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 요일 헤더 (Apple 감성: 얇고 깔끔한 폰트)
        Row(modifier = Modifier.padding(bottom = 6.dp)) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEachIndexed { index, day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = when (index) {
                        0 -> Color(0xFFFF6B6B) // 부드러운 레드 (일요일)
                        6 -> Color(0xFF4A90E2) // 부드러운 블루 (토요일)
                        else -> Color(0xFF333333) // 기본 블랙
                    },
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 날짜 렌더링
        val rows = (days.size + firstDayOfWeek - 1) / 7 + 1
        for (row in 0 until rows) {
            Spacer(modifier = Modifier.height(6.dp)) // 각 주 간격 조정
            Row {
                for (col in 0..6) {
                    val dayIndex = row * 7 + col - firstDayOfWeek
                    val date = days.getOrNull(dayIndex)

                    Box(
                        modifier = Modifier
//                            .padding(2.dp)
                            .background(
                                color = if (date != null) {
                                    when {
                                        date in setOf(
                                            selectedDates.startDate, selectedDates.endDate
                                        ) -> Color(0xffe7f2fe) // 부드러운 블루
                                        selectedDates.startDate != null && selectedDates.endDate != null && date.isAfter(
                                            selectedDates.startDate
                                        ) && date.isBefore(selectedDates.endDate) -> Color(
                                            0xffe7f2fe
                                        ) // 선택된 범위 (연한 블루)
                                        selectedDates.startDate != null && selectedDates.endDate == null
                                            -> Color.Transparent

                                        else -> Color.Transparent
                                    }
                                } else {
                                    Color.Transparent
                                }, shape = RoundedCornerShape(
                                    topStart = if (date == selectedDates.startDate) {
                                        25.dp
                                    } else {
                                        0.dp
                                    },
                                    bottomStart = if (date == selectedDates.startDate) {
                                        25.dp
                                    } else {
                                        0.dp
                                    },
                                    topEnd = if (date == selectedDates.endDate) {
                                        25.dp
                                    } else if (selectedDates.startDate != null && selectedDates.endDate == null) {
                                        25.dp
                                    } else {
                                        0.dp
                                    },
                                    bottomEnd = if (date == selectedDates.endDate) {
                                        25.dp
                                    } else if (selectedDates.startDate != null && selectedDates.endDate == null) {
                                        25.dp
                                    } else {
                                        0.dp
                                    },
                                )
                            )
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable(
                                enabled = date != null
                                        && !date.isBefore(LocalDate.now()) // 오늘 이전 날짜는 비활성화
                                        && (
                                        selectedDates.startDate == null
                                                || selectedDates.endDate != null
                                                || date == selectedDates.startDate
                                                || date.isAfter(selectedDates.startDate)
                                        ),
                                onClick = { date?.let(onDateSelected) }),
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            // 날짜 숫자 UI
                            Text(
                                text = date.dayOfMonth.toString(),
                                color = when {
                                    date == today -> Color(0xff000000) // 오늘 날짜
                                    date == selectedDates.startDate || date == selectedDates.endDate -> Color.White // 오늘 날짜
                                    (selectedDates.startDate != null && date.isBefore(
                                        selectedDates.startDate
                                    )) || (selectedDates.endDate != null && date.isAfter(
                                        selectedDates.endDate
                                    )) -> Color(0xFF828f9e) // 지난 날짜는 연한 그레이
                                    date.dayOfWeek == DayOfWeek.SUNDAY -> Color(0xFFFF6B6B)
                                    date.dayOfWeek == DayOfWeek.SATURDAY -> Color(0xFF4A90E2)
                                    else -> Color(0xFF828f9e)
                                },
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                            // "출발", "도착", "오늘" 텍스트 디자인 개선
                            when (date) {
                                today -> {
                                    DateLabelText(
                                        date = date.dayOfMonth.toString(),
                                        label = if (date.dayOfMonth == selectedDates.startDate?.dayOfMonth) "출발" else "●",
                                        color = Color.White,
                                        modifier = Modifier.align(Alignment.BottomCenter)
                                    )
                                }

                                selectedDates.startDate -> {
                                    DateLabelText(
                                        date = date.dayOfMonth.toString(),
                                        label = "출발",
                                        color = Color.White,
                                        modifier = Modifier.align(Alignment.BottomCenter)
                                    )
                                }

                                selectedDates.endDate -> {
                                    DateLabelText(
                                        date = date.dayOfMonth.toString(),
                                        label = "도착",
                                        color = Color.White,
                                        modifier = Modifier.align(Alignment.BottomCenter)
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

@Composable
fun DateLabelText(date: String, label: String, color: Color, modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                shape = CircleShape, color = Color(0xff057bf6)
            ), contentAlignment = Alignment.Center
    ) {
        Text(
            text = date,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Text(
            text = label, color = color, style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 9.sp
            ),
            //        fontWeight = FontWeight.Bold,
            modifier = modifier
        )
    }
}