package com.example.greetingcard.presentation.view.plan.plandetail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.greetingcard.R
import com.example.greetingcard.data.model.response.Plan
import com.example.greetingcard.data.model.response.Schedule
import com.example.greetingcard.presentation.ui.common.CustomLoadingIndicator
import com.example.greetingcard.presentation.view.plan.component.EditTitleDialog
import com.example.greetingcard.presentation.view.plan.component.PlanDeleteDialog
import com.example.greetingcard.presentation.view.plan.plandetail.schedule.AddScheduleBottomSheet
import com.example.greetingcard.presentation.view.plan.plandetail.schedule.ScheduleDetailBottomSheet
import com.example.greetingcard.presentation.view.plan.plandetail.schedule.ScheduleItem
import com.example.greetingcard.presentation.viewModel.plan.plandetail.PlanDetailViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun PlanDetailScreen(
    planId: Int?, planDetailViewModel: PlanDetailViewModel, navController: NavController
) {
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
            PlanDetailContent(
                plan = planState.plan!!,
                planDetailViewModel = planDetailViewModel,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailContent(
    plan: Plan, planDetailViewModel: PlanDetailViewModel, navController: NavController
) {
    val context = LocalContext.current
    val scheduleSheetState = rememberModalBottomSheetState()
    val addLocationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedSchedule by remember { mutableStateOf<Schedule?>(null) }
    var isAddLocationModalOpen by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayDateList = remember(plan.startedDate, plan.endDate) {
        val startDate = LocalDate.parse(plan.startedDate, dateFormatter)
        val endDate = LocalDate.parse(plan.endDate, dateFormatter)
        val totalDays = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
        (1..totalDays).map { i ->
            val date = startDate.plusDays((i - 1).toLong())
            i to date.format(DateTimeFormatter.ofPattern("MM/dd (E)", Locale.KOREAN))
        }
    }

    var selectedDay by remember { mutableIntStateOf(1) }

    var isEditOptionSheetOpen by remember { mutableStateOf(false) }
    var isEditTitleDialogOpen by remember { mutableStateOf(false) }
    var isEditDateScreenOpen by remember { mutableStateOf(false) }


    val editedTitle by remember { mutableStateOf(plan.title) }



    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding(),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Box {
                Image(
                    painter = painterResource(id = getImageForDestination(plan.destination)),
                    contentDescription = "여행지 이미지",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "뒤로가기",
                        tint = Color.Black.copy(alpha = 0.7f)
                    )
                }
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = "플랜 삭제",
                        tint = Color.Black.copy(alpha = 0.7f)
                    )
                }
            }
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = plan.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "편집",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.clickable { isEditOptionSheetOpen = true })
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${plan.startedDate} ~ ${plan.endDate}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "총 일정 ${plan.schedules.size}개 / ₩${plan.schedules.sumOf { it.cost ?: 0 }}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        dayDateList.forEach { (day, dateText) ->
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        text = "Day $day - $dateText",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    val schedules = plan.schedules.filter { it.day == day }
                    schedules.forEach { schedule ->
                        ScheduleItem(
                            schedule = schedule,
                            onScheduleClicked = { selectedSchedule = schedule })
                    }

