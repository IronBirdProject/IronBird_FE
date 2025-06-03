package com.example.greetingcard.presentation.view.home.planning

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.R

@Composable
fun UserInfoCard(
    modifier: Modifier = Modifier,
    onUserProfileClicked: () -> Unit,
    onPlanClicked: () -> Unit,
    onPostingClicked: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xfff2f2f2)),
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // 👉 Plan 영역 전체 클릭되도록 수정
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onPlanClicked() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(R.drawable.airplane_icon),
                        contentDescription = "Plan",
                        modifier = Modifier.size(36.dp)
                    )
                    Text("Plan", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }

            VerticalDivider(
                color = Color.Gray,
                thickness = 0.5.dp,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxHeight()
            )

            // 👉 Post 영역 전체 클릭되도록 수정
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onPostingClicked() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.send_icon),
                        contentDescription = "Post",
                        modifier = Modifier.size(36.dp)
                    )
                    Text("Post", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
