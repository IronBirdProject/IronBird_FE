package com.example.greetingcard.presentation.view.home.planning

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.greetingcard.R
import com.example.greetingcard.data.model.response.UserInfo
import com.example.greetingcard.presentation.viewModel.user.UserViewModel


@Composable
fun PlanningScreen(
    navController: NavController, listState: LazyListState, userViewModel: UserViewModel
) {
    val user by userViewModel.userInfo.collectAsState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(
            bottom = 150.dp
        )
    ) {
        item {
            Spacer(Modifier.height(24.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 3.dp, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GreetingSection(user)
                    MyTravelCard(onClick = { navController.navigate("my_plan") })
                }
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
            PlanCreateSection(
                userName = user?.name ?: "여행자",
                onCreateClicked = { navController.navigate("plan_create") }
            )
        }

//        item {
//            Spacer(Modifier.height(16.dp))
//            FeaturesGrid(navController)
//        }

        item {
            Spacer(Modifier.height(16.dp))
            TravelTipSection()
        }
    }
}

@Composable
fun GreetingSection(user: UserInfo?) {
    Log.d("GreetingSection", "User: $user")
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = user?.profilePic,
                error = painterResource(R.drawable.profile_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text("${user?.name ?: "여행자"}님 👋", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("오늘도 여행을 시작해볼까요?", fontSize = 14.sp, color = Color.Gray)
            }
        }

        IconButton(onClick = { /* 알림 */ }) {
            Icon(Icons.Default.Notifications, contentDescription = "알림")
        }
    }
}

@Composable
fun MyTravelCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF8FF))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.airplane_icon),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text("내 여행 정보", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text("가장 가까운 여행을 확인해보세요!", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00CFFF))
            ) {
                Text("내 플랜", fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun PlanCreateSection(
    userName: String,
    onCreateClicked: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 인삿말 + 설명
            Text(
                text = "$userName 님😄\n새로운 여행을 계획해보세요!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "가장 가까운 여행 일정을 관리하고\n새로운 여행도 시작해보세요.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 여행 계획 버튼
            Button(
                onClick = onCreateClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00CFFF))
            ) {
                Text("여행 계획하기", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun TravelTipSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("✈️ 여행 꿀팁", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFFFFC107))
                Spacer(Modifier.width(8.dp))
                Text(
                    "현지에서는 유심보다 eSIM이 더 저렴할 수 있어요!",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Button(
                onClick = { /* TODO: 추천 리스트 페이지 이동 */ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00CFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("여행 체크리스트 보기")
            }
        }
    }
}


// 검색창
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    var text by remember {
        mutableStateOf("")
    }
    BasicTextField(
        value = text,
        singleLine = true,
        onValueChange = { text = it },
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp, vertical = 8.dp)
            .height(60.dp)
            .background(Color(0xFFF4F4F4), shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp),

        ) {
        TextFieldDefaults.DecorationBox(
            value = text,
            innerTextField = it,
            enabled = true,
            singleLine = false,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            placeholder = { Text("원하는 여행지를 검색해보세요!", fontSize = 14.sp, color = Color.Gray) },
            leadingIcon = {
                Icon(
                    tint = Color.Gray,
                    imageVector = Icons.Default.Search,
                    contentDescription = "search_btn",
                    modifier = Modifier.size(22.dp)
                )
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "clear_text",
                        tint = Color.LightGray,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                text = ""
                            })
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )
    }
}
