package com.example.greetingcard.presentation.view.plan.createplan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.greetingcard.presentation.navigation.Screen
import com.example.greetingcard.presentation.view.home.planning.CustomBottomBar
import com.example.greetingcard.presentation.viewModel.plan.PlanPreviewViewModel
import com.example.greetingcard.presentation.viewModel.plan.createplan.PlanCreateViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanTitleScreen(
    navController: NavController,
    rootNavController: NavController,
    planCreateViewModel: PlanCreateViewModel,
    planPreviewViewModel: PlanPreviewViewModel
) {
    var title by remember { mutableStateOf("") }
    val isButtonEnabled = title.isNotBlank()

    val isLoading by planCreateViewModel.isLoading.collectAsState()
    val showSuccessDialog by planCreateViewModel.isSuccess.collectAsState()


    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("여행 이름을 지어주세요", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            CustomBottomBar(
                label = "여행 만들기",
                enabled = isButtonEnabled,
                onBottomBarClick = {
                    planCreateViewModel.setTitle(title)
                    planCreateViewModel.createPlan()
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "여행 제목",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("예: 제주도 여름휴가 3박4일") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }

//            Button(
//                onClick = {
//                    planCreateViewModel.setTitle(title)
//                    planCreateViewModel.createPlan()
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(52.dp),
//                enabled = isButtonEnabled,
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text("여행 만들기", style = MaterialTheme.typography.titleMedium)
//            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)), // 반투명 배경
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp,
            )
        }
    }


    if (showSuccessDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = {},
            title = { Text("여행 생성 완료!") },
            text = { Text("여행 일정이 성공적으로 생성되었습니다.") },
            confirmButton = {
                TextButton(onClick = {
                    rootNavController.navigate(Screen.Home.route) {
                        popUpTo(Screen.PlanCreate.route) { inclusive = true }
                        launchSingleTop = true
                    }
//                    planPreviewViewModel.loadPlanPreviews() // 여행 목록 새로고침
                }) {
                    Text("확인")
                }
            }
        )
    }
}
