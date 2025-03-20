package com.example.greetingcard.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.greetingcard.presentation.ui.home.planning.PlanningScreen
import com.example.greetingcard.presentation.ui.home.posting.PostingTab
import com.example.greetingcard.presentation.viewModel.home.HomeViewModel

@Composable
fun HomePage(navController: NavHostController, homeViewModel: HomeViewModel = viewModel()) {
    // 탭 별로 liststate 선언
    val listStates = List(homeViewModel.tabs.size) { rememberLazyListState() }

    Scaffold(
        containerColor = Color.White,

        // 상단 바
        topBar = {
            CustomAppBar(homeViewModel, navController, listStates[homeViewModel.selectedTabIndex])
        },

        // 플로팅 버튼
        floatingActionButton = {
            // TODO: 추후 포스팅 탭에서 가장 가까운 여행 일정 페이지로 이동하는 플로팅 버튼 생성 예정
            if (homeViewModel.selectedTabIndex == 0) {
                CreatePlanFloatingButton {
                    navController.navigate("create_plan")
                }
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
                PlanningScreen(listState = listStates[0])
            } else {
                PostingTab(homeViewModel = homeViewModel, listState = listStates[1])
            }
        }
    }
}


// 플로팅 버튼 (플랜 추가 페이지 이동 버튼)
@Composable
fun CreatePlanFloatingButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        modifier = Modifier
            .padding(horizontal = 40.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(60.dp),
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 2.dp
        ),
        containerColor = Color(0xFFD4ECF7),
        shape = CircleShape,
        onClick = { onClick() },
        icon = {},
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "여행 일정 만들기", fontSize = 16.sp)
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    tint = Color.Gray,
                    contentDescription = "일정 만들기 FAB"
                )
            }
        },
    )
}

@Composable
fun BackButton() {

}