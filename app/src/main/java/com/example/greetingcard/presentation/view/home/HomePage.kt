package com.example.greetingcard.presentation.view.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.greetingcard.R
import com.example.greetingcard.presentation.view.home.planning.PlanningScreen
import com.example.greetingcard.presentation.view.home.posting.PostingTab
import com.example.greetingcard.presentation.viewModel.home.HomeViewModel
import com.example.greetingcard.presentation.viewModel.plan.PlanPreviewViewModel
import com.example.greetingcard.presentation.viewModel.user.UserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun HomePage(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    userViewModel: UserViewModel,
    planPreviewViewModel: PlanPreviewViewModel
) {
    // 탭 별로 liststate 선언
    val listStates = List(homeViewModel.tabs.size) { rememberLazyListState() }

    // 가장 가까운 여행 일정 가져오기
//    val upcomingPlan by planPreviewViewModel.nearestPlan.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.userInfo
        planPreviewViewModel.loadPlanPreviews() // TODO: 실제 로그인 유저 ID로 변경
//        Log.d("HomePage", "예정된 여행 ${upcomingPlan.toString()}")
    }

    // 안전하게 최초 실행 시 한번만 호출

    Scaffold(
        containerColor = Color.White,

        // 상단 바
        topBar = {
            CustomAppBar(
                homeViewModel,
                navController,
                listStates[homeViewModel.selectedTabIndex],
                onClickLogout = {
                    userViewModel.clearSession(onSuccess = {
                        Log.d("HomePage", "로그아웃 성공")
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                        Toast.makeText(
                            navController.context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT
                        ).show()
                    }, onFailure = { error ->
                        Log.e("HomePage", "로그아웃 실패: $error")
                        Toast.makeText(
                            navController.context, error, Toast.LENGTH_SHORT
                        ).show()
                    })
                })
        },

        // 플로팅 버튼
        floatingActionButton = {
            if (homeViewModel.selectedTabIndex == 0) {
                PlanUpcomingFloatingButton(
                    modifier = Modifier.padding(16.dp),
                    planPreviewViewModel = planPreviewViewModel,
                    navController = navController
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // selectedTabIndex에 따라 화면 변경
            if (homeViewModel.selectedTabIndex == 0) {
                PlanningScreen(
                    navController = navController,
                    listState = listStates[0],
                    userViewModel = userViewModel
                )
            } else {
                PostingTab(homeViewModel = homeViewModel, listState = listStates[1])
            }
        }
    }
}

// 예정된 여행 일정 플로팅 버튼
@Composable
fun PlanUpcomingFloatingButton(
    modifier: Modifier = Modifier,
    planPreviewViewModel: PlanPreviewViewModel,
    navController: NavController
) {
    // 가장 가까운 여행 일정 가져오기
    val plan by planPreviewViewModel.nearestPlan.collectAsState()
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                if (plan == null) {
                    return@clickable
                }
                navController.navigate("plan_detail/${plan?.id}")
            },
        color = Color.Transparent, // Surface 자체는 투명하게
        shadowElevation = 6.dp,
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF00D4C5), Color(0xFF0FBEE9))
                    ), shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(
                        id = getImageForDestination(
                            plan?.destination ?: "여행지"
                        )
                    ),
                    contentDescription = "여행지 이미지",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = plan?.title ?: "여행 일정이 없습니다",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = plan?.let {
                        val dDay = calculateDDay(it.startedDate)
                        val period = formatDateRange(it.startedDate, it.endDate)
                        "D-$dDay | $period"
                    } ?: "날짜 정보 없음", fontSize = 13.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "일정 아이콘",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "내 일정", fontSize = 12.sp, color = Color.White
                    )
                }
            }
        }
    }
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

fun calculateDDay(startDate: String): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return try {
        val date = LocalDate.parse(startDate, formatter)
        ChronoUnit.DAYS.between(LocalDate.now(), date)
    } catch (e: Exception) {
        0
    }
}

fun formatDateRange(startDate: String, endDate: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("M.d(E)", Locale.KOREAN)

        val start = LocalDate.parse(startDate, formatter).format(outputFormatter)
        val end = LocalDate.parse(endDate, formatter).format(outputFormatter)

        "$start - $end"
    } catch (e: Exception) {
        "$startDate ~ $endDate"
    }
}

