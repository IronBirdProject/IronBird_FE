package com.example.greetingcard.presentation.view.home.planning

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.greetingcard.R

@Composable
fun FeaturesGrid(navController: NavController) {
    val cardSize = (LocalConfiguration.current.screenWidthDp.dp - 60.dp) / 2

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        FeatureCard(
            width = cardSize,
            title = "여행 탐색",
            description = "인기 여행지 보기",
            img = R.drawable.travel_icon,
            onClick = { navController.navigate("explore") }
        )

        FeatureCard(
            width = cardSize,
            title = "환전 도우미",
            description = "여행지 환율 계산",
            img = R.drawable.map_img,
            onClick = { navController.navigate("exchange") }
        )
    }
}

@Composable
fun FeatureCard(
    width: Dp,
    title: String,
    description: String,
    img: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(width)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xfff5f5f5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(description, fontSize = 12.sp, color = Color.Gray)
            }
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = img),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}