//                    OutlinedButton(
//                        onClick = {
//                            selectedDay = day
//                            isAddLocationModalOpen = true
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 12.dp),
//                        shape = RoundedCornerShape(10.dp)
//                    ) {
//                        Text("할 일 추가", color = Color.Black)
//                    }
                    AddScheduleCircleButton(
                        onClick = {
                            selectedDay = day
                            isAddLocationModalOpen = true
                        },
                    )


                }
            }
        }
    }

    // 스케쥴 추가 모달
    if (isAddLocationModalOpen) {
        ModalBottomSheet(
            sheetState = addLocationSheetState,
            onDismissRequest = { isAddLocationModalOpen = false },
            containerColor = Color.White,
        ) {
            AddScheduleBottomSheet(selectedDay = selectedDay, onSave = { desc, cost, memo, time ->
                planDetailViewModel.addSchedule(
                    description = desc,
                    time = time,
                    day = selectedDay,
                    cost = cost,
                    memo = memo,
                    planId = plan.id
                )
                isAddLocationModalOpen = false
            }, onCancel = { isAddLocationModalOpen = false })
        }
    }

    // 여행 수정 다이얼로그
    if (isEditOptionSheetOpen) {
        ModalBottomSheet(
            windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 20.dp),
            onDismissRequest = { isEditOptionSheetOpen = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color.White,
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "수정할 항목을 선택하세요",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black, // 수정 버튼 색상
                        contentColor = Color.White // 텍스트 색상
                    ),
                    onClick = {
                        isEditOptionSheetOpen = false
                        isEditTitleDialogOpen = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                ) {
                    Text("여행 제목 수정")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black, // 수정 버튼 색상
                        contentColor = Color.White // 텍스트 색상
                    ), onClick = {
                        isEditOptionSheetOpen = false
                        isEditDateScreenOpen = true // ← 캘린더로 이동
                        // 날짜 수정은 추후 처리
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                ) {
                    Text("여행 날짜 수정")
                }
            }
        }
    }

    // 날짜 수정 다이얼로그
    if (isEditDateScreenOpen) {
        CalendarEditDialog(
            initialStartDate = LocalDate.parse(plan.startedDate),
            initialEndDate = LocalDate.parse(plan.endDate),
            onConfirm = { newStartDate, newEndDate ->
                planDetailViewModel.updatePlan(
                    planId = plan.id,
                    startDate = newStartDate.toString(),
                    endDate = newEndDate.toString(),
                    onSuccess = {
                        Toast.makeText(context, "날짜가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        isEditDateScreenOpen = false
                    },
                    onFailure = {
                        Toast.makeText(context, "수정 실패: $it", Toast.LENGTH_SHORT).show()
                    })
            },
            onDismiss = { isEditDateScreenOpen = false })
    }



    selectedSchedule?.let { schedule ->
        ModalBottomSheet(
            sheetState = scheduleSheetState,
            onDismissRequest = { selectedSchedule = null },
            containerColor = Color.White,
            modifier = Modifier.fillMaxHeight(0.25f),
            windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) // 네비게이션 바 패딩 제거
        ) {
            ScheduleDetailBottomSheet(
                schedule = schedule, onSaveMemo = { selectedSchedule = null })
        }
    }

    if (showDeleteDialog) {
        // 삭제 다이얼로그 표시
        PlanDeleteDialog(onConfirm = {
            planDetailViewModel.deletePlan(planId = plan.id, onSuccess = {
                Toast.makeText(context, "플랜이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                navController.popBackStack() // 삭제 후 이전 화면으로 이동
            }, onError = {
                Toast.makeText(context, "삭제 실패: $it", Toast.LENGTH_SHORT).show()
            })
            showDeleteDialog = false
        }, onDismiss = { showDeleteDialog = false })
    }


    EditTitleDialog(
        isDialogOpen = isEditTitleDialogOpen,
        currentTitle = editedTitle,
        onConfirm = { newTitle ->
            planDetailViewModel.updatePlan(planId = plan.id, title = newTitle, onSuccess = {
                Toast.makeText(context, "제목이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                isEditTitleDialogOpen = false

            }, onFailure = {
                Toast.makeText(context, "제목 수정 실패: $it", Toast.LENGTH_SHORT).show()
            })
        },
        onDismiss = { isEditTitleDialogOpen = false })
}

// 이미지 리소스 여행지 매핑 함수
fun getImageForDestination(destination: String): Int {
    return when (destination) {
        "제주도" -> R.drawable.jeju
        "부산" -> R.drawable.busan
        "강릉" -> R.drawable.gangneung
        "경주" -> R.drawable.gyeongju
        "여수" -> R.drawable.yeosu
        "일본" -> R.drawable.japan
        "몽골" -> R.drawable.mongolia
        "상하이" -> R.drawable.shanghai
        "필리핀" -> R.drawable.philippines
        else -> R.drawable.travel_icon // 기본 이미지 리소스 설정 필요
    }
}


@Composable
fun AddScheduleCircleButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFEFEFEF)) // 연회색 배경
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add, // Material의 + 아이콘
                contentDescription = "할 일 추가",
                tint = Color(0xFF666666), // 어두운 회색 (아이콘 색)
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
