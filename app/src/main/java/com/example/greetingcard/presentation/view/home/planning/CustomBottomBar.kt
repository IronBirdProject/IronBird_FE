package com.example.greetingcard.presentation.view.home.planning

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.presentation.ui.theme.PurpleGrey40
import com.example.greetingcard.presentation.ui.theme.btnLightBlue

@Composable
fun CustomBottomBar(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean,
    onBottomBarClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            modifier = modifier
                .navigationBarsPadding()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 25.dp)
                .padding(top = 10.dp, bottom = 25.dp)
                .fillMaxWidth()
                .height(50.dp),
            enabled = enabled,
            colors = ButtonColors(
                contentColor = Color.White,
                containerColor = Color(0xff057bf6),
                disabledContentColor = PurpleGrey40,
                disabledContainerColor = btnLightBlue
            ),
            onClick = onBottomBarClick
        ) {
            Text(
                text = label, color = Color.White, style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }
    }
}