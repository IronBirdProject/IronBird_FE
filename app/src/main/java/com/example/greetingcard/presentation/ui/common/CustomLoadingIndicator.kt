package com.example.greetingcard.presentation.ui.common

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// TODO: 추후 디자인 업데이트(로딩 인디케이터)
@Composable
fun CustomLoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        color = Color.Black,
        trackColor = Color.Transparent,
    )
}