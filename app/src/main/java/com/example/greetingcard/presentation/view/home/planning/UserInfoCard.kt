package com.example.greetingcard.presentation.view.home.planning

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffefefef),
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Box {
            Column {
                Row(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .clickable { onPlanClicked() }
                    ) {
//                        Icon(
//                            imageVector = Icons.Default.,
//                            contentDescription = "여행계획",
//                            modifier = Modifier.size(40.dp)
//                        )
                        Image(
                            painter = painterResource(R.drawable.airplane_icon),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(40.dp),
                            contentDescription = "travel_icon"
                        )
                        Text("Plan", fontSize = 14.sp)
                    }
                    VerticalDivider(
                        color = Color.Gray,
                        thickness = 0.2.dp,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .clickable { onPostingClicked() }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.send_icon),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(40.dp),
                            contentDescription = "posting_icon"
                        )
                        Text("Post", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}