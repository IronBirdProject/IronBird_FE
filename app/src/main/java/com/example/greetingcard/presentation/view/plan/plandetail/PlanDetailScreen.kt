package com.example.greetingcard.presentation.view.plan.plandetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.greetingcard.data.model.response.Plan
import com.example.greetingcard.data.model.response.Schedule
import com.example.greetingcard.presentation.ui.common.CustomLoadingIndicator
import com.example.greetingcard.presentation.viewModel.plan.plandetail.PlanDetailViewModel

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
            PlanDetailContent(plan = planState.plan!!)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailContent(plan: Plan) {
    val scheduleSheetState = rememberModalBottomSheetState()
    val addLocationSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

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
                schedule = selectedSchedule,
                onSaveMemo = { memo ->
                    selectedSchedule = null
                }
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 배경 이미지
        plan.backgroundImg?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        shape = RoundedCornerShape(
                            bottomStart = 30.dp,
                            bottomEnd = 30.dp,
                        )
                    )
                    .height(250.dp),
            )
        }

        // 제목 & 날짜
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = plan.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${plan.startDate} - ${plan.endDate}", color = Color.Gray)
        }

        // Day 탭 (단순 예시)
        val dayList = plan.schedule.groupBy { it.day }.toSortedMap()
        val selectedDay = remember { mutableStateOf(dayList.keys.first()) }

        ScrollableTabRow(
            containerColor = Color.White,
            selectedTabIndex = selectedDay.value - 1,
            edgePadding = 10.dp,
            indicator = {},
            divider = {}
        ) {
            dayList.keys.forEachIndexed { index, day ->
                Tab(
                    // 선택된 탭 배경색 표시
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 3.dp)
                        .clip(CircleShape)
                        .border(
                            width = 0.5.dp,
                            color = if (index == selectedDay.value - 1) Color.Transparent else Color.LightGray,
                            shape = CircleShape
                        )
                        .background(
                            color =
                                if (index == (selectedDay.value - 1)) Color(0xff057bf6)
                                else Color.Transparent
                        ),
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray,
                    selected = selectedDay.value == day,
                    onClick = { selectedDay.value = day },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("DAY $day", fontWeight = FontWeight.Bold)
                            Text("1월 ${4 + index}일", fontSize = 12.sp) // 더미 날짜 로직
                        }
                    }
                )
            }
        }

        // 일정 리스트
        LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
            items(dayList[selectedDay.value] ?: emptyList()) { schedule ->
                ScheduleItem(
                    schedule = schedule,
                    onScheduleClicked = {
                        selectedSchedule = schedule
                    })
            }
            item {
                Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                    OutlinedButton(
//                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        onClick = { isAddLocationModalOpen = true }
                    ) {
                        Text("장소 추가", color = Color.Black)
                    }

                    if (isAddLocationModalOpen) {
                        ModalBottomSheet(
                            sheetState = addLocationSheetState,
                            onDismissRequest = { isAddLocationModalOpen = false },
                            containerColor = Color.White,
                        ) {
                            AddLocationBottomSheet(
                                onSave = { title, desc, amount, memo ->
                                    // 저장 로직 처리
                                    println("장소 저장: $title, $desc, $amount, $memo")
                                    isAddLocationModalOpen = false
                                },
                                onCancel = {
                                    isAddLocationModalOpen = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleItem(schedule: Schedule, onScheduleClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 시간
        Text(
            textAlign = TextAlign.Center,
            text = schedule.time,
            modifier = Modifier.padding(start = 16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )

        // 카드
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color.Black.copy(alpha = 0.05f),
                    spotColor = Color.Black.copy(alpha = 0.4f)
                )
                .clickable {
                    onScheduleClicked()
                },
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Column {
                    Text(text = schedule.description, fontWeight = FontWeight.Bold)

                    schedule.memo?.let {
                        Text(text = it, fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (schedule.cost != null) {
                        Text(
                            text = "금액 ${"%,d원".format(schedule.cost)}",
                            color = Color(0xFF333333),
                            fontSize = 12.sp
                        )
                    } else {
                        Text(
                            text = "💰 미정원",
                            color = Color(0xFF999999),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleDetailBottomSheet(schedule: Schedule?, onSaveMemo: (String) -> Unit) {
    var memo by remember { mutableStateOf(schedule?.memo ?: "") }

    Column(modifier = Modifier.padding(horizontal = 30.dp)) {
        // 스케쥴 description
        Text(text = schedule!!.description, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.ChecklistRtl,
                tint = if (schedule.memo != null) Color.Black else Color.Gray,
                contentDescription = "memo_icon"
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = schedule.memo ?: "메모",
                fontSize = 13.sp,
                color = if (schedule.memo != null) Color.Black else Color.Gray
            )

        }
        Spacer(modifier = Modifier.height(50.dp))
//        TextField(
//            value = memo,
//            onValueChange = { memo = it },
//            label = { Text("메모 작성") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = { onSaveMemo(memo) }, modifier = Modifier.align(Alignment.End)) {
//            Text("저장")
//        }
    }
}

// 장소추가 바텀모달 컴포저블
@Composable
fun AddLocationBottomSheet(
    onSave: (title: String, description: String, amount: String, memo: String) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "장소 추가",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("설명") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("필요 금액") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = memo,
            onValueChange = { memo = it },
            label = { Text("메모") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text("취소")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    onSave(title, description, amount, memo)
                }
            ) {
                Text("저장")
            }
        }
    }
}
