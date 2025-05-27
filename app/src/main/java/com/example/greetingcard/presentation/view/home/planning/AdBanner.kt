package com.example.greetingcard.presentation.view.home.planning

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


// 광고 배너 (임시)
@Composable
fun AdvertisementSection(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .height(200.dp)
            .background(Color(0xFFF0975B), shape = RoundedCornerShape(20.dp))
            .clickable { navController.navigate("map_test") }
    ) {
//        Text(
//            text = "지도 테스트",
//            modifier = Modifier.align(Alignment.Center),
//            textAlign = TextAlign.Center,
//            fontWeight = FontWeight.Bold
//        )

    }
}