package com.example.greetingcard.presentation.view.my_info

import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.greetingcard.R
import com.example.greetingcard.data.model.response.PlanPreview
import com.example.greetingcard.presentation.view.plan.component.PlanDeleteDialog
import com.example.greetingcard.presentation.viewModel.plan.PlanPreviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPlanScreen(navController: NavController, planPreviewViewModel: PlanPreviewViewModel) {
    val planList by planPreviewViewModel.planPreviews.collectAsState()
    val isLoading by planPreviewViewModel.isLoading.collectAsState()
    val error by planPreviewViewModel.error.collectAsState()

    // 삭제 다이얼로그 상태
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteTargetPlanId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        planPreviewViewModel.loadPlanPreviews() // 로그인 유저 ID
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                title = { Text("내 플랜", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Clear, contentDescription = "닫기")
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("로딩 중...")
            }

            error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("오류: $error")
            }

            else -> LazyColumn(
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
            ) {
                item {
                    Text(
                        text = "다가오는 여행",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(planList, key = { it.id }) { preview ->
                    PlanCard(
                        plan = preview,
                        onClick = {
                            navController.navigate("plan_detail/${preview.id}")
                        },
                        onEdit = {
                            // 수정 로직 (예: 다이얼로그 열기)
//                            viewModel.openEditDialog(preview)
                        },
                        onDelete = {
                            // 삭제 로직 (예: 다이얼로그 열기)
                            deleteTargetPlanId = preview.id
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }

        if (showDeleteDialog) {
            Log.d("플랜 삭제 다이얼로그", "삭제 다이얼로그 표시: $deleteTargetPlanId")
            // 삭제 다이얼로그 표시
            PlanDeleteDialog(
                onConfirm = {
                    // 삭제 로직 호출
                    planPreviewViewModel.deletePlan(
                        planId = deleteTargetPlanId
                            ?: return@PlanDeleteDialog, // 안전하게 ID가 null이 아닌지 확인
                        onSuccess = {
                            Toast.makeText(
                                navController.context,
                                "플랜이 삭제되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onFailure = { errorMessage ->
                            // 삭제 실패 처리 (예: Toast 메시지)
                            Toast.makeText(
                                navController.context,
                                "플랜 삭제 실패: $errorMessage",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                    showDeleteDialog = false
                    Toast.makeText(
                        navController.context,
                        "플랜이 삭제되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onDismiss = { showDeleteDialog = false }
            )
        }
    }
}

@Composable
fun PlanCard(
    plan: PlanPreview,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 썸네일 이미지 (고정 or 서버에서 받아오기)
            Image(
                painter = painterResource(id = getImageForDestination(plan.destination)),
                contentDescription = "Plan thumbnail",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plan.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${plan.startedDate} ~ ${plan.endDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "더보기")
                }

                DropdownMenu(
                    modifier = Modifier
                        .background(Color.White),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("수정") },
                        onClick = {
                            expanded = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("삭제") },
                        onClick = {
                            expanded = false
                            onDelete()
                        }
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
