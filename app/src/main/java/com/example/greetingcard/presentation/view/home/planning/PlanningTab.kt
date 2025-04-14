package com.example.greetingcard.presentation.view.home.planning

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.greetingcard.R


// 플래닝 화면
@Composable
fun PlanningScreen(navController: NavController, listState: LazyListState) {
    println("플래닝탭 listState: $listState")
    LazyColumn(
        state = listState,
    ) {
        item {
//            SearchBar()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(horizontal = 30.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row {
                    AsyncImage(
                        // TODO: 유저 프로필 이미지 url 주입
                        model = "",
                        error = painterResource(R.drawable.user_profile),
                        contentDescription = "user_info_profile_image",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillBounds
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "김수현",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "초보 여행자",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                IconButton(
                    modifier = Modifier.size(60.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xffefefef)
                    ),
                    onClick = {
                        // TODO: 알림 연동
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "notification",
                        modifier = Modifier.size(35.dp),
                    )
                }
            }
        }
        item {
            UserInfoCard(
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
                onUserProfileClicked = {
                    Log.d(
                        "UserInfo Click",
                        "============ Click ============"
                    )
                },
                onPlanClicked = { navController.navigate("my_plan") },
                onPostingClicked = { Log.d("Posting Click", "============ Click ============") })
        }
        item {
            FeaturesGrid()
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
            AdvertisementSection()
            Spacer(modifier = Modifier.height(60.dp))
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
                            }
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )
    }
}
