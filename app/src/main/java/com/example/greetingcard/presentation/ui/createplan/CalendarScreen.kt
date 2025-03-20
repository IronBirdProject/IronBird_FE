package com.example.greetingcard.presentation.ui.createplan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.greetingcard.ui.theme.Purple40
import com.example.greetingcard.ui.theme.PurpleGrey40
import com.example.greetingcard.ui.theme.PurpleGrey80
import com.example.greetingcard.presentation.viewModel.createplan.CalendarViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel(),
    onBack: () -> Unit = {} // 뒤로가기 콜백
) {
    val selectedDates by viewModel.selectedDates.collectAsState()
    val currentMonth = remember { LocalDate.now().withDayOfMonth(1) }
    val travelDurationText = if (selectedDates.startDate != null && selectedDates.endDate != null) {
        val days = ChronoUnit.DAYS.between(selectedDates.startDate, selectedDates.endDate)
        "${days}박 ${days + 1}일 일정 등록하기"
    } else {
        "날짜를 선택해주세요"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "여행일정 등록",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // 스크롤 가능한 달력
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp) // 스크롤 뷰 상하 패딩
                ) {
                    // 오늘 포함 13개월 생성
                    items(generateMonths(currentMonth, 13)) { month ->
                        MonthView(
                            yearMonth = month,
                            today = LocalDate.now(),
                            onDateSelected = { date -> viewModel.selectDate(date) },
                            selectedDates = selectedDates
                        )
                        Spacer(modifier = Modifier.height(24.dp)) // 각 달 간의 간격
                    }
                }

                Button(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(25.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = Color.Black.copy(alpha = 0.3f),
                            spotColor = Color.Black.copy(alpha = 0.5f)
                        ),
                    enabled = selectedDates.startDate != null && selectedDates.endDate != null,
                    colors = ButtonColors(
                        contentColor = Color.White,
                        containerColor = Purple40,
                        disabledContentColor = PurpleGrey40,
                        disabledContainerColor = PurpleGrey80
                    ),

                    onClick ={
                        // TODO : 다음 페이지로 이동
                    }
                ){
                    Text(
                        text = travelDurationText,
                        style = TextStyle(
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    )
}

fun generateMonths(startMonth: LocalDate, months: Int = 13): List<YearMonth> {
    return (0 until months).map { startMonth.plusMonths(it.toLong()).yearMonth }
}

val LocalDate.yearMonth: YearMonth
    get() = YearMonth.of(year, month)