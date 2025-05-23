package com.example.greetingcard.presentation.view.plan.createplan

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.greetingcard.presentation.view.home.planning.CustomBottomBar
import com.example.greetingcard.presentation.viewModel.plan.createplan.CalendarViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarViewModel = viewModel(),
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
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                ),
                title = {
                    Text(
                        text = "여행일정 등록",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Close, contentDescription = "뒤로가기"
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            Log.d("innerPadding", "$innerPadding")
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // 스크롤 가능한 달력
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // 오늘 포함 13개월 생성
                    items(generateMonths(currentMonth, 13)) { month ->
                        MonthView(
                            yearMonth = month,
                            today = LocalDate.now(),
                            onDateSelected = { date -> viewModel.selectDate(date) },
                            selectedDates = selectedDates
                        )
                        Spacer(modifier = Modifier.height(20.dp)) // 각 달 간의 간격
                    }
                }
            }
        },
        bottomBar = {
            CustomBottomBar(
                label = travelDurationText,
                enabled = selectedDates.startDate != null && selectedDates.endDate != null,
                onBottomBarClick = { navController.navigate("plan_destination") }
            )
        },
    )
}

fun generateMonths(startMonth: LocalDate, months: Int = 13): List<YearMonth> {
    return (0 until months).map { startMonth.plusMonths(it.toLong()).yearMonth }
}

val LocalDate.yearMonth: YearMonth
    get() = YearMonth.of(year, month)