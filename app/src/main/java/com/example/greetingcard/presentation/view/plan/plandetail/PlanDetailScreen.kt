package com.example.greetingcard.presentation.view.plan.plandetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.greetingcard.data.model.response.Plan
import com.example.greetingcard.data.model.response.Schedule
import com.example.greetingcard.presentation.ui.common.CustomLoadingIndicator
import com.example.greetingcard.presentation.view.plan.plandetail.schedule.AddScheduleBottomSheet
import com.example.greetingcard.presentation.view.plan.plandetail.schedule.ScheduleDetailBottomSheet
import com.example.greetingcard.presentation.view.plan.plandetail.schedule.ScheduleItem
import com.example.greetingcard.presentation.viewModel.plan.plandetail.PlanDetailViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun PlanDetailScreen(planId: Int?, planDetailViewModel: PlanDetailViewModel) {
    // 화면 진입 시 데이터 로딩
    LaunchedEffect(planId) {
        planId?.let {
            planDetailViewModel.fetchPlanDetails(it)
        }
    }

    val planState by planDetailViewModel.planDetailState.collectAsState()

    when {
        // 로딩
        planState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomLoadingIndicator()
            }
        }
        // 에러 발생
        planState.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("해당 여행 정보를'\n'찾을 수 없어요!")
            }
        }

        // 정상적으로 로딩 완료
        planState.plan != null -> {
            PlanDetailContent(plan = planState.plan!!, planDetailViewModel = planDetailViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailContent(plan: Plan, planDetailViewModel: PlanDetailViewModel) {
    val scheduleSheetState = rememberModalBottomSheetState()
    val addLocationSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // 선택한 스케쥴
    var selectedSchedule by remember { mutableStateOf<Schedule?>(null) }

    // 장소 추가 다이얼로그 상태
    var isAddLocationModalOpen by remember { mutableStateOf(false) }
    var newLocationText by remember { mutableStateOf("") }

    selectedSchedule?.let { schedule ->
        ModalBottomSheet(
            sheetState = scheduleSheetState,
            onDismissRequest = { selectedSchedule = null },
            containerColor = Color.White
        ) {
            ScheduleDetailBottomSheet(
                schedule = selectedSchedule, onSaveMemo = { memo ->
                    selectedSchedule = null
                })
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 배경 이미지
//        plan.backgroundImg?.let { imageUrl ->
        AsyncImage(
            model = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/26/97/39/7f/caption.jpg?w=2400&h=-1&s=1&cx=1920&cy=1080&chk=v1_f31158e4bb953d28a308",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFECECEC), shape = RoundedCornerShape(
                        bottomStart = 30.dp,
                        bottomEnd = 30.dp,
                    )
                )
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = 30.dp,
                        bottomEnd = 30.dp,
                    )
                )
                .height(250.dp),
        )

        var selectedDay by remember { mutableStateOf(1) }

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // 👉 날짜 계산
        val dayDateList = remember(plan.startedDate, plan.endDate) {
            val startDate = LocalDate.parse(plan.startedDate, dateFormatter)
            val endDate = LocalDate.parse(plan.endDate, dateFormatter)
            val totalDays = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

            (1..totalDays).map { i ->
                val date = startDate.plusDays((i - 1).toLong())
                i to date.format(DateTimeFormatter.ofPattern("MM/dd"))
            }
        }

        LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {

            item {
                // 제목 & 날짜
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = plan.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${plan.startedDate} - ${plan.endDate}", color = Color.Gray)
                }
            }

            dayDateList.forEach { (day, dateText) ->
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Text(
                            text = "DAY $day - $dateText",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        val schedules = plan.schedules.filter { it.day == day }
                        schedules.forEach { schedule ->
                            ScheduleItem(schedule = schedule, onScheduleClicked = {
                                selectedSchedule = schedule
                            })
                        }

                        OutlinedButton(
                            onClick = {
                                selectedDay = day
                                isAddLocationModalOpen = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("장소 추가", color = Color.Black)
                        }
                    }
                }
            }
        }

        // 장소 추가 BottomSheet
        if (isAddLocationModalOpen) {
            ModalBottomSheet(
                sheetState = addLocationSheetState,
                onDismissRequest = { isAddLocationModalOpen = false },
                containerColor = Color.White,
            ) {
                AddScheduleBottomSheet(
                    selectedDay = selectedDay,
                    onSave = { title, desc, cost, memo, time ->
                        planDetailViewModel.addSchedule(
                            description = desc,
                            time = time,
                            day = selectedDay,
                            cost = cost,
                            memo = memo,
                            planId = plan.id
                        )
                        isAddLocationModalOpen = false
                    },
                    onCancel = { isAddLocationModalOpen = false }
                )
            }
        }
    }
}



