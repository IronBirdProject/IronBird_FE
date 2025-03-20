package com.example.greetingcard.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.greetingcard.presentation.viewModel.home.HomeViewModel


// 상단 바
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
    listState: LazyListState,
) {
    // 탭 목록
    val tabs = homeViewModel.tabs
    // 현재 선택된 탭 인덱스
    val selectedTabIndex = homeViewModel.selectedTabIndex
    // scroll여부 판단 변수 (selectedTabIndex) 값에 따라 hasScrolled를 다시 계산
    val hasScrolled by remember(selectedTabIndex) {
        derivedStateOf { listState.firstVisibleItemScrollOffset > 0 }
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
        ),
        modifier = Modifier.drawWithContent {
            drawContent()
            if (hasScrolled) {
                println("hasScrolled True")
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 0.5.dp.toPx()
                )
            }
        },
        title = {
            // 상단 탭 바
            TabRow(
                modifier = Modifier.fillMaxWidth(0.7f),
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = Color.Black,
                indicator = { tabPositions ->
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Color.Black,
                        width = 45.dp,
                        height = 2.dp,
                    )
                },
                divider = {
                    HorizontalDivider(
                        color = Color.White,
                        thickness = 0.dp,
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { homeViewModel.selectTab(index) },
                        text = {
                            Text(
                                text = title,
                                fontSize = 18.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.W500 else FontWeight.W400,
                                color = if (selectedTabIndex == index) Color.Black else Color.Gray,
                            )
                        },
                    )
                }
            }
        },
        actions = {
            Icon(
                Icons.Default.AddToPhotos,
                modifier = Modifier.clickable {
                    navController.navigate("create_post")
                },
                contentDescription = "add_post",
                tint = Color.Black,
            )
            CustomDropDownMenu(homeViewModel, navController = navController)
        }
    )
}


// 드롭다운 메뉴
@Composable
fun CustomDropDownMenu(
    homeViewModel: HomeViewModel = viewModel(),
    navController: NavHostController
) {
    Box {
        IconButton(onClick = {
            homeViewModel.toggleDropDownMenu()
        }) {
            Icon(Icons.Default.MoreVert, contentDescription = "menu")
        }
        DropdownMenu(
            expanded = homeViewModel.isDropDownExpanded,
            onDismissRequest = {
                homeViewModel.toggleDropDownMenu()
            },
            modifier = Modifier.background(Color.White)
        ) {
            DropdownMenuItem(
                text = { Text("설정") },
                onClick = { } // TODO: 추후 페이지 이동
            )
            DropdownMenuItem(
                text = { Text("기타") },
                onClick = { } // TODO: 추후 페이지 이동
            )
        }
    }
}